package com.uddernetworks.tf2.guns.custom.demoman;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class StickyBomb extends Demoman {

    private Item itemdrop;
    private StickyBombPlayers players = new StickyBombPlayers();

    public StickyBomb(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
        try {

            if (players.canHaveMore(player)) {
                if (!held) {
                    itemdrop = player.getLocation().getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SNOW_BALL));
                    itemdrop.setCustomName("StickyBomb");
                    itemdrop.setCustomNameVisible(false);
                    itemdrop.setVelocity(player.getLocation().getDirection().normalize().multiply(gun.getPower()));
                    players.setPlayer(getPlayer(), this, System.currentTimeMillis());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            explode();
                        }
                    }.runTaskLater(Main.getPlugin(), 80);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void explode() {
        try {
            if (itemdrop != null && !itemdrop.isDead()) {
                Particles.spawnExplosionParticles(itemdrop.getLocation(), 2);
                itemdrop.remove();
                List<Entity> near = getPlayer().getWorld().getEntities();
                near.stream().filter(e -> e instanceof LivingEntity).filter(e -> e.getLocation().distance(itemdrop.getLocation()) < 3).forEach(e -> ((Damageable) e).damage(getGun().getDamage()));
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}