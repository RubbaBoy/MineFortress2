package com.uddernetworks.tf2.guns;

import java.util.ArrayList;
import java.util.HashMap;

public class GunList {

    static HashMap<String, GunObject> guns = new HashMap<String, GunObject>();

    public static void registerGun(GunObject gun) {
        guns.put(gun.getName(), gun);
    }

    public static void unregisterGun(GunObject gun) {
        if (guns.containsKey(gun.getName())) {
            guns.remove(gun);
        }
    }

    public static HashMap<String, GunObject> getGunlist() {
        return guns;
    }

}
