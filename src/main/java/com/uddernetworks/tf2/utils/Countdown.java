package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown {

    int count = 0;

    public void Countdown(final Player player, final int seconds) throws Exception {
        try {
            new BukkitRunnable() {
                public void run() {
                    if (count < seconds + 1) {
                        if (seconds - count == 0) {
                            player.setLevel(0);
                        } else {
                            player.setLevel(seconds - count);
                        }
                        count++;
                    } else {
                        player.setLevel(0);
                        count = 0;
                        this.cancel();
                    }
                }
            }.runTaskTimer(Main.getPlugin(), 20, 20);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}
