package com.uddernetworks.tf2.command;

import com.uddernetworks.tf2.inv.AdminGunList;
import com.uddernetworks.tf2.main.Main;
import me.confuser.barapi.BarAPI;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CommandTF2 implements CommandExecutor {

    Main plugin;

    public CommandTF2(Main passedPlugin) {
        this.plugin = passedPlugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tf2")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                AdminGunList adminGunList = new AdminGunList();
                player.sendMessage("Opening the admin gun menu...");
                adminGunList.openGUI(player);
                return true;
            } else {
                sender.sendMessage("You need to be a player to run this command!");
                return true;
            }
        }
        return false;
    }

}