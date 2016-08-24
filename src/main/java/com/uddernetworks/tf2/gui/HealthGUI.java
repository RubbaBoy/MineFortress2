package com.uddernetworks.tf2.gui;

import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HealthGUI {

    public HealthGUI(Player player) {
        PlayerHealth health = new PlayerHealth();
        int percentage = Math.round((int) health.getHealth(player) / (PlayerClasses.getPlayerClass(player).getHealth() / 100));
        if (percentage < 0 || percentage > 100) return;
        player.getInventory().setItemInOffHand(new ItemStack(Material.DIAMOND_HOE, 1, (short) (percentage + 1)));
    }

}
