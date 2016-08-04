package com.uddernetworks.tf2.gui;

import com.uddernetworks.tf2.guns.PlayerHealth;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HealthGUI {

    static int HEALTH_NEED_CHANGE = 100;

    private PlayerHealth health = new PlayerHealth();

    public HealthGUI(Player player) {
        int percentage = (int) Math.round((health.getHealth(player) / HEALTH_NEED_CHANGE) * 100);

        player.getInventory().setItemInOffHand(new ItemStack(Material.DIAMOND_HOE, 1, (short) (percentage + 1)));
    }

}
