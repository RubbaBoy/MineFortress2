package com.uddernetworks.tf2.utils.threads;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.guns.Bullet;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.guns.custom.Controller;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.HashMap3;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GunThreadUtil {

    private boolean stahp = false;

    public static HashMap<String, Long> clickPlayers = new HashMap<>();

    private PlayerGuns playerGuns = new PlayerGuns();
    PlayerHealth playerHealth = new PlayerHealth();

    private static Player player = null;

    private Game game;

    Main main;

    public static HashMap3<Player, GunObject, Long> shot = new HashMap3<>();

    public GunThreadUtil(Main main) {
        super();
        try {
            this.main = main;
            game = new Game(main);

            java.util.Timer jTimer = new java.util.Timer();
            Timer timer = new Timer(jTimer);
            jTimer.schedule(timer, 0, 10);

        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void stahp() {
        stahp = true;
    }

    public boolean isRunning() {
        return !stahp;
    }

    private class Timer extends TimerTask {

        java.util.Timer timer;

        public Timer(java.util.Timer timer) {
            this.timer = timer;
        }

        private void doThings() {
            try {
                if (!stahp) {
                    HashMap<String, Long> clickPlayers_temp = new HashMap<>(clickPlayers);
                    for (String player_str : clickPlayers_temp.keySet()) {
                        Player player = Bukkit.getPlayer(player_str);
                        if (System.currentTimeMillis() - clickPlayers_temp.get(player_str) > 260) {
                            clickPlayers.remove(player_str);
                            shot.remove(player);
                        } else {
                            if (shot.getT(player) <= System.currentTimeMillis()) {
                                shot.setT(player, System.currentTimeMillis() + shot.get(player).getCooldown());
                                Bukkit.getScheduler().runTask(main, new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (shot.containsKey(player) && shot.get(player) != null) {
                                            if (playerGuns.getClip(player) > 0) {
                                                if (shot.get(player).getCustom() != null) {
                                                    Controller controller = new Controller(shot.get(player), player, true);
                                                    try {
                                                        if (Controller.canProceed(player)) {
                                                            controller.run();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    if (shot.get(player).isShotgun()) {
                                                        for (int i = 0; i < shot.get(player).getShotgun_bullet(); i++) {
                                                            new Bullet(player, shot.get(player));
                                                        }
                                                    } else {
                                                        new Bullet(player, shot.get(player));
                                                    }
                                                    playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                                                    Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), () -> {
                                                        if (shot.get(player).getSound() != null) {
                                                            player.playSound(player.getLocation(), shot.get(player).getSound(), 1, 1);
                                                        }
                                                        return null;
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else {
                    timer.cancel();
                }
            } catch (Throwable throwable) {
                new ExceptionReporter(throwable);
            }
        }

        @Override
        public void run() {
            doThings();
        }
    }
}