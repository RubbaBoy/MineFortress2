package com.uddernetworks.tf2.guns;

import org.bukkit.entity.Player;

public class GunPersonalized {

    private Player player;
    private GunObject gun;
    private int ammo = 0;
    private int clip = 0;

    public GunPersonalized(Player player, GunObject gun) {
        this.player = player;
        this.gun = gun;
    }

    public int getClip() {
        return clip;
    }

    public void setClip(int clip) {
        this.clip = clip;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GunObject getGun() {
        return gun;
    }

    public void setGun(GunObject gun) {
        this.gun = gun;
    }

}
