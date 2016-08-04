package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.sentry.Sentry;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.*;
import com.uddernetworks.tf2.utils.threads.GunThreadUtil;
import net.minecraft.server.v1_9_R1.BlockPosition;
import net.minecraft.server.v1_9_R1.EntityArmorStand;
import net.minecraft.server.v1_9_R1.PacketPlayOutBlockBreakAnimation;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

    static HashSet<Player> zoom = new HashSet<Player>();

    static HashMap3<Player, Long, State> primary_cooldowns = new HashMap3<Player, Long, State>();
    static HashMap3<Player, Long, State> secondary_cooldowns = new HashMap3<Player, Long, State>();
    static HashMap<Player, Long> melee_cooldowns = new HashMap<Player, Long>();

    public GunListener(Main main, GunThreadUtil thread) {
        this.main = main;
        this.thread = thread;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            for (int i = 0; i < GunList.getGunlist().size(); i++) {
                GunObject gun = GunList.getGunAt(i);
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    event.setCancelled(true);
                    if (gun.isSniper()) {
                        if (primary_cooldowns.containsKey(player)) {
                            int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                            if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() <= 0) {
                                primary_cooldowns.put(player, System.currentTimeMillis());
                                primary_cooldowns.setT(player, State.NORMAL);
                                if (playerGuns.getClip(player) > 0) {
                                    playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                                    player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                                    new Bullet(player, gun);
                                } else {
                                    primary_cooldowns.put(player, System.currentTimeMillis());
                                    primary_cooldowns.setT(player, State.RELOADING);
                                    playerGuns.reloadGun(player);
                                }
                            }
                        } else if (!primary_cooldowns.containsKey(player)) {
                            primary_cooldowns.put(player, System.currentTimeMillis());
                            primary_cooldowns.setT(player, State.NORMAL);
                            if (playerGuns.getClip(player) > 0) {
                                playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                                player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                                new Bullet(player, gun);
                            } else {
                                primary_cooldowns.put(player, System.currentTimeMillis());
                                primary_cooldowns.setT(player, State.RELOADING);
                                playerGuns.reloadGun(player);
                            }
                        } else if (primary_cooldowns.containsKey(player)) {
                            int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                            if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() > 0) {
                                event.setCancelled(true);
//                        player.sendMessage("You cant use that for another " + ((primary_cooldowns.get(player) + gun.getCooldown()) - System.currentTimeMillis()) / 1000 + " seconds!");
                            }
                        }
                    }
                }
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            for (int i = 0; i < GunList.getGunlist().size(); i++) {
                GunObject gun = GunList.getGunAt(i);
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    if (gun.getScopeable()) {
                        if (!zoom.contains(player)) {
                            zoom.add(player);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90000000, 255, true, false));

                            player.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));

                            if (gun.getNVscope()) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 90000000, 1, true, false));
                            }
                        } else {
                            zoom.remove(player);
                            player.removePotionEffect(PotionEffectType.SLOW);

                            player.getInventory().setHelmet(new ItemStack(Material.AIR));

                            if (gun.getNVscope()) {
                                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow bullet = (Arrow) event.getDamager();
            if (bullet.getShooter() instanceof Player) {
                Bullet bullet1 = new Bullet();
                if (event.getEntity() instanceof ArmorStand) {
                    if (bullet1.isBulletFromSentry(bullet.getCustomName())) {
                        event.setCancelled(true);
                    }
                } else {
                    Player shooter = (Player) bullet.getShooter();
                    if (bullet1.isBullet(bullet.getCustomName())) {
                        GunObject gun = GunList.getGunAt(Integer.parseInt(bullet.getCustomName()));
                        if (gun.getType() == WeaponType.PRIMARY || gun.getType() == WeaponType.SECONDARY) {
                            event.setCancelled(true);
                            bullet.remove();
                            HitByBulletEvent event2 = new HitByBulletEvent(event.getEntity(), gun);
                            Bukkit.getPluginManager().callEvent(event2);
                        }
                    } else if (bullet1.isBulletFromSentry(bullet.getCustomName())) {
                        if (NumberUtils.isNumber(bullet.getCustomName().substring(6, bullet.getCustomName().length()))) {
                            int id = Integer.parseInt(bullet.getCustomName().substring(6, bullet.getCustomName().length()));
                            Sentry sentry = Sentries.getSentry(id);

                            bullet.remove();
                            HitByBulletEvent event2 = new HitByBulletEvent(event.getEntity(), sentry);
                            Bukkit.getPluginManager().callEvent(event2);
                        }
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
                        try {
                            if (player.getInventory().getItemInMainHand().serialize().toString().equals(GunList.getWrench().getItemStack().serialize().toString())) {
                                if (Sentries.isObjectSentry((ArmorStand) event.getEntity())) {
                                    Sentry sentry = Sentries.getSentry((ArmorStand) event.getEntity());
                                    sentry.upgrade();
                                    event.setCancelled(true);
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (player.getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {

                            event.setCancelled(true);
                            if (melee_cooldowns.containsKey(player) && (melee_cooldowns.get(player) + gun.getCooldown()) - System.currentTimeMillis() <= 0) {
                                playerHealth.addHealth((Player) victim, playerHealth.getHealth((Player) victim) - gun.getDamage());
                                List<Entity> near = victim.getWorld().getEntities();
                                for (Entity e : near) {
                                    if (e.getLocation().distance(victim.getLocation()) <= gun.getKZR()) {
                                        if (e instanceof Player) {
                                            playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gun.getDamage());
                                        }
                                    }
                                }

                                melee_cooldowns.put(player, System.currentTimeMillis());

                            } else if (!melee_cooldowns.containsKey(player)) {
                                if (victim instanceof Player) {
                                    playerHealth.addHealth((Player) victim, playerHealth.getHealth((Player) victim) - gun.getDamage());
                                    List<Entity> near = victim.getWorld().getEntities();
                                    for (Entity e : near) {
                                        if (e.getLocation().distance(victim.getLocation()) <= gun.getKZR()) {
                                            if (e instanceof Player) {
                                                playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gun.getDamage());
                                            }
                                        }
                                    }
                                }

                                melee_cooldowns.put(player, System.currentTimeMillis());

                            } else if (melee_cooldowns.containsKey(player) && (melee_cooldowns.get(player) + gun.getCooldown()) - System.currentTimeMillis() > 0) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow bullet = (Arrow) event.getEntity();
            Entity entity = event.getEntity();
            if (bullet.getShooter() instanceof Player) {
                Bullet bullet1 = new Bullet();
                if (bullet1.isBullet(bullet.getCustomName())) {
                    GunObject gun = GunList.getGunAt(Integer.parseInt(bullet.getCustomName()));
                    HitByBulletEvent event2 = new HitByBulletEvent(entity, gun);
                    Bukkit.getPluginManager().callEvent(event2);
                    bullet.remove();
                } else if (bullet1.isBulletFromSentry(bullet.getCustomName())) {
                    if (NumberUtils.isNumber(bullet.getCustomName().substring(6, bullet.getCustomName().length()))) {
                        int id = Integer.parseInt(bullet.getCustomName().substring(6, bullet.getCustomName().length()));
                        Sentry sentry = Sentries.getSentry(id);

                        HitByBulletEvent event2 = new HitByBulletEvent(entity, sentry);
                        Bukkit.getPluginManager().callEvent(event2);
                        bullet.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBulletHitEntity(HitByBulletEvent event) {
        Entity target = event.getEntity();
        if (target instanceof Player) {
            Player player = (Player) target;
            if (event.usesGun()) {
                GunObject gun = event.getGun();
                player.setHealth(player.getMaxHealth());
                playerHealth.addHealth(player, playerHealth.getHealth(player) - gun.getDamage());
                List<Entity> near = player.getWorld().getEntities();
                for (Entity e : near) {
                    if (e.getLocation().distance(player.getLocation()) <= gun.getKZR()) {
                        if (e instanceof Player) {
                            if (e != target) {
                                playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gun.getDamage());
                            }
                        }
                    }
                }
            } else {
                Sentry sentry = event.getSentry();
                player.setHealth(player.getMaxHealth());
                playerHealth.addHealth(player, playerHealth.getHealth(player) - sentry.getDamage());
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) throws InterruptedException {
        Player player = event.getPlayer();
        long currTime = System.currentTimeMillis();
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            for (int i = 0; i < GunList.getGunlist().size(); i++) {
                GunObject gun = GunList.getGunAt(i);
                if (player.getInventory().getItemInMainHand().serialize().equals(gun.getItemStack().serialize())) {

                    event.setCancelled(true);
                    if (primary_cooldowns.containsKey(player)) {
                        int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                        if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() <= 0) {
                            primary_cooldowns.put(player, System.currentTimeMillis());
                            primary_cooldowns.setT(player, State.NORMAL);
                            if (playerGuns.getClip(player) > 0) {
                                if (!gun.isSniper()) {
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
                        if (playerGuns.getClip(player) > 0) {
                            if (!gun.isSniper()) {
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
                }
            }
        }
    }

    @EventHandler
    public void arrowHit(ArrowHitBlockEvent event) {
        Arrow bullet = event.getArrow();
        Block block = event.getBlock();

        if (bullet.getShooter() instanceof Player) {
            Player shooter = (Player) bullet.getShooter();
            Bullet bullet1 = new Bullet();
            if (bullet1.isBullet(bullet.getCustomName())) {
                GunObject gun = GunList.getGunAt(Integer.parseInt(bullet.getCustomName()));
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