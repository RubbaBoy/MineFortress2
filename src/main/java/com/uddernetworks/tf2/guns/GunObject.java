package com.uddernetworks.tf2.guns;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GunObject {

    private String name = null;
    private String lore = null;
    private Material item = null;
    private Sound sound = null;
    private double damage = 0;

    ItemStack gun;

    public GunObject(String name, String lore, Material item, Sound sound, double damage) {
        this.name = name;
        this.lore = lore;
        this.item = item;
        this.sound = sound;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public double getDamage() {
        return damage;
    }

    public Sound getSound() {
        return sound;
    }

    public ItemStack getItemStack() {
        ItemStack itemgun = new ItemStack(item);
        ItemMeta meta = itemgun.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);
        itemgun.setItemMeta(meta);
        return itemgun;
    }

    public void giveGun(Player player) {
        ItemStack itemgun = new ItemStack(item);
        ItemMeta meta = itemgun.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);
        itemgun.setItemMeta(meta);
        player.getInventory().setItemInMainHand(itemgun);
    }

}
