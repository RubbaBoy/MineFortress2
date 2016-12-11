package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.gui.HealthGUI;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.HashMap3;
import com.uddernetworks.tf2.utils.SQLLoadout;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

public class PlayerHealth {

    static HashMap3<Player, Double, Integer> health = new HashMap3<>();
    Game game = new Game(Main.getPlugin());

    public void addPlayer(Player player, int maxhealth) {
        try {
            health.setT(player, maxhealth);
            health.put(player, (double) maxhealth);

            new HealthGUI(player);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public boolean addHealth(Player player, double health) {
        try {
            if (PlayerHealth.health.containsKey(player)) {
                if (!DeadMode.isInDeadMode(player)) {
                    if (health > 0) {
                        PlayerHealth.health.put(player, health);

                        new HealthGUI(player);
                        return false;
                    } else {
                        new HealthGUI(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, true));
                        player.teleport(Main.getPlugin().getSpectateLocation(player.getWorld()));
                        addPlayer(player, PlayerClasses.getPlayerClass(player).getHealth());
                        PlayerGuns playerGuns = new PlayerGuns();
                        playerGuns.fillAll(player);
                        player.setGameMode(GameMode.ADVENTURE);
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        DeadMode.setDeadMode(player, true);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.setGameMode(GameMode.SURVIVAL);
                                addPlayer(player, PlayerClasses.getPlayerClass(player).getHealth());
                                Random random = new Random();
                                player.teleport(Main.getPlugin().getSpawnBlocks(game.getWorld(), PlayerTeams.getPlayer(player)).get(random.nextInt(Main.getPlugin().getSpawnBlocks(player.getWorld(), PlayerTeams.getPlayer(player)).size())));
                                DeadMode.setDeadMode(player, false);
                            }
                        }.runTaskLater(Main.getPlugin(), 60);
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
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

    public static void clearAll() {
        try {
            for (Player player : health.keySet()) {
                health.remove(player);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}