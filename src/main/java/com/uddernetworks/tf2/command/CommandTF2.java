package com.uddernetworks.tf2.command;

import com.uddernetworks.tf2.guns.sentry.Sentry;
import com.uddernetworks.tf2.inv.AdminGunList;
import com.uddernetworks.tf2.inv.ClassChooser;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                if (args.length == 0) {
                    ClassChooser classChooser = new ClassChooser();
                    player.sendMessage("Opening the admin gun menu...");

                    classChooser.openGUI(player);
                    return true;
                } else if (args[0].equalsIgnoreCase("sentry")) {
                    Sentry sentry = new Sentry(player.getLocation(), 10);
                    sentry.spawnSentry();
                    return true;
                }
            } else {
                sender.sendMessage("You need to be a player to run this command!");
                return true;
            }
        }
        return false;
    }

}