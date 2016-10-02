package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.util.Random;

public class DamageIndicator {

    public static void spawnIndicator(double damage, World world, double x, double y, double z) {
        try {
            Random random = new Random();
            float VelX = (float) -0.2 + (float) (Math.random() * 0.1);
            float VelY = Math.abs((float) 0.3 + (float) (Math.random() * 0.1));
            float VelZ = (float) -0.2 + (float) (Math.random() * 0.1);
            ArmorStand stand = (ArmorStand) world.spawnEntity(new Location(world, x + 0.15 + (1.25 * random.nextDouble()), y - 1 + (1.25 * random.nextDouble()), z + 0.15 + (1.25 * random.nextDouble())), EntityType.ARMOR_STAND);
            stand.setVelocity(new Vector(VelX, VelY, VelZ));
            stand.setGravity(true);
            stand.setSmall(false);
            stand.setVisible(false);
            stand.setCustomName(ChatColor.RED + "" + damage);
            stand.setCustomNameVisible(true);

            Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), stand::remove, 7L + (long) (random.nextDouble() * (2L)));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}
