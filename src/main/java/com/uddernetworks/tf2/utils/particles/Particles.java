package com.uddernetworks.tf2.utils.particles;

import com.uddernetworks.tf2.main.Main;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class Particles {

    public static void spawnHealthParticles(Location locTo, Location locFrom) {
        Random random = new Random();
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, (float) (locTo.getX() + random.nextFloat() - 0.2), (float) (locTo.getY() + random.nextFloat() + 0.8), (float) (locTo.getZ() + random.nextFloat() - 0.2), 0, 0, 0, 0, 5, null);
        PacketPlayOutWorldParticles packet2 = new PacketPlayOutWorldParticles(EnumParticle.CRIT_MAGIC, true, (float) (locTo.getX() + random.nextFloat() - 0.2), (float) (locTo.getY() + random.nextFloat() + 0.8), (float) (locTo.getZ() + random.nextFloat() - 0.2), 0, 0, 0, 0, 5, null);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);
        }
    }


    private Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        return Class.forName(name);
    }
    private Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        return conField.get(nmsPlayer);
    }
}