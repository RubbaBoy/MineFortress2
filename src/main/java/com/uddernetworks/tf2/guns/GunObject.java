package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.utils.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GunObject {

    private WeaponType type;
    private String name = null;
    private String lore = null;
    private Material item = null;
    private Sound sound = null;
    private double power = 0;
    private double damage = 0;
    private int KZR;
    private boolean scopeable = false;
    private boolean NVscope = false;
    private int clip;
    private int ammo;
    private int maxClip;
    private int maxAmmo;
    private int cooldown;
    private int cooldown_reload;

    private Player player = Bukkit.getPlayer("RubbaBoy");

    ItemStack gun;

    public GunObject(WeaponType type, String name, String lore, Material item, Sound sound, double power, double damage, int KZR, boolean scopeable, boolean NVscope, int clip, int ammo, int maxClip, int maxAmmo, int cooldown, int cooldown_reload) {
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.item = item;
        this.sound = sound;
        this.power = power;
        this.damage = damage;
        this.KZR = KZR;
        this.scopeable = scopeable;
        this.NVscope = NVscope;
        this.clip = clip;
        this.ammo = ammo;
        this.maxClip = maxClip;
        this.maxAmmo = maxAmmo;
        this.cooldown = cooldown;
        this.cooldown_reload = cooldown_reload;

    }

    public WeaponType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public double getDamage() {
        return damage;
    }

    public double getKZR() {
        return KZR;
    }

    public double getPower() {
        return power;
    }

    public boolean getScopeable() {
        return scopeable;
    }

    public boolean getNVscope() {
        return NVscope;
    }

    public Sound getSound() {
        return sound;
    }

    public int getClip() {
        return clip;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxClip() {
        return maxClip;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCooldownReload(){
        return cooldown_reload;
    }

    public void setClip(int clip) {
        this.clip = clip;

        new BulletGUI(player, this);

    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;

        new BulletGUI(player, this);
    }

    public void reloadGun() {
        if (getAmmo() - getClip() > 0) {
            if (getClip() <= getAmmo()) {
                setAmmo(getAmmo() - (getMaxClip() - getClip()));
                setClip(getMaxClip() - getClip());
            } else {
                setClip(getAmmo());
                setAmmo(0);
            }
            new BulletGUI(player, this);
        }
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

    public void giveGun(Player player, int slot) {
        ItemStack itemgun = new ItemStack(item);
        ItemMeta meta = itemgun.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);
        itemgun.setItemMeta(meta);
        player.getInventory().setItem(slot, itemgun);


        new BulletGUI(player, this);
    }

}
