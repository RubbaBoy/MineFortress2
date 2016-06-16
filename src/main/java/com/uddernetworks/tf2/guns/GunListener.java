package com.uddernetworks.tf2.guns;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GunListener implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            System.out.println("Someone clicked the air!");
            for (Object gun_obj : GunList.getGunlist().values()) {
                GunObject gun = (GunObject) gun_obj;
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    Snowball bullet = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
                    bullet.setShooter(player);
                    bullet.setVelocity(player.getLocation().getDirection().multiply(4));
                    player.playSound(player.getLocation(), gun.getSound(), 1, 1);
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
//                        System.out.println("Damage: " + gun.getDamage());
                    }
                }
            }
        }
    }

}
