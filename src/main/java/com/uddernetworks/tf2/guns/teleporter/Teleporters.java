package com.uddernetworks.tf2.guns.teleporter;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Teleporters {

    static int ent_size = 0;
    static int ext_size = 0;

    static ArrayList<TeleporterEntrance> teleporters_ent = new ArrayList<>();

    static ArrayList<TeleporterExit> teleporters_ext = new ArrayList<>();

    public static void addTeleporterEntrance(TeleporterEntrance teleporterEntrance) {
        try {
            teleporters_ent.add(teleporterEntrance);
            ent_size += 1;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeTeleporterEntrance(TeleporterEntrance teleporterEntrance) {
        try {
            if (teleporters_ent.contains(teleporterEntrance)) {
                teleporters_ent.remove(teleporterEntrance);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void addTeleporterExit(TeleporterExit teleporterExit) {
        try {
            teleporters_ext.add(teleporterExit);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeTeleporterExit(TeleporterExit teleporterExit) {
        try {
            if (teleporters_ext.contains(teleporterExit)) {
                teleporters_ext.remove(teleporterExit);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static boolean hasEntrance(Player player) {
        try {
            for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
                if (teleporterEntrance.getPlayer() == player) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static boolean hasExit(Player player) {
        try {
            for (TeleporterExit teleporterExit : teleporters_ext) {
                if (teleporterExit.getPlayer() == player) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static boolean hasCounterpart(TeleporterEntrance teleporterEntrance) {
        try {
            return hasExit(teleporterEntrance.getPlayer());
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static boolean hasCounterpart(TeleporterExit teleporterExit) {
        try {
            return hasEntrance(teleporterExit.getPlayer());
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static TeleporterExit getCounterpart(TeleporterEntrance teleporterEntrance) {
        try {
            for (TeleporterExit teleporterExit : teleporters_ext) {
                if (teleporterExit.getPlayer() == teleporterEntrance.getPlayer()) {
                    return teleporterExit;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static TeleporterEntrance getCounterpart(TeleporterExit teleporterExit) {
        try {
            for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
                if (teleporterEntrance.getPlayer() == teleporterExit.getPlayer()) {
                    return teleporterEntrance;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static boolean isEntrance(Location location) {
        try {
            for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
                if (teleporterEntrance.getLocation().getBlock().equals(location.getBlock())) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static boolean isExit(Location location) {
        try {
            for (TeleporterExit teleporterExit : teleporters_ext) {
                if (teleporterExit.getLocation().getBlock().equals(location.getBlock())) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static TeleporterEntrance getEntrance(Location location) {
        try {
            for (TeleporterEntrance teleporterEntrance : teleporters_ent) {
                if (teleporterEntrance.getLocation().getBlock().equals(location.getBlock())) {
                    return teleporterEntrance;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static TeleporterExit getExit(Location location) {
        try {
            for (TeleporterExit teleporterExit : teleporters_ext) {
                if (teleporterExit.getLocation().getBlock().equals(location.getBlock())) {
                    return teleporterExit;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static void removeAll() {
        try {
            teleporters_ent.forEach(TeleporterEntrance::remove);
            teleporters_ext.forEach(TeleporterExit::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeEntranceBy(Player player) {
        try {
            teleporters_ent.stream().filter(teleporterEntrance -> teleporterEntrance.getPlayer() == player).forEach(TeleporterEntrance::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeExitBy(Player player) {
        try {
            teleporters_ext.stream().filter(teleporterExit -> teleporterExit.getPlayer() == player).forEach(TeleporterExit::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}
