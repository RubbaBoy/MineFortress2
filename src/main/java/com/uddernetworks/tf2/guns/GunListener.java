package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.arena.ArenaManager;
import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.arena.TeamChooser;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.custom.Controller;
import com.uddernetworks.tf2.guns.custom.demoman.StickyBombPlayers;
import com.uddernetworks.tf2.guns.dispenser.Dispenser;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.sentry.Sentry;
import com.uddernetworks.tf2.guns.teleporter.TeleporterEntrance;
import com.uddernetworks.tf2.guns.teleporter.TeleporterExit;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.*;
import com.uddernetworks.tf2.utils.data.PlayerSlots;
import com.uddernetworks.tf2.utils.threads.GunThreadUtil;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityArmorStand;
import net.minecraft.server.v1_10_R1.PacketPlayOutBlockBreakAnimation;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GunListener implements Listener {

    public Main main;
    private GunThreadUtil thread;

    private PlayerGuns playerGuns = new PlayerGuns();
    private PlayerHealth playerHealth = new PlayerHealth();

    private enum State {
        NORMAL(true),
        RELOADING(false);

        private boolean value;

        State(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }
    }

    static HashSet<Player> zoom = new HashSet<>();
    static HashMap<Player, Boolean> zoom_second = new HashMap<>();

    static HashMap3<Player, Long, State> primary_cooldowns = new HashMap3<>();
    static HashMap<Player, Long> melee_cooldowns = new HashMap<>();

    public GunListener(Main main, GunThreadUtil thread) {
        try {
            this.main = main;
            this.thread = thread;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        try {
            if (ArenaManager.getManager().isInGame(event.getPlayer())) {
                Player player = event.getPlayer();
                if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                    for (int i = 0; i < GunList.getGunlist().size(); i++) {
                        GunObject gun = GunList.getGunAt(i);
                        if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                            event.setCancelled(true);
                            if (!DeadMode.isInDeadMode(player)) {

                                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                    if (gun.getCustom() != null) {
                                        if (gun.getCustom().equalsIgnoreCase("engineer.wrench")) {
                                            if (Dispensers.isObjectDispenser(event.getClickedBlock())) {
                                                if (PlayerMetal.getPlayer(player) >= 200) {
                                                    PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) - 200);
                                                    Dispensers.getDispenser(event.getClickedBlock()).upgrade();
                                                }
                                            }
                                            return;
                                        }
                                    }
                                }

                                if (gun.leftClick()) {
                                    if (Controller.canProceed(player)) {
                                        if (primary_cooldowns.containsKey(player)) {
                                            int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                                            if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() <= 0) {
                                                primary_cooldowns.put(player, System.currentTimeMillis());
                                                primary_cooldowns.setT(player, State.NORMAL);
                                                if (playerGuns.getClip(player) > 0 && gun.getCustom() == null) {
                                                    playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                                                    if (gun.getSound() != null) {
                                                        player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                                                    }
                                                    if (gun.isShotgun()) {
                                                        for (int i2 = 0; i2 < gun.getShotgun_bullet(); i2++) {
                                                            new Bullet(player, gun);
                                                        }
                                                    } else {
                                                        new Bullet(player, gun);
                                                    }
                                                } else if (gun.getCustom() != null) {
                                                    if (gun.getMaxClip() != 0 && gun.getMaxAmmo() != 0) {
                                                        if (playerGuns.getClip(player) > 0) {
                                                            Controller controller = new Controller(gun, player, false);
                                                            try {
                                                                if (Controller.canProceed(player)) {
                                                                    controller.run();
                                                                } else {
                                                                    controller.sendStopNotify(player);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            primary_cooldowns.put(player, System.currentTimeMillis());
                                                            primary_cooldowns.setT(player, State.RELOADING);
                                                            playerGuns.reloadGun(player);
                                                        }
                                                    } else {
                                                        Controller controller = new Controller(gun, player, false);
                                                        try {
                                                            if (Controller.canProceed(player)) {
                                                                controller.run();
                                                            } else {
                                                                controller.sendStopNotify(player);
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                } else {
                                                    primary_cooldowns.put(player, System.currentTimeMillis());
                                                    primary_cooldowns.setT(player, State.RELOADING);
                                                    playerGuns.reloadGun(player);
                                                }
                                            }
                                        } else if (!primary_cooldowns.containsKey(player)) {
                                            primary_cooldowns.put(player, System.currentTimeMillis());
                                            primary_cooldowns.setT(player, State.NORMAL);
                                            if (playerGuns.getClip(player) > 0 || (playerGuns.getPlayerGun(player).getMaxClip() == 0 && playerGuns.getPlayerGun(player).getMaxAmmo() == 0)) {
                                                if (gun.getCustom() != null) {
                                                    Controller controller = new Controller(gun, player, false);
                                                    try {
                                                        if (Controller.canProceed(player)) {
                                                            controller.run();
                                                        } else {
                                                            controller.sendStopNotify(player);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                                                    if (gun.getSound() != null) {
                                                        player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                                                    }
                                                    if (gun.isShotgun()) {
                                                        for (int i2 = 0; i2 < gun.getShotgun_bullet(); i2++) {
                                                            new Bullet(player, gun);
                                                        }
                                                    } else {
                                                        new Bullet(player, gun);
                                                    }
                                                }
                                            } else {
                                                primary_cooldowns.put(player, System.currentTimeMillis());
                                                primary_cooldowns.setT(player, State.RELOADING);
                                                playerGuns.reloadGun(player);
                                            }
                                        } else if (primary_cooldowns.containsKey(player)) {
                                            int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                                            if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() > 0) {
                                                event.setCancelled(true);
                                            }
                                        }
                                    } else {
                                        if (gun.getCustom() != null) {
                                            Controller controller = new Controller(gun, player, false);
                                            try {
                                                if (Controller.canProceed(player)) {
                                                    controller.run();
                                                } else {
                                                    controller.sendStopNotify(player);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                    for (int i = 0; i < GunList.getGunlist().size(); i++) {
                        GunObject gun = GunList.getGunAt(i);
                        if (player.getInventory().getItemInMainHand() == null) {
                            return;
                        }
                        if (gun.getItemStack() == null) {
                            return;
                        }
                        if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                            if (!Controller.canProceed(player)) {
                                return;
                            }
                            if (gun.getScopeable()) {
                                if (!zoom.contains(player)) {
                                    if (!zoom_second.containsKey(player)) {
                                        zoom_second.put(player, false);
                                    }
                                    i = GunList.getGunlist().size();
                                    if (zoom_second.containsKey(player) && zoom_second.get(player)) {
                                        zoom_second.put(player, false);
                                        zoom.add(player);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90000000, 255, true, false));

                                        player.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));

                                        if (gun.getNVscope()) {
                                            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 90000000, 1, true, false));
                                        }
                                    } else if (zoom_second.containsKey(player) && !zoom_second.get(player)) {
                                        zoom_second.put(player, true);
                                    }
                                } else {
                                    i = GunList.getGunlist().size();
                                    if (zoom_second.containsKey(player) && zoom_second.get(player)) {
                                        zoom_second.put(player, false);
                                        zoom.remove(player);
                                        player.removePotionEffect(PotionEffectType.SLOW);

                                        player.getInventory().setHelmet(new ItemStack(Material.AIR));

                                        if (gun.getNVscope()) {
                                            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                                        }
                                    } else if (zoom_second.containsKey(player) && !zoom_second.get(player)) {
                                        zoom_second.put(player, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        try {
            if (event.getDamager() instanceof Player) {
                if (!ArenaManager.getManager().isInGame((Player) event.getDamager())) {
                    return;
                }
            }
            if (event.getEntity() instanceof Player) {
                if (!ArenaManager.getManager().isInGame((Player) event.getEntity())) {
                    return;
                }
            }
            if (event.getDamager() instanceof Arrow) {
                Bullet bullet1 = new Bullet();
                Arrow bullet = (Arrow) event.getDamager();
                if (bullet.getShooter() instanceof Player) {
                    Player shooter = (Player) bullet.getShooter();
                    if (event.getEntity() instanceof ArmorStand) {
                        event.setCancelled(true);
                        GunObject gun = GunList.getGunAt(Integer.parseInt(bullet.getCustomName()));
                        ((ArmorStand) event.getEntity()).setHealth(((ArmorStand) event.getEntity()).getMaxHealth());
                        HitByBulletEvent event2 = new HitByBulletEvent(event.getEntity(), gun, shooter);
                        Bukkit.getPluginManager().callEvent(event2);
                        event.getDamager().remove();

                    } else {
                        if (bullet1.isBullet(bullet.getCustomName())) {
                            GunObject gun = GunList.getGunAt(Integer.parseInt(bullet.getCustomName()));
                            if (gun.getType() == WeaponType.PRIMARY || gun.getType() == WeaponType.SECONDARY) {
                                event.setCancelled(true);
                                bullet.remove();
                                HitByBulletEvent event2 = new HitByBulletEvent(event.getEntity(), gun, shooter);
                                Bukkit.getPluginManager().callEvent(event2);
                            }
                        } else if (bullet1.isBulletFromSentry(bullet.getCustomName())) {
                            if (NumberUtils.isNumber(bullet.getCustomName().substring(7, bullet.getCustomName().length()))) {
                                int id = Integer.parseInt(bullet.getCustomName().substring(7, bullet.getCustomName().length()));
                                Sentry sentry = Sentries.getSentry(id);

                                bullet.remove();
                                HitByBulletEvent event2 = new HitByBulletEvent(event.getEntity(), sentry);
                                Bukkit.getPluginManager().callEvent(event2);
                            }
                        }
                    }
                } else if (bullet.getShooter() instanceof ArmorStand){
                    if (event.getEntity() instanceof ArmorStand) {
                        if (bullet1.isBulletFromSentry(bullet.getCustomName())) {
                            ((ArmorStand) event.getEntity()).setHealth(((ArmorStand) event.getEntity()).getMaxHealth());
                            bullet.remove();
                            event.setCancelled(true);
                        }
                    } else if (event.getEntity() instanceof Player){
                        if (bullet1.isBulletFromSentry(bullet.getCustomName())) {
                            int id = Integer.parseInt(bullet.getCustomName().substring(7, bullet.getCustomName().length()));
                            Sentry sentry = Sentries.getSentry(id);

                            bullet.remove();
                            HitByBulletEvent event2 = new HitByBulletEvent(event.getEntity(), sentry);
                            Bukkit.getPluginManager().callEvent(event2);
                            event.setCancelled(true);
                        }
                    }
                }
            } else if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                Entity victim = event.getEntity();
                for (int i = 0; i < GunList.getGunlist().size(); i++) {
                    GunObject gun = GunList.getGunAt(i);
                    if (gun.getType() == WeaponType.MELEE) {
                        if (event.getEntity() instanceof ArmorStand) {
                            if (gun.getCustom() != null) {
                                if (gun.getCustom().equalsIgnoreCase("engineer.wrench")) {
                                    if (Sentries.isObjectSentry((ArmorStand) event.getEntity())) {
                                        if (PlayerMetal.getPlayer(player) >= 200) {
                                            PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) - 200);
                                            Sentry sentry = Sentries.getSentry((ArmorStand) event.getEntity());
                                            sentry.upgrade();
                                        }
                                    }
                                }
                            }
                            event.setCancelled(true);
                        } else if (victim instanceof Player && PlayerTeams.getPlayer((Player) victim) != PlayerTeams.getPlayer(player)) {
                            if (player.getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {

                                event.setCancelled(true);
                                if (melee_cooldowns.containsKey(player) && (melee_cooldowns.get(player) + gun.getCooldown()) - System.currentTimeMillis() <= 0) {
                                    if (playerHealth.addHealth((Player) victim, playerHealth.getHealth((Player) victim) - gun.getDamage())) {
                                        DeathMessage deathMessage = new DeathMessage((Player) victim, player, gun);
                                        deathMessage.sendMessage();
                                    }
                                    List<Entity> near = victim.getWorld().getEntities();
                                    for (Entity e : near) {
                                        if (e.getLocation().distance(victim.getLocation()) <= gun.getKZR()) {
                                            if (e instanceof Player) {
                                                if (e != victim) {
                                                    if (playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gun.getDamage())) {
                                                        DeathMessage deathMessage = new DeathMessage((Player) e, player, gun);
                                                        deathMessage.sendMessage();
                                                    }
                                                    DamageIndicator.spawnIndicator(gun.getDamage(), e.getWorld(), e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ());
                                                }
                                            }
                                        }
                                    }

                                    melee_cooldowns.put(player, System.currentTimeMillis());

                                } else if (!melee_cooldowns.containsKey(player)) {
                                    if (playerHealth.addHealth((Player) victim, playerHealth.getHealth((Player) victim) - gun.getDamage())) {
                                        DeathMessage deathMessage = new DeathMessage((Player) victim, player, gun);
                                        deathMessage.sendMessage();
                                    }
                                    List<Entity> near = victim.getWorld().getEntities();
                                    for (Entity e : near) {
                                        if (e.getLocation().distance(victim.getLocation()) <= gun.getKZR()) {
                                            if (e instanceof Player) {
                                                if (e != victim) {
                                                    if (playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gun.getDamage())) {
                                                        DeathMessage deathMessage = new DeathMessage((Player) e, player, gun);
                                                        deathMessage.sendMessage();
                                                    }
                                                    DamageIndicator.spawnIndicator(gun.getDamage(), e.getWorld(), e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ());
                                                }
                                            }
                                        }
                                    }

                                    melee_cooldowns.put(player, System.currentTimeMillis());

                                } else if (melee_cooldowns.containsKey(player) && (melee_cooldowns.get(player) + gun.getCooldown()) - System.currentTimeMillis() > 0) {
                                    event.setCancelled(true);
                                }
                            }
                        } else if (victim instanceof Fireball) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else if (event.getDamager() instanceof Fireball) {
                if (event.getEntity() instanceof Player) {
                    Fireball fireball = (Fireball) event.getDamager();
                    Player player = (Player) event.getEntity();
                    FireballHitEvent event2 = new FireballHitEvent(fireball, player);
                    Bukkit.getPluginManager().callEvent(event2);
                    event.setCancelled(true);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onFireballExplode(EntityExplodeEvent event) {
        try {
            if (event.getEntity() instanceof Fireball) {
                if (ArenaManager.getManager().getArena() != null) {
                    event.setCancelled(true);
                    Fireball fireball = (Fireball) event.getEntity();
                    FireballHitEvent event2 = new FireballHitEvent(fireball);
                    Bukkit.getPluginManager().callEvent(event2);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onFireballhit(FireballHitEvent event) {
        try {
            Fireball fireball = event.getFireball();
            if (fireball.getShooter() instanceof Player) {
                Player shooter = (Player) fireball.getShooter();
                if (fireball.getCustomName().contains("fireball_")) {
                    if (GunList.isGunId(Integer.parseInt(fireball.getCustomName().replace("fireball_", "")))) {
                        GunObject gun = GunList.getGunAt(Integer.parseInt(fireball.getCustomName().replace("fireball_", "")));
                        List<Entity> near = shooter.getWorld().getEntities();
                        if (event.hitPlayer()) {
                            Player player = event.getPlayer();
                            if (PlayerTeams.getPlayer(player) != PlayerTeams.getPlayer(shooter)) {
                                if (playerHealth.addHealth(player, playerHealth.getHealth(player) - gun.getDamage())) {
                                    DeathMessage deathMessage = new DeathMessage(player, shooter, gun);
                                    deathMessage.sendMessage();
                                }
                                DamageIndicator.spawnIndicator(gun.getDamage(), player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                            }
                        } else {
                            near.stream().filter(e -> e instanceof Player && PlayerTeams.getPlayer((Player) e) != PlayerTeams.getPlayer(shooter)).forEach(e -> {
                                if (e.getLocation().distance(fireball.getLocation()) <= gun.getKZR()) {
                                    if (PlayerTeams.getPlayer((Player) e) != PlayerTeams.getPlayer(shooter)) {
                                        if (playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gun.getDamage())) {
                                            DeathMessage deathMessage = new DeathMessage((Player) e, shooter, gun);
                                            deathMessage.sendMessage();
                                        }
                                        DamageIndicator.spawnIndicator(gun.getDamage(), e.getWorld(), e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ());
                                    }
                                }
                            });
                        }
                        fireball.remove();
                    }
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onBulletHitEntity(HitByBulletEvent event) {
        try {
            if (event.isEntity()) {
                if (event.getEntity() instanceof Player) {
                    if (!ArenaManager.getManager().isInGame((Player) event.getEntity())) {
                        return;
                    }
                }
            }
            try {
                if (event.isEntity() && event.getEntity() instanceof Player && PlayerTeams.getPlayer(event.getDamager()) != PlayerTeams.getPlayer((Player) event.getEntity())) {
                    Player player = (Player) event.getEntity();
                    if (event.usesGun()) {
                        GunObject gun = event.getGun();
                        player.setHealth(player.getMaxHealth());
                        if (playerHealth.addHealth(player, playerHealth.getHealth(player) - gun.getDamage())) {
                            DeathMessage deathMessage = new DeathMessage(player, event.getDamager(), gun);
                            deathMessage.sendMessage();
                        }
                        List<Entity> near = player.getWorld().getEntities();
                        near.stream().filter(e -> e.getLocation().distance(player.getLocation()) <= gun.getKZR()).filter(e -> e instanceof Player).filter(e -> e != player).forEach(e -> {
                            if (playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gun.getDamage())) {
                                DeathMessage deathMessage = new DeathMessage((Player) e, event.getDamager(), gun);
                                deathMessage.sendMessage();
                            }
                            DamageIndicator.spawnIndicator(gun.getDamage(), e.getWorld(), e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ());
                        });
                    } else {
                        Sentry sentry = event.getDamagingSentry();
                        player.setHealth(player.getMaxHealth());
                        if (playerHealth.addHealth(player, playerHealth.getHealth(player) - sentry.getDamage())) {
                            DeathMessage deathMessage = new DeathMessage(player, event.getDamager(), sentry);
                            deathMessage.sendMessage();
                        }
                    }
                } else if (event.isSentry()) {
                    Sentry sentry = event.getSentry();
                    sentry.getObj().setHealth(20F);
                    if (event.usesGun()) {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamager())) {
                            sentry.setHealth(sentry.getHealth() - (int) event.getGun().getDamage());
                            DamageIndicator.spawnIndicator(event.getGun().getDamage(), event.getSentry().getObj().getLocation().getWorld(), event.getSentry().getObj().getLocation().getX(), event.getSentry().getObj().getLocation().getY(), event.getSentry().getObj().getLocation().getZ());
                        }
                    } else {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamagingSentry().getPlayer())) {
                            sentry.setHealth(sentry.getHealth() - (int) event.getSentry().getDamage());
                            DamageIndicator.spawnIndicator(event.getDamagingSentry().getDamage(), event.getSentry().getObj().getLocation().getWorld(), event.getSentry().getObj().getLocation().getX(), event.getSentry().getObj().getLocation().getY(), event.getSentry().getObj().getLocation().getZ());
                        }
                    }
                } else if (event.isDispenser()) {
                    Dispenser dispenser = event.getDispenser();
                    if (event.usesGun()) {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamager())) {
                            dispenser.setHealth(dispenser.getHealth() - (int) event.getGun().getDamage());
                            DamageIndicator.spawnIndicator(event.getGun().getDamage(), event.getDispenser().getLocation().getWorld(), event.getDispenser().getLocation().getX(), event.getDispenser().getLocation().getY(), event.getDispenser().getLocation().getZ());
                        }
                    } else {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamagingSentry().getPlayer())) {
                            dispenser.setHealth(dispenser.getHealth() - (int) event.getSentry().getDamage());
                            DamageIndicator.spawnIndicator(event.getDamagingSentry().getDamage(), event.getDispenser().getLocation().getWorld(), event.getDispenser().getLocation().getX(), event.getDispenser().getLocation().getY(), event.getDispenser().getLocation().getZ());
                        }
                    }
                } else if (event.isTeleporterEntrance()) {
                    TeleporterEntrance teleporterEntrance = event.getTeleporterEntrance();
                    if (event.usesGun()) {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamager())) {
                            teleporterEntrance.setHealth(teleporterEntrance.getHealth() - (int) event.getGun().getDamage());
                            DamageIndicator.spawnIndicator(event.getGun().getDamage(), event.getTeleporterEntrance().getLocation().getWorld(), event.getTeleporterEntrance().getLocation().getX(), event.getTeleporterEntrance().getLocation().getY(), event.getTeleporterEntrance().getLocation().getZ());
                        }
                    } else {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamagingSentry().getPlayer())) {
                            teleporterEntrance.setHealth(teleporterEntrance.getHealth() - (int) event.getSentry().getDamage());
                            DamageIndicator.spawnIndicator(event.getDamagingSentry().getDamage(), event.getTeleporterEntrance().getLocation().getWorld(), event.getTeleporterEntrance().getLocation().getX(), event.getTeleporterEntrance().getLocation().getY(), event.getTeleporterEntrance().getLocation().getZ());
                        }
                    }
                } else if (event.isTeleporterExit()) {
                    TeleporterExit teleporterExit = event.getTeleporterExit();
                    if (event.usesGun()) {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamager())) {
                            teleporterExit.setHealth(teleporterExit.getHealth() - (int) event.getGun().getDamage());
                            DamageIndicator.spawnIndicator(event.getGun().getDamage(), event.getTeleporterExit().getLocation().getWorld(), event.getTeleporterExit().getLocation().getX(), event.getTeleporterExit().getLocation().getY(), event.getTeleporterExit().getLocation().getZ());
                        }
                    } else {
                        if (PlayerTeams.getPlayer(event.getSentry().getPlayer()) != PlayerTeams.getPlayer(event.getDamagingSentry().getPlayer())) {
                            teleporterExit.setHealth(teleporterExit.getHealth() - (int) event.getSentry().getDamage());
                            DamageIndicator.spawnIndicator(event.getDamagingSentry().getDamage(), event.getTeleporterExit().getLocation().getWorld(), event.getTeleporterExit().getLocation().getX(), event.getTeleporterExit().getLocation().getY(), event.getTeleporterExit().getLocation().getZ());
                        }
                    }
                }
            } catch (NullPointerException ignored) {}
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        try {
            if (ArenaManager.getManager().isInGame(event.getPlayer())) {
                Player player = event.getPlayer();
                long currTime = System.currentTimeMillis();
                if (event.getHand() == EquipmentSlot.HAND) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            for (GunObject gun : GunList.getGunlist()) {
                                if (player.getInventory().getItemInMainHand().clone().serialize().equals(gun.getItemStack().clone().serialize())) {
                                    if (gun.getCustom() != null && gun.getCustom().equalsIgnoreCase("demoman.stickybomb")) {
                                        StickyBombPlayers players = new StickyBombPlayers();
                                        players.explode(player);
                                        return;
                                    }
                                    event.setCancelled(true);
                                    if (gun.getType() != WeaponType.MELEE) {
                                        try {
                                            if (primary_cooldowns.containsKey(player)) {
                                                int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                                                if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() <= 0) {
                                                    primary_cooldowns.put(player, System.currentTimeMillis());
                                                    primary_cooldowns.setT(player, State.NORMAL);
                                                    if (playerGuns.getClip(player) > 0 || (gun.getMaxClip() == 0 && gun.getMaxAmmo() == 0)) {
                                                        if (!gun.leftClick()) {
                                                            if (!GunThreadUtil.clickPlayers.containsKey(event.getPlayer().getName()) || GunThreadUtil.clickPlayers.get(event.getPlayer().getName()) == null) {
                                                                GunThreadUtil.clickPlayers.put(event.getPlayer().getName(), currTime);
                                                                GunThreadUtil.shot.put(player, gun);
                                                                GunThreadUtil.shot.setT(player, System.currentTimeMillis());
                                                                return;
                                                            }

                                                            long lastClick = GunThreadUtil.clickPlayers.get(event.getPlayer().getName());
                                                            if (currTime - lastClick > 260) {
                                                                GunThreadUtil.clickPlayers.remove(event.getPlayer().getName());
                                                                GunThreadUtil.shot.remove(player);
                                                            } else {
                                                                GunThreadUtil.clickPlayers.put(event.getPlayer().getName(), currTime);
                                                            }
                                                        }
                                                    } else {
                                                        primary_cooldowns.put(player, System.currentTimeMillis());
                                                        primary_cooldowns.setT(player, State.RELOADING);
                                                        GunThreadUtil.clickPlayers.remove(event.getPlayer().getName());
                                                        GunThreadUtil.shot.remove(player);
                                                        playerGuns.reloadGun(player);
                                                    }
                                                }
                                            } else if (!primary_cooldowns.containsKey(player)) {
                                                primary_cooldowns.put(player, System.currentTimeMillis());
                                                primary_cooldowns.setT(player, State.NORMAL);
                                                if (playerGuns.getClip(player) > 0 || (gun.getMaxClip() == 0 && gun.getMaxAmmo() == 0)) {
                                                    if (!gun.leftClick()) {
                                                        if (!GunThreadUtil.clickPlayers.containsKey(event.getPlayer().getName())) {
                                                            if (GunThreadUtil.clickPlayers.isEmpty()) {
                                                                GunThreadUtil.clickPlayers.put(event.getPlayer().getName(), currTime);
                                                                GunThreadUtil.shot.put(player, gun);
                                                                GunThreadUtil.shot.setT(player, System.currentTimeMillis());
                                                                return;
                                                            }
                                                        }

                                                        long lastClick = GunThreadUtil.clickPlayers.get(event.getPlayer().getName());
                                                        if (currTime - lastClick > 260) {
                                                            GunThreadUtil.clickPlayers.remove(event.getPlayer().getName());
                                                            GunThreadUtil.shot.remove(player);
                                                        } else {
                                                            GunThreadUtil.clickPlayers.put(event.getPlayer().getName(), currTime);
                                                        }
                                                    }
                                                } else {
                                                    primary_cooldowns.put(player, System.currentTimeMillis());
                                                    primary_cooldowns.setT(player, State.RELOADING);
                                                    GunThreadUtil.clickPlayers.remove(event.getPlayer().getName());
                                                    GunThreadUtil.shot.remove(player);
                                                    playerGuns.reloadGun(player);
                                                }
                                            } else if (primary_cooldowns.containsKey(player)) {
                                                int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                                                if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() > 0) {
                                                    event.setCancelled(true);
                                                }
                                            }
                                        } catch (NullPointerException ignored) {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void arrowHit(ArrowHitBlockEvent event) {
        try {
            if (event.getArrow().getShooter() instanceof Player) {
                if (ArenaManager.getManager().isInGame((Player) event.getArrow().getShooter())) {
                    Arrow bullet = event.getArrow();
                    Block block = event.getBlock();

                    if (bullet.getShooter() instanceof Player) {
                        Bullet bullet1 = new Bullet();
                        if (bullet1.isBullet(bullet.getCustomName())) {
                            Player shooter = (Player) bullet.getShooter();
                            GunObject gun = GunList.getGunAt(Integer.parseInt(bullet.getCustomName()));
                            HitByBulletEvent event2 = new HitByBulletEvent(block.getLocation(), gun, shooter);
                            Bukkit.getPluginManager().callEvent(event2);
                            if (!gun.isSniper()) {
                                if (gun.getDamage() >= 2 && gun.getDamage() < 5) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 9);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                } else if (gun.getDamage() >= 5 && gun.getDamage() < 10) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 5);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                } else if (gun.getDamage() >= 10 && gun.getDamage() < 20) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 7);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                } else if (gun.getDamage() >= 20) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 9);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                }
                                bullet.remove();
                            }
                        } else if (bullet1.isBulletFromSentry(bullet.getCustomName())) {
                            if (NumberUtils.isNumber(bullet.getCustomName().substring(6, bullet.getCustomName().length()))) {
                                int id = Integer.parseInt(bullet.getCustomName().substring(6, bullet.getCustomName().length()));
                                Sentry sentry = Sentries.getSentry(id);

                                if (sentry.getDamage() >= 2 && sentry.getDamage() < 5) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 9);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                } else if (sentry.getDamage() >= 5 && sentry.getDamage() < 10) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 5);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                } else if (sentry.getDamage() >= 10 && sentry.getDamage() < 20) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 7);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                } else if (sentry.getDamage() >= 20) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        EntityArmorStand entity;
                                        entity = new EntityArmorStand(((CraftWorld) block.getLocation().getWorld()).getHandle());
                                        ((CraftWorld) block.getLocation().getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                                        PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ()), (byte) 9);
                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                                    }
                                }
                                bullet.remove();
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onHungerSwitch(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (ArenaManager.getManager().isInGame(player)) {
                player.setFoodLevel(6);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        try {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (ArenaManager.getManager().isInGame(player)) {
                    event.setCancelled(true);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            if (!ArenaManager.getManager().isInGame(event.getPlayer())) {
                TeamChooser chooser = new TeamChooser(main);
                chooser.sendPlayer(event.getPlayer());
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (ArenaManager.getManager().isInGame(player)) {
            ArenaManager.getManager().removePlayer(player);
        }
    }

    @EventHandler
    public void ArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        try {
            if (ArenaManager.getManager().isInGame(event.getPlayer())) {
                event.setCancelled(true);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onPlayerSlotSwitch(PlayerItemHeldEvent event) {
        try {
            Player player = event.getPlayer();
            int newslot = event.getNewSlot();
            if (ArenaManager.getManager().isInGame(event.getPlayer())) {
                if (newslot == 1 || newslot == 2 || newslot == 7 || newslot == 8) {
                    for (int i = 0; i < GunList.getGunlist().size(); i++) {
                        GunObject gun = GunList.getGunAt(i);
                        if (player.getInventory().getItemInMainHand().serialize().equals(gun.getItemStack().serialize())) {
                            if (!event.isCancelled()) {
                                zoom_second.put(player, false);
                                zoom.remove(player);
                                player.removePotionEffect(PotionEffectType.SLOW);
                                player.getInventory().setHelmet(new ItemStack(Material.AIR));

                                if (gun.getNVscope()) {
                                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                                }

                                event.setCancelled(true);
                                PlayerSlots playerSlots = new PlayerSlots(main);
                                playerSlots.addPlayer(player, newslot);
                            }
                        }
                    }
                } else {
                    event.setCancelled(true);
                    player.getInventory().setHeldItemSlot(0);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onWindowClose(InventoryCloseEvent event) {
        try {
            if (event.getPlayer() instanceof Player) {
                if (ArenaManager.getManager().isInGame((Player) event.getPlayer())) {
                    PlayerSlots playerSlots = new PlayerSlots(main);
                    playerSlots.rawAdd((Player) event.getPlayer(), 0);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}