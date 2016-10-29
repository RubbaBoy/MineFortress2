package com.uddernetworks.tf2.guns.custom.demoman;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.DamageIndicator;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.Hitbox;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class GrenadeLauncher extends Demoman {

    PlayerHealth playerHealth = new PlayerHealth();

    public GrenadeLauncher(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
        try {

            if (!held) {
                final Item itemdrop = player.getLocation().getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.NETHER_BRICK));
                itemdrop.setCustomName("GrenadeLauncher");
                itemdrop.setCustomNameVisible(false);
                itemdrop.setVelocity(player.getLocation().getDirection().normalize().multiply(gun.getPower()));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!itemdrop.isDead()) {
                            List<Entity> near = getPlayer().getWorld().getEntities();
                            near.stream().filter(e -> e instanceof Player).filter(e -> e.getLocation().distance(itemdrop.getLocation()) < 3).forEach(e -> {
                                playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - getGun().getDamage());
                                DamageIndicator.spawnIndicator(getGun().getDamage(), e.getLocation().getWorld(), e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ());
                            });
                            Particles.spawnExplosionParticles(itemdrop.getLocation(), 2);
                            itemdrop.remove();
                        }
                    }

                }.runTaskLater(Main.getPlugin(), 46);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}
