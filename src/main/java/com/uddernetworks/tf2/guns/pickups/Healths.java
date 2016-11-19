package com.uddernetworks.tf2.guns.pickups;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.Location;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Healths {

    static private HashSet<Health> healths = new HashSet<>();

    public static void addHealth(Health health) {
        try {
            healths.add(health);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeHealth(Health health) {
        try {
            if (healths.contains(health)) {
                healths.remove(health);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeAll() {
        try {
            HashSet<Health> temp = new HashSet<>(healths);
            temp.forEach(Health::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static boolean exists(Health health) {
        try {
            return healths.contains(health);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static Health getItem(Location location) {
        try {
            for (Health health : healths) {
                if (health.getLocation().distance(location) < 0.5) {
                    return health;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static Health getItem(Item item) {
        try {
            for (Health health : healths) {
                if (health.getItem().getEntityId() == item.getEntityId()) {
                    return health;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }
}