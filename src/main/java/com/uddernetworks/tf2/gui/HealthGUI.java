package com.uddernetworks.tf2.gui;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HealthGUI {

    public HealthGUI(Player player) {
        try {
            PlayerHealth health = new PlayerHealth();
            int percentage = Math.toIntExact(Math.round(health.getHealth(player) / ((double) (PlayerClasses.getPlayerClass(player).getHealth()) / (double) 100)));
            if (percentage < 0 || percentage > 100) return;
            ItemStack health_item = new ItemStack(Material.DIAMOND_HOE, 1, (short) (percentage + 1));
            health_item = setUnbreakable(health_item.clone());
            player.getInventory().setItemInOffHand(health_item);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    private ItemStack setUnbreakable(ItemStack item) {
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
