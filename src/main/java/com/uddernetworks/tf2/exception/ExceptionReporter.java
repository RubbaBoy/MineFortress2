package com.uddernetworks.tf2.exception;

import com.uddernetworks.tf2.inv.Anvil;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.AnvilChooser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExceptionReporter {

    static long last_reported = 0;
    Main main = Main.getPlugin();

    public ExceptionReporter(Throwable e) {
        System.out.println("An error has occurred, trying to report it...");
        if (last_reported == 0) {
            last_reported = System.currentTimeMillis();
            if (main.silentError()) {
                Player player = null;
                for (Player player_ : Bukkit.getOnlinePlayers()) {
                    if (player_.isOp()) {
                        player = player_;
                    }
                }

                AnvilChooser.setThrow(player, e);
                if (player != null) {
                    new AnvilChooser(player, "");
                }
            } else {
                Anvil anvil = new Anvil();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        anvil.openAnvilInventory(player, e);
                        return;
                    }
                }
            }
        } else if (last_reported + 300000 <= System.currentTimeMillis()) {
            last_reported = System.currentTimeMillis();
            if (main.silentError()) {
                Player player = null;
                for (Player player_ : Bukkit.getOnlinePlayers()) {
                    if (player_.isOp()) {
                        player = player_;
                    }
                }

                if (player != null) {
                    new AnvilChooser(player, "");
                }
            } else {
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
}