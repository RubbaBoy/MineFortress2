package com.uddernetworks.tf2.utils.particles;

import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.TeamEnum;
import net.minecraft.server.v1_10_R1.EnumParticle;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class Particles {

    public static void spawnHealthParticles(Location locTo, Location locFrom, TeamEnum team) {
        int particles = 50;
//        Random random = new Random();
//        locTo.getWorld().spigot().playEffect(locTo, Effect.POTION_SWIRL, 0, 0, 75F / 255.0F, 86F / 255.0F, 255F / 255.0F, 1, 0, 2);

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

//        Vector link = locTo.toVector().subtract(locFrom.toVector());
//        float length = (float)link.length();
//        link.normalize();
//
//        float ratio = length / 100;
//        Vector v = link.multiply(ratio);
//        Location loc = locFrom.clone().subtract(v);
//        for (int i = 0; i < 100; i++) {
//            loc.add(v);
//            locTo.getWorld().spigot().playEffect(loc, Effect.POTION_SWIRL, 0, 0, R / 255.0F, G / 255.0F, B / 255.0F, 1, 0, 1);
//        }

        Vector link = locTo.toVector().subtract(locFrom.toVector());
        float length = (float)link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = locFrom.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            loc.add(v);
            loc.getWorld().spigot().playEffect(loc, Effect.POTION_SWIRL, 0, 0, R / 255.0F, G / 255.0F, B / 255.0F, 10, 0, 1);
        }
    }
}