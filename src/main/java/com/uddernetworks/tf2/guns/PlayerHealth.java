package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.gui.HealthGUI;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.HashMap3;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PlayerHealth {

    static HashMap3<Player, Double, Integer> health = new HashMap3<>();

    public void addPlayer(Player player, int maxhealth) {
        try {
            health.setT(player, maxhealth);
            health.put(player, (double) maxhealth);

            new HealthGUI(player);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void addHealth(Player player, double health) {
        try {
            if (PlayerHealth.health.containsKey(player)) {
                if (PlayerHealth.health.get(player) - health > 0) {
                    PlayerHealth.health.put(player, health);

                    new HealthGUI(player);
                } else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, true));
                    player.setGameMode(GameMode.ADVENTURE);
                    player.setFlying(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.setGameMode(GameMode.SURVIVAL);
                            player.setFlying(false);
                            player.teleport(Main.getPlugin().getSpectateLocation(player.getWorld()));
                        }
                    }.runTaskLater(Main.getPlugin(), 60);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public int getMaxHealth(Player player) {
        try {
            if (PlayerHealth.health.containsKey(player)) {
                return PlayerHealth.health.getT(player);
            } else {
                return 0;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public double getHealth(Player player) {
        try {
            if (health.containsKey(player)) {
                return health.get(player);
            } else {
                return 0;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

}