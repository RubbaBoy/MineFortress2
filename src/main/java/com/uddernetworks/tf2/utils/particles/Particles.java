package com.uddernetworks.tf2.utils.particles;

import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particles {

    static PlayerHealth playerHealth = new PlayerHealth();

    public static void spawnHealthParticles(Location locTo, Location locFrom, TeamEnum team) {
        int particles = 10;
        float R;
        float G;
        float B;

        if (team == TeamEnum.BLUE) {
            R = 75F;
            G = 86F;
            B = 255F;
        } else {
            R = 251F;
            G = 119F;
            B = 80F;
        }

        Vector link = locTo.toVector().subtract(locFrom.toVector());
        float length = (float)link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = locFrom.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            loc.add(v);
            loc.getWorld().spigot().playEffect(loc, Effect.POTION_SWIRL, 0, 0, R / 255.0F, G / 255.0F, B / 255.0F, 10, 0, 100);
        }
    }

    public static void spawnFlamethrowerParticles(Player player) {
        int particles = 8;
        Game game = new Game(Main.getPlugin());
        List<Entity> entities = game.getWorld().getEntities();
        double t = 0;
        Location loc = player.getEyeLocation().clone();
        Vector direction = loc.getDirection().normalize();
        for (int i = 0; i < particles; i++) {
            t = t + 0.5;
            double x = direction.getX() * t;
            double y = direction.getY() * t;
            double z = direction.getZ() * t;
            loc.add(x, y, z);
            entities.stream().filter(entity -> entity instanceof Player && entity != player).filter(entity -> entity.getLocation().distance(loc) < 0.1).forEach(entity -> {
                if (playerHealth.getHealth((Player) entity) - 2 < 0) {
                    playerHealth.addHealth((Player) entity, 0);
                } else {
                    playerHealth.addHealth((Player) entity, playerHealth.getHealth((Player) entity) - 2);
                }
            });
            loc.getWorld().spigot().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0, 0, 1, 1, 1, 100, i + 1, 100);
            loc.subtract(x, y, z);
        }
    }

    public static void spawnExplosionParticles(Location location, int count) {
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 5F, 5F);
        location.getWorld().playEffect(location, Effect.EXPLOSION_HUGE, count);
    }
}