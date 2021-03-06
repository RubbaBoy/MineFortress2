package com.uddernetworks.tf2.guns.sentry;

import com.uddernetworks.tf2.exception.ExceptionReporter;
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
        try {
            sentries.add(sentry);
            sentry_id.put(size, sentry);
            size += 1;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeSentry(Sentry sentry) {
        try {
            sentries.remove(sentry);
            sentry_id.remove(getSentryId(sentry));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void removeSentryBy(Player player) {
        try {
            ArrayList<Sentry> temp = new ArrayList<>(sentries);
            temp.stream().filter(sentry -> sentry.getPlayer() == player).forEach(Sentry::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static int getSentryId(Sentry sentry) {
        try {
            for (int i = 0; i < sentry_id.size(); i++) {
                if (sentry_id.get(i) == sentry) {
                    return i;
                } else {
                }
            }
//            for (int id : sentry_id.keySet()) {
//                if (sentry_id.get(id) == sentry) {
//                    return id;
//                }
//            }
            return -1;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return -1;
        }
    }

    public static Sentry getSentry(int id) {
        try {
            if (sentry_id.keySet().contains(id)) {
                return sentries.get(id);
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static Sentry getSentry(ArmorStand armorStand) {
        try {
            for (Sentry sentry : sentry_id.values()) {
                if (sentry.getObj() == armorStand) {
                    return sentry;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static Sentry getSentry(Player player) {
        try {
            for (Sentry sentry : sentry_id.values()) {
                if (sentry.getPlayer() == player) {
                    return sentry;
                }
            }
            return null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static boolean isObjectSentry(ArmorStand object) {
        try {
            for (Sentry sentry : sentry_id.values()) {
                if (sentry.getObj() == object) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static boolean hasSentry(Player player) {
        try {
            for (Sentry sentry : sentry_id.values()) {
                if (sentry.getPlayer() == player) {
                    return true;
                }
            }
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static void removeAll() {
        try {
            ArrayList<Sentry> temp = new ArrayList<>(sentries);
            temp.forEach(Sentry::remove);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}
