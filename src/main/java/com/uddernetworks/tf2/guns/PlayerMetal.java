package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.gui.MetalGUI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerMetal {

    private static HashMap<Player, Integer> playerMetal = new HashMap<>();

    public static void addPlayer(Player player, int metal) {
        try {
            playerMetal.put(player, metal);
            new MetalGUI(player);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static int getPlayer(Player player) {
        try {
            if (playerMetal.containsKey(player)) {
                return playerMetal.get(player);
            } else {
                return -1;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return -1;
        }
    }

    public static void clearAll() {
        try {
            ArrayList<Player> temp_set = new ArrayList<>(playerMetal.keySet());
            for (Player player : temp_set) {
                playerMetal.remove(player);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}