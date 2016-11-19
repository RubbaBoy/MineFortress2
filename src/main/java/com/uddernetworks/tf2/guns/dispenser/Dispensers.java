package com.uddernetworks.tf2.guns.dispenser;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Dispensers {

    static int size = 0;

    static ArrayList<Dispenser> dispensers = new ArrayList<>();

    static HashMap<Integer, Dispenser> dispenser_ids = new HashMap<>();

    public static void addDispenser(Dispenser dispenser) {
        try {
            dispensers.add(dispenser);
            dispenser_ids.put(size, dispenser);
            size += 1;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeDispenser(Dispenser dispenser) {
        try {
            dispensers.remove(getDipenserId(dispenser));
            dispenser_ids.remove(getDipenserId(dispenser));
            size -= 1;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static int getDipenserId(Dispenser dispenser) {
        try {
            for (int id : dispenser_ids.keySet()) {
                if (dispenser_ids.get(id) == dispenser) {
                    return id;
                }
            }
            return -1;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return -1;
        }
    }

    public static Dispenser getDispenser(int id) {
        try {
            if (dispenser_ids.keySet().contains(id)) {
                return dispensers.get(id);
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static boolean isObjectDispenser(Block block) {
        try {
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
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static Dispenser getDispenser(Block block) {
        try {
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
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static boolean hasDispenser(Player player) {
        try {
            for (Dispenser dispenser : dispenser_ids.values()) {
                if (dispenser.getPlayer() == player) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static boolean isInRange(Player player) {
        try {
            for (Dispenser dispenser : dispenser_ids.values()) {
                if (dispenser.getLocation().distance(player.getLocation()) <= 2.5) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static Dispenser getNearestDispenser(Player player) {
        try {
            if (isInRange(player)) {
                for (Dispenser dispenser : dispenser_ids.values()) {
                    if (dispenser.getLocation().distance(player.getLocation()) <= 2.5) {
                        return dispenser;
                    }
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static void removeDispenserBy(Player player) {
        try {
            ArrayList<Dispenser> temp = new ArrayList<>(dispensers);
            temp.stream().filter(dispenser -> dispenser.getPlayer() == player).forEach(Dispenser::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeAll() {
        try {
            ArrayList<Dispenser> temp = new ArrayList<>(dispensers);
            for (Dispenser dispenser : temp) {
                dispenser.remove();
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}
