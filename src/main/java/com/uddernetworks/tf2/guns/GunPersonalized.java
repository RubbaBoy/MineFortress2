package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
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
        try {
            return clip;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public void setClip(int clip) {
        try {
            this.clip = clip;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public int getAmmo() {
        try {
            return ammo;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public void setAmmo(int ammo) {
        try {
            this.ammo = ammo;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public Player getPlayer() {
        try {
            return player;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public void setPlayer(Player player) {
        try {
            this.player = player;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public GunObject getGun() {
        try {
            return gun;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public void setGun(GunObject gun) {
        try {
            this.gun = gun;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}
