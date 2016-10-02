package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Win {

    Main plugin;

    public void Win(TeamEnum team) {
        try {
            if (team == TeamEnum.RED) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Game.sendTitle(player, "The Red Team Has Won!", "", 5, 110, 5);
                }
            } else if (team == TeamEnum.BLUE) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Game.sendTitle(player, "The Blue Team Has Won!", "", 5, 110, 5);
                }
            }
            Main.getPlugin().onDisable();
            Main.getPlugin().onEnable();
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void setMain(Main main) {
        plugin = main;
    }

}