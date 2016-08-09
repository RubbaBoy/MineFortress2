package com.uddernetworks.tf2.command;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.arena.TeamChooser;
import com.uddernetworks.tf2.guns.sentry.Sentry;
import com.uddernetworks.tf2.inv.AdminGunList;
import com.uddernetworks.tf2.inv.ClassChooser;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            TeamChooser chooser = new TeamChooser(plugin);
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage("Opening the team chooser");
                    chooser.sendPlayers();
                    return true;
                } else if (args[0].equalsIgnoreCase("start")) {
                    if (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() == Bukkit.getOnlinePlayers().size()) {
                        player.sendMessage(ChatColor.BLUE + "Players in blue team: " + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE));
                        player.sendMessage(ChatColor.RED + "Players in red team: " + PlayerTeams.getPlayersInTeam(TeamEnum.RED));
                    } else {
                        player.sendMessage("Not all players have chosen a team yet! there is " + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()) + "/" + Bukkit.getOnlinePlayers().size());
                    }
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