package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.gui.MetalGUI;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerMetal {

    private static HashMap<Player, Integer> playerMetal = new HashMap<>();

    public static void addPlayer(Player player, int metal) {
        playerMetal.put(player, metal);
        new MetalGUI(player);
    }

    public static int getPlayer(Player player) {
        if (playerMetal.containsKey(player)) {
            return playerMetal.get(player);
        } else {
            return -1;
        }
    }

}