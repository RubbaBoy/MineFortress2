package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.guns.Bullet;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.dispenser.Dispenser;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.sentry.Sentry;
import com.uddernetworks.tf2.guns.teleporter.TeleporterEntrance;
import com.uddernetworks.tf2.guns.teleporter.TeleporterExit;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HitByBulletEvent extends Event {

    Object damaged;
    GunObject gun;
    Sentry sentry;

    public HitByBulletEvent(Object object, GunObject gun) {
        this.damaged = object;
        this.gun = gun;
    }

    public HitByBulletEvent(Object object, Sentry sentry) {
        damaged = object;
        this.sentry = sentry;
    }

    public boolean isEntity() {
        return damaged instanceof Entity && !(damaged instanceof ArmorStand);
    }

    public boolean isSentry() {
        return damaged instanceof ArmorStand && Sentries.isObjectSentry((ArmorStand) damaged);
    }

    public boolean isDispenser() {
        return damaged instanceof Location && Dispensers.isObjectDispenser(((Location) damaged).getBlock());
    }

    public boolean isTeleporterEntrance() {
        return damaged instanceof Location && Teleporters.isEntrance(((Location) damaged));
    }

    public boolean isTeleporterExit() {
        return damaged instanceof Location && Teleporters.isExit(((Location) damaged));
    }

    public Sentry getDamagingSentry() {
        return sentry;
    }

    public Entity getEntity() {
        if (isEntity()) {
            return (Entity) damaged;
        } else {
            return null;
        }
    }

    public Sentry getSentry() {
        if (isSentry()) {
            return Sentries.getSentry((ArmorStand) damaged);
        } else {
            return null;
        }
    }

    public Dispenser getDispenser() {
        if (isDispenser()) {
            return Dispensers.getDispenser(((Location) damaged).getBlock());
        } else {
            return null;
        }
    }

    public TeleporterEntrance getTeleporterEntrance() {
        if (isTeleporterEntrance()) {
            return Teleporters.getEntrance((Location) damaged);
        } else {
            return null;
        }
    }

    public TeleporterExit getTeleporterExit() {
        if (isTeleporterExit()) {
            return Teleporters.getExit((Location) damaged);
        } else {
            return null;
        }
    }

    public GunObject getGun() {
        return gun;
    }

    public boolean usesGun() {
        return !(gun == null && sentry != null);
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}