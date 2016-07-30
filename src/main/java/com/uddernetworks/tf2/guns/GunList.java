package com.uddernetworks.tf2.guns;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GunList {

    static ArrayList<GunObject> guns = new ArrayList<>();

    static HashMap<Integer, GunObject> gun_ids = new HashMap<>();

    public static void registerGun(GunObject gun) {
        gun_ids.put(gun_ids.size(), gun);
        guns.add(gun);
    }

    public static boolean isGunId(int id) {
        return gun_ids.containsKey(id);
    }

    public static void addGunItem(int id, GunObject gun) {
        gun_ids.put(id, gun);
    }

    public static void unregisterGun(GunObject gun) {
        if (guns.contains(gun)) {
            guns.remove(gun);
        }
    }

    public static void deleteGunItem(int id) {
        gun_ids.remove(id);
    }

    public static int getIndexOf(GunObject gunObject) {
        return guns.indexOf(gunObject);
    }

    public static GunObject getGunAt(int index) {
        return guns.get(index);
    }

    public static ArrayList<GunObject> getGunlist() {
        return guns;
    }

}
