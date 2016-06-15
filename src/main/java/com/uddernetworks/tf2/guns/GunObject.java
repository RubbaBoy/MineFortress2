package com.uddernetworks.tf2.guns;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GunObject {

    private String name;
    private String lore;
    private Material item;
    private Sound sound;
    private double damage;

    ItemStack gun;

    public GunObject(String name, String lore, Material item, Sound sound, double damage) {
        this.name = name;
        this.lore = lore;
        this.item = item;
        this.sound = sound;
        this.damage = damage;
    }

    public void registerGun() {
        gun = new ItemStack(Material.GRASS);
    }

    public static void giveGun(Player player) {

    }

}
