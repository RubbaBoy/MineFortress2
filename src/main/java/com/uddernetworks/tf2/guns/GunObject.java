package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.WeaponType;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    private int maxClip;
    private int maxAmmo;
    private int cooldown;
    private int cooldown_reload;
    private boolean tracers = false;
    private boolean sniper = false;
    private int accuracy;
    private boolean shotgun = false;
    private int shotgun_bullet = 0;
    private ClassEnum classtype;
    private boolean classDefault = false;

    ItemStack gun;

    public GunObject(WeaponType type, String name, String lore, Material item, Sound sound, double power, double damage, int KZR, boolean scopeable, boolean NVscope, int maxClip, int maxAmmo, int cooldown, int cooldown_reload, boolean tracers, boolean sniper, int accuracy, boolean shotgun, int shotgun_bullet, ClassEnum classtype, boolean classDefault) {
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
        this.maxClip = maxClip;
        this.maxAmmo = maxAmmo;
        this.cooldown = cooldown;
        this.cooldown_reload = cooldown_reload;
        this.tracers = tracers;
        this.sniper = sniper;
        this.accuracy = accuracy;
        this.shotgun = shotgun;
        this.shotgun_bullet = shotgun_bullet;
        this.classtype = classtype;
        this.classDefault = classDefault;

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

    public boolean getTracers() {
        return tracers;
    }

    public boolean isSniper() {
        return sniper;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public ClassEnum getClassType() {
        return classtype;
    }

    public boolean isShotgun() {
        return shotgun;
    }

    public int getShotgun_bullet() {
        return shotgun_bullet;
    }

    public boolean isClassDefault() {
        return classDefault;
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

}
