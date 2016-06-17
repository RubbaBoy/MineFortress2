package com.uddernetworks.tf2.guns;

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

import java.util.HashSet;
import java.util.List;

public class GunListener implements Listener {

    static HashSet<Player> zoom = new HashSet<Player>();

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            for (Object gun_obj : GunList.getGunlist().values()) {
                GunObject gun = (GunObject) gun_obj;
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    if (gun.getClip() > 0) {
                        gun.setClip(gun.getClip() - 1);
                        Snowball bullet = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
                        bullet.setShooter(player);
                        bullet.setVelocity(player.getEyeLocation().getDirection().multiply(gun.getPower()));
                        player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                    } else {
                        gun.reloadGun();
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
                    if (shooter.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                        event.setDamage(gun.getDamage());

                        List<Entity> near = shooter.getWorld().getEntities();
//                        Bukkit.getPlayer("RubbaBoy").sendMessage("Bullet location is: " + bullet.getLocation().toString());
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
