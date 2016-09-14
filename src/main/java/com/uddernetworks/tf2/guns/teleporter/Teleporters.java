package com.uddernetworks.tf2.guns.teleporter;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Teleporters {

    static int ent_size = 0;
    static int ext_size = 0;

    static HashSet<TeleporterEntrance> deleted_ent = new HashSet<>();
    static ArrayList<TeleporterEntrance> teleporters_ent = new ArrayList<>();
//    static HashMap<Integer, TeleporterEntrance> teleporter_ent_ids = new HashMap<>();

    static HashSet<TeleporterExit> deleted_ext = new HashSet<>();
    static ArrayList<TeleporterExit> teleporters_ext = new ArrayList<>();
//    static HashMap<Integer, TeleporterExit> teleporter_ext_ids = new HashMap<>();

    public static void addTeleporterEntrance(TeleporterEntrance teleporterEntrance) {
        teleporters_ent.add(teleporterEntrance);
        ent_size += 1;
    }

    public static void removeTeleporterEntrance(TeleporterEntrance teleporterEntrance) {
        teleporters_ent.remove(teleporterEntrance);
    }

//    public static int getTeleporterEntranceId(TeleporterEntrance teleporterEntrance) {
//        for (TeleporterEntrance teleporterEntrance1 : teleporters_ent) {
//            if (teleporterEntrance1 == teleporterEntrance) {
//                return teleporterEntrance1;
//            }
//        }
//        return -1;
//    }

    public static void addTeleporterExit(TeleporterExit teleporterExit) {
        teleporters_ext.add(teleporterExit);
    }

    public static void removeTeleporterExit(TeleporterExit teleporterExit) {
        teleporters_ext.remove(teleporterExit);
    }

//    public static int getTeleporterExitId(TeleporterExit teleporterExit) {
//        for (TeleporterExit teleporterExit1 : teleporters_ext) {
//            if (teleporterExit1 == teleporterExit) {
//                return id;
//            }
//        }
//        return -1;
//    }

    public static boolean hasEntrance(Player player) {
        for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
            if (teleporterEntrance.getPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasExit(Player player) {
        for (TeleporterExit teleporterExit : teleporters_ext) {
            if (teleporterExit.getPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasCounterpart(TeleporterEntrance teleporterEntrance) {
        return hasExit(teleporterEntrance.getPlayer());
    }

    public static boolean hasCounterpart(TeleporterExit teleporterExit) {
        return hasEntrance(teleporterExit.getPlayer());
    }

    public static TeleporterExit getCounterpart(TeleporterEntrance teleporterEntrance) {
        for (TeleporterExit teleporterExit : teleporters_ext) {
            if (teleporterExit.getPlayer() == teleporterEntrance.getPlayer()) {
                return teleporterExit;
            }
        }
        return null;
    }

    public static TeleporterEntrance getCounterpart(TeleporterExit teleporterExit) {
        for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
            if (teleporterEntrance.getPlayer() == teleporterExit.getPlayer()) {
                return teleporterEntrance;
            }
        }
        return null;
    }

    public static boolean isEntrance(Location location) {
        for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
            if (teleporterEntrance.getLocation().getBlock().equals(location.getBlock())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExit(Location location) {
        for (TeleporterExit teleporterExit : teleporters_ext) {
            if (teleporterExit.getLocation().getBlock().equals(location.getBlock())) {
                return true;
            }
        }
        return false;
    }

    public static TeleporterEntrance getEntrance(Location location) {
        for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
            if (teleporterEntrance.getLocation().getBlock().equals(location.getBlock())) {
                return teleporterEntrance;
            }
        }
        return null;
    }

    public static TeleporterExit getExit(Location location) {
        for (TeleporterExit teleporterExit : teleporters_ext) {
            if (teleporterExit.getLocation().getBlock().equals(location.getBlock())) {
                return teleporterExit;
            }
        }
        return null;
    }

    public static void removeAll() {
        teleporters_ent.forEach(TeleporterEntrance::remove);
        teleporters_ext.forEach(TeleporterExit::remove);
    }

    public static void removeEntranceBy(Player player) {
        teleporters_ent.stream().filter(teleporterEntrance -> teleporterEntrance.getPlayer() == player).forEach(TeleporterEntrance::remove);
    }

    public static void removeExitBy(Player player) {
        teleporters_ext.stream().filter(teleporterExit-> teleporterExit.getPlayer() == player).forEach(TeleporterExit::remove);
    }

}
