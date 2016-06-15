package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.main.Game;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Win {

    Main plugin;

//    Game game = new Game(plugin);

    public void Win(TeamEnum team) {
        if (team == TeamEnum.RED) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Game.sendTitle(player, "The Red Team Has Won!", "", 5, 110, 5);
            }
        } else if (team == TeamEnum.BLUE){
            for (Player player : Bukkit.getOnlinePlayers()) {
                Game.sendTitle(player, "The Blue Team Has Won!", "", 5, 110, 5);
            }
        } else if (team == TeamEnum.MACHINE) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Game.sendTitle(player, "The Machines Have Won!", "", 5, 110, 5);
            }
        }
        Main.getPlugin().onDisable();
        Main.getPlugin().onEnable();
    }

    public void setMain(Main main) {
        plugin = main;
    }

}
