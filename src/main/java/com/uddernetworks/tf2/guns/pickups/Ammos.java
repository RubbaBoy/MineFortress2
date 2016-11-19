package com.uddernetworks.tf2.guns.pickups;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.Location;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.HashSet;

public class Ammos {

    static private HashSet<Ammo> ammos = new HashSet<>();

    public static void addAmmo(Ammo ammo) {
        try {
            ammos.add(ammo);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeAmmo(Ammo ammo) {
        try {
            if (ammos.contains(ammo)) {
                ammos.remove(ammo);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeAll() {
        try {
            HashSet<Ammo> temp = new HashSet<>(ammos);
            temp.forEach(Ammo::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static boolean exists(Ammo ammo) {
        try {
            return ammos.contains(ammo);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static Ammo getItem(Location location) {
        try {
            for (Ammo ammo : ammos) {
                if (ammo.getLocation().distance(location) < 0.5) {
                    return ammo;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static Ammo getItem(Item item) {
        try {
            for (Ammo ammo : ammos) {
                if (ammo.getItem().getEntityId() == item.getEntityId()) {
                    return ammo;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

}