package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.gui.HealthGUI;
import com.uddernetworks.tf2.utils.HashMap3;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerHealth {

    static HashMap3<Player, Double, Integer> health = new HashMap3<>();

    public void addPlayer(Player player, int maxhealth) {
        health.setT(player, maxhealth);
        health.put(player, (double) maxhealth);

        new HealthGUI(player);
    }

    public void addHealth(Player player, double health) {
        if (PlayerHealth.health.containsKey(player)) {
            PlayerHealth.health.put(player, health);

            new HealthGUI(player);
        }
    }

    public int getMaxHealth(Player player) {
        if (PlayerHealth.health.containsKey(player)) {
            return PlayerHealth.health.getT(player);
        } else {
            return 0;
        }
    }

    public double getHealth(Player player) {
        return health.get(player);
    }

}