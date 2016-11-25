package com.uddernetworks.tf2.exception;

import com.uddernetworks.tf2.inv.Anvil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExceptionReporter {

    static long last_reported = 0;

    public ExceptionReporter(Throwable e) {
        if (last_reported == 0) {
            last_reported = System.currentTimeMillis();
            Anvil anvil = new Anvil();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    anvil.openAnvilInventory(player, e);
                    return;
                }
            }
        } else if (last_reported + 300000 <= System.currentTimeMillis()) {
            last_reported = System.currentTimeMillis();
            Anvil anvil = new Anvil();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    anvil.openAnvilInventory(player, e);
                    return;
                }
            }
        }
    }
}