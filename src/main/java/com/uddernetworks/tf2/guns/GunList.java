package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.utils.ClassEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GunList {

    static int size = 0;

    static ArrayList<GunObject> guns = new ArrayList<>();

    static HashMap<Integer, GunObject> gun_ids = new HashMap<>();

    public static void registerGun(GunObject gun) {
        try {
            gun_ids.put(size, gun);
            guns.add(gun);
            size += 1;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static boolean isGunId(int id) {
        try {
            return gun_ids.containsKey(id);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static ArrayList<GunObject> getGunsOfType(ClassEnum type) {
        try {
            return gun_ids.values().stream().filter(gun -> gun.getClassType() == type).collect(Collectors.toCollection(ArrayList::new));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static void addGunItem(int id, GunObject gun) {
        try {
            gun_ids.put(id, gun);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void unregisterGun(GunObject gun) {
        try {
            if (guns.contains(gun)) {
                guns.remove(gun);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static GunObject getGunByName(String name) {
        try {
            for (GunObject gun : guns) {
                if (name.equalsIgnoreCase(gun.getName())) {
                    return gun;
                }
            }
            System.out.println("Tried to get unregistered gun by name: " + name);
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static ArrayList<GunObject> getDefaultGuns(ClassEnum classEnum) {
        try {
            return GunList.guns.stream().filter(gun -> gun.isClassDefault() && gun.getClassType() == classEnum).collect(Collectors.toCollection(ArrayList::new));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static void deleteGunItem(int id) {
        try {
            gun_ids.remove(id);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static int getIndexOf(GunObject gunObject) {
        try {
            return guns.indexOf(gunObject);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public static GunObject getGunAt(int index) {
        try {
            return guns.get(index);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static ArrayList<GunObject> getGunlist() {
        try {
            return guns;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

}
