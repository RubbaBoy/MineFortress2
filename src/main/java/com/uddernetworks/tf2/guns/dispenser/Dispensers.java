package com.uddernetworks.tf2.guns.dispenser;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Dispensers {

    static int size = 0;

    static ArrayList<Dispenser> dispensers = new ArrayList<>();

    static HashMap<Integer, Dispenser> dispenser_ids = new HashMap<>();

    public static void addDispenser(Dispenser dispenser) {
        dispensers.add(dispenser);
        dispenser_ids.put(size, dispenser);
        size += 1;
    }

    public static void removeDispenser(int id) {
        dispensers.remove(id);
        dispenser_ids.remove(id);
        size -= 1;
    }

    public static int getDipenserId(Dispenser dispenser) {
        for (int id : dispenser_ids.keySet()) {
            if (dispenser_ids.get(id) == dispenser) {
                return id;
            }
        }
        return -1;
    }

    public static Dispenser getDispenser(int id) {
        if (dispenser_ids.keySet().contains(id)) {
            return dispensers.get(id);
        } else {
            return null;
        }
    }

    public static boolean isObjectDispenser(Block block) {
        for (Dispenser dispenser : dispenser_ids.values()) {
            if (dispenser.getLocation().getBlock().getLocation().clone().equals(block.getLocation())) {
                return true;
            } else if (dispenser.getLocation().clone().add(0, 1, 0).getBlock().getLocation().equals(block.getLocation())) {
                return true;
            } else if (dispenser.getLocation().clone().add(0, 2, 0).getBlock().getLocation().equals(block.getLocation())) {
                if (dispenser.getLevel() == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Dispenser getDispenser(Block block) {
        for (Dispenser dispenser : dispenser_ids.values()) {
            if (dispenser.getLocation().clone().getBlock().equals(block)) {
                return dispenser;
            } else if (dispenser.getLocation().clone().add(0, 1, 0).getBlock().equals(block)) {
                return dispenser;
            } else if (dispenser.getLocation().clone().add(0, 2, 0).getBlock().equals(block)) {
                if (dispenser.getLevel() == 3) {
                    return dispenser;
                }
            }
        }
        return null;
    }

    public static boolean hasDispenser(Player player) {
        for (Dispenser dispenser : dispenser_ids.values()) {
            if (dispenser.getPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInRange(Player player) {
        for (Dispenser dispenser : dispenser_ids.values()) {
            if (dispenser.getLocation().distance(player.getLocation()) <= 2.5) {
                return true;
            }
        }
        return false;
    }

    public static Dispenser getNearestDispenser(Player player) {
        if (isInRange(player)) {
            for (Dispenser dispenser : dispenser_ids.values()) {
                if (dispenser.getLocation().distance(player.getLocation()) <= 2.5) {
                    return dispenser;
                }
            }
        }
        return null;
    }

    public static void removeBy(Player player) {
        dispenser_ids.values().stream().filter(dispenser -> dispenser.getPlayer() == player).forEach(Dispenser::remove);
    }

    public static void removeAll() {
        dispenser_ids.values().forEach(Dispenser::remove);
    }

}
