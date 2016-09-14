package com.uddernetworks.tf2.guns.sentry;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Sentries {

    static int size = 0;

    static ArrayList<Sentry> sentries = new ArrayList<>();

    static HashMap<Integer, Sentry> sentry_id = new HashMap<>();

    public static void addSentry(Sentry sentry) {
        sentries.add(sentry);
        sentry_id.put(size, sentry);
        size += 1;
    }

    public static void removeSentry(int id) {
        sentries.remove(id);
        sentry_id.remove(id);
    }

    public static int getSentryId(Sentry sentry) {
        for (int id : sentry_id.keySet()) {
            if (sentry_id.get(id) == sentry) {
                return id;
            }
        }
        return -1;
    }

    public static Sentry getSentry(int id) {
        if (sentry_id.keySet().contains(id)) {
            return sentries.get(id);
        } else {
            return null;
        }
    }

    public static Sentry getSentry(ArmorStand armorStand) {
        for (Sentry sentry : sentry_id.values()) {
            if (sentry.getObj() == armorStand) {
                return sentry;
            }
        }
        return null;
    }

    public static Sentry getSentry(Player player) {
        for (Sentry sentry : sentry_id.values()) {
            if (sentry.getPlayer() == player) {
                return sentry;
            }
        }
        return null;
    }

    public static boolean isObjectSentry(ArmorStand object) {
        for (Sentry sentry : sentry_id.values()) {
            if (sentry.getObj() == object) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSentry(Player player) {
        for (Sentry sentry : sentry_id.values()) {
            if (sentry.getPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static void removeAll() {
        sentry_id.values().forEach(Sentry::remove);
    }

}
