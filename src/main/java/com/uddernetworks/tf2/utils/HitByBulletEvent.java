package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;
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
        try {
            return damaged instanceof Entity && !(damaged instanceof ArmorStand);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public boolean isSentry() {
        try {
            return damaged instanceof ArmorStand && Sentries.isObjectSentry((ArmorStand) damaged);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public boolean isDispenser() {
        try {
            return damaged instanceof Location && Dispensers.isObjectDispenser(((Location) damaged).getBlock());
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public boolean isTeleporterEntrance() {
        try {
            return damaged instanceof Location && Teleporters.isEntrance(((Location) damaged));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public boolean isTeleporterExit() {
        try {
            return damaged instanceof Location && Teleporters.isExit(((Location) damaged));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public Sentry getDamagingSentry() {
        try {
            return sentry;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public Entity getEntity() {
        try {
            if (isEntity()) {
                return (Entity) damaged;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public Sentry getSentry() {
        try {
            if (isSentry()) {
                return Sentries.getSentry((ArmorStand) damaged);
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public Dispenser getDispenser() {
        try {
            if (isDispenser()) {
                return Dispensers.getDispenser(((Location) damaged).getBlock());
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public TeleporterEntrance getTeleporterEntrance() {
        try {
            if (isTeleporterEntrance()) {
                return Teleporters.getEntrance((Location) damaged);
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public TeleporterExit getTeleporterExit() {
        try {
            if (isTeleporterExit()) {
                return Teleporters.getExit((Location) damaged);
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
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

    public boolean usesGun() {
        try {
            return !(gun == null && sentry != null);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}