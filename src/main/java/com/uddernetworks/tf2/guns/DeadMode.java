package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class DeadMode {

    private static HashSet<Player> dead = new HashSet<>();

    public static void setDeadMode(Player player, boolean deadMode) {
        try {
            if (deadMode) {
                if (!dead.contains(player)) {
                    dead.add(player);
                }
            } else {
                if (dead.contains(player)) {
                    dead.remove(player);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static boolean isInDeadMode(Player player) {
        try {
            return dead.contains(player);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

}