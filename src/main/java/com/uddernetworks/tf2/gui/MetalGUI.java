package com.uddernetworks.tf2.gui;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.guns.Clip;
import com.uddernetworks.tf2.guns.Metal;
import com.uddernetworks.tf2.guns.PlayerMetal;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MetalGUI {

    public MetalGUI(Player player) {
        int metal = PlayerMetal.getPlayer(player);

        Material material;
        if (PlayerTeams.getPlayer(player) == TeamEnum.RED) {
            material = Material.WOOD_SPADE;
        } else {
            material = Material.WOOD_PICKAXE;
        }

        switch (metal) {
            case 0:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_0.getValue()));
                break;
            case 25:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_25.getValue()));
                break;
            case 50:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_50.getValue()));
                break;
            case 75:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_75.getValue()));
                break;
            case 100:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_100.getValue()));
                break;
            case 125:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_125.getValue()));
                break;
            case 150:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_150.getValue()));
                break;
            case 175:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_175.getValue()));
                break;
            case 200:
                player.getInventory().setItem(1, new ItemStack(material, 1, Metal.Metal_200.getValue()));
                break;
            default:
                player.getInventory().setItem(1, new ItemStack(Material.AIR, 1));
                break;
        }

    }

}