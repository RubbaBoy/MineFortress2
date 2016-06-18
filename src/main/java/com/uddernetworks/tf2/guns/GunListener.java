package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.utils.HashMap3;
import com.uddernetworks.tf2.utils.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GunListener implements Listener {

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

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            for (Object gun_obj : GunList.getGunlist().values()) {
                GunObject gun = (GunObject) gun_obj;
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    event.setCancelled(true);
                    if (primary_cooldowns.containsKey(player)) {
                        int cooldown = primary_cooldowns.getT(player).getValue() ? gun.getCooldown() : gun.getCooldownReload();
                        if ((primary_cooldowns.get(player) + cooldown) - System.currentTimeMillis() <= 0) {
                            primary_cooldowns.put(player, System.currentTimeMillis());
                            primary_cooldowns.setT(player, State.NORMAL);
                            if (gun.getClip() > 0) {
                                gun.setClip(gun.getClip() - 1);
                                Snowball bullet = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
                                bullet.setShooter(player);
                                bullet.setVelocity(player.getEyeLocation().getDirection().multiply(gun.getPower()));
                                player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                            } else {
                                primary_cooldowns.put(player, System.currentTimeMillis());
                                primary_cooldowns.setT(player, State.RELOADING);
                                gun.reloadGun();
                            }
                        }
                    } else if (!primary_cooldowns.containsKey(player)){
                        primary_cooldowns.put(player, System.currentTimeMillis());
                        primary_cooldowns.setT(player, State.NORMAL);
                        if (gun.getClip() > 0) {
                            gun.setClip(gun.getClip() - 1);
                            Snowball bullet = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
                            bullet.setShooter(player);
                            bullet.setVelocity(player.getEyeLocation().getDirection().multiply(gun.getPower()));
                            player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                        } else {
                            primary_cooldowns.put(player, System.currentTimeMillis());
                            primary_cooldowns.setT(player, State.RELOADING);
                            gun.reloadGun();
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
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            for (Object gun_obj : GunList.getGunlist().values()) {
                GunObject gun = (GunObject) gun_obj;
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    if (gun.getScopeable()) {
                        if (!zoom.contains(player)) {
                            zoom.add(player);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90000000, 255, true, false));

                            if (gun.getNVscope()) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 90000000, 1, true, false));
                            }
                        } else {
                            zoom.remove(player);
                            player.removePotionEffect(PotionEffectType.SLOW);

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
        if (event.getDamager() instanceof Snowball) {
            Snowball bullet = (Snowball) event.getDamager();
            if (bullet.getShooter() instanceof Player) {
                Player shooter = (Player) bullet.getShooter();
                for (Object gun_obj : GunList.getGunlist().values()) {
                    GunObject gun = (GunObject) gun_obj;
                    if (gun.getType() == WeaponType.PRIMARY || gun.getType() == WeaponType.SECONDARY) {
                        if (shooter.getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {
                            event.setDamage(gun.getDamage());
                            List<Entity> near = shooter.getWorld().getEntities();
                            for (Entity e : near) {
                                if (e.getLocation().distance(bullet.getLocation()) <= gun.getKZR()) {
                                    if (e instanceof Damageable) {
                                        ((Damageable) e).damage(gun.getDamage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity victim = event.getEntity();
            for (Object gun_obj : GunList.getGunlist().values()) {
                GunObject gun = (GunObject) gun_obj;
                if (gun.getType() == WeaponType.MELEE) {
                    if (player.getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {

                        event.setCancelled(true);
                        if (melee_cooldowns.containsKey(player) && (melee_cooldowns.get(player) + gun.getCooldown()) - System.currentTimeMillis() <= 0) {
                            event.setDamage(gun.getDamage());
                            List<Entity> near = victim.getWorld().getEntities();
                            for (Entity e : near) {
                                if (e.getLocation().distance(victim.getLocation()) <= gun.getKZR()) {
                                    if (e instanceof Damageable) {
                                        ((Damageable) e).damage(gun.getDamage());
                                    }
                                }
                            }

                            melee_cooldowns.put(player, System.currentTimeMillis());

                        } else if (!melee_cooldowns.containsKey(player)){
                            event.setDamage(gun.getDamage());
                            List<Entity> near = victim.getWorld().getEntities();
                            for (Entity e : near) {
                                if (e.getLocation().distance(victim.getLocation()) <= gun.getKZR()) {
                                    if (e instanceof Damageable) {
                                        ((Damageable) e).damage(gun.getDamage());
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

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
            if (event.getEntity() instanceof Snowball) {
                Snowball bullet = (Snowball) event.getEntity();
                Entity entity = event.getEntity();
                if (bullet.getShooter() instanceof Player) {
                    Player shooter = (Player) bullet.getShooter();
                    for (Object gun_obj : GunList.getGunlist().values()) {
                        GunObject gun = (GunObject) gun_obj;
                        if (shooter.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                            List<Entity> near = entity.getWorld().getEntities();
                            for (Entity e : near) {
                                if (e.getLocation().distance(entity.getLocation()) <= gun.getKZR()) {
                                    if (e instanceof Damageable) {
                                        ((Damageable) e).damage(gun.getDamage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

}
