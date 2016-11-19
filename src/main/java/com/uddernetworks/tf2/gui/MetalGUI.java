package com.uddernetworks.tf2.gui;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.Clip;
import com.uddernetworks.tf2.guns.Metal;
import com.uddernetworks.tf2.guns.PlayerMetal;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MetalGUI {

    public MetalGUI(Player player) {
        try {
            int metal = PlayerMetal.getPlayer(player);
            ItemStack itemStack;

            Material material;
            if (PlayerTeams.getPlayer(player) == TeamEnum.RED) {
                material = Material.WOOD_SPADE;
            } else {
                material = Material.WOOD_PICKAXE;
            }

            switch (metal) {
                case 0:
                    itemStack = new ItemStack(material, 1, Metal.Metal_0.getValue());
                    break;
                case 25:
                    itemStack = new ItemStack(material, 1, Metal.Metal_25.getValue());
                    break;
                case 50:
                    itemStack = new ItemStack(material, 1, Metal.Metal_50.getValue());
                    break;
                case 75:
                    itemStack = new ItemStack(material, 1, Metal.Metal_75.getValue());
                    break;
                case 100:
                    itemStack = new ItemStack(material, 1, Metal.Metal_100.getValue());
                    break;
                case 125:
                    itemStack = new ItemStack(material, 1, Metal.Metal_125.getValue());
                    break;
                case 150:
                    itemStack = new ItemStack(material, 1, Metal.Metal_150.getValue());
                    break;
                case 175:
                    itemStack = new ItemStack(material, 1, Metal.Metal_175.getValue());
                    break;
                case 200:
                    itemStack = new ItemStack(material, 1, Metal.Metal_200.getValue());
                    break;
                default:
                    itemStack = new ItemStack(Material.AIR, 1);
                    break;
            }

            if (itemStack.getType() != Material.AIR) {
                itemStack = setUnbreakable(itemStack.clone());
            }

            player.getInventory().setItem(1, itemStack);

        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public ItemStack setUnbreakable(ItemStack item) {
        ItemStack toreturn;
        net.minecraft.server.v1_10_R1.ItemStack stack = org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack.asNMSCopy(item.clone());
        net.minecraft.server.v1_10_R1.NBTTagCompound tag = new net.minecraft.server.v1_10_R1.NBTTagCompound();
        tag.setBoolean("Unbreakable", true);
        stack.setTag(tag);

        toreturn = org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack.asCraftMirror(stack);

        ItemMeta meta = toreturn.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        toreturn.setItemMeta(meta);

        return toreturn;
    }

}