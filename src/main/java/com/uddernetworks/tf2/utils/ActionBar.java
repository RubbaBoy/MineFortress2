package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

    public ActionBar(Player player, String text, ChatColor... color) {
        try {
            String colors = "";
            for (ChatColor aColor : color) {
                colors += aColor.toString();
            }
            IChatBaseComponent bar_message = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + colors + text + "\"}");
            PacketPlayOutChat bar = new PacketPlayOutChat(bar_message, (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public ActionBar(Player player, StringBuilder text, ChatColor... color) {
        try {
            String colors = "";
            for (ChatColor aColor : color) {
                colors += aColor.toString();
            }
            IChatBaseComponent bar_message = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + colors + text + "\"}");
            PacketPlayOutChat bar = new PacketPlayOutChat(bar_message, (byte) 2);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}
