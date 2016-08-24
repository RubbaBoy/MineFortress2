package com.uddernetworks.tf2.command;

import com.uddernetworks.tf2.arena.ArenaManager;
import com.uddernetworks.tf2.arena.TeamChooser;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.SQLLoadout;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTF2 implements CommandExecutor {

    Main plugin;

    public CommandTF2(Main passedPlugin) {
        this.plugin = passedPlugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tf2")) {
            sender.sendMessage("Sending you to pick a team...");
            TeamChooser chooser = new TeamChooser(plugin);
            try {
                ArenaManager.getManager().createArena();
                chooser.sendPlayers();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}