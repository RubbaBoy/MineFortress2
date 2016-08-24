package com.uddernetworks.tf2.utils.threads;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.guns.Bullet;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.HashMap3;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GunThreadUtil extends Thread {

    private boolean stahp = false;

    public static HashMap<String, Long> clickPlayers = new HashMap<>();

    private boolean nope = true;

    private PlayerGuns playerGuns = new PlayerGuns();
    PlayerHealth playerHealth = new PlayerHealth();

    private static Player player = null;

    private Game game;

    Main main;

    public static HashMap3<Player, GunObject, Long> shot = new HashMap3<>();

    public GunThreadUtil(Main main) {
        super();
        this.main = main;
        this.start();
        game = new Game(main);
    }

    public void stahp() {
        stahp = true;
    }

    public void run() {
        doThings();
    }

    private void doThings() {
        try {
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate((Runnable) () -> {
                if (!stahp) {

                    for (String player_str : clickPlayers.keySet()) {
                        Player player = Bukkit.getPlayer(player_str);
                        if (System.currentTimeMillis() - clickPlayers.get(player_str) > 260) {
                            clickPlayers.remove(player_str);
                            shot.remove(player);
                        } else {

                            if (shot.getT(player) <= System.currentTimeMillis()) {
                                shot.setT(player, System.currentTimeMillis() + shot.get(player).getCooldown());
                                Bukkit.getScheduler().runTask(main, new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        //                                            Particles.spawnHealthParticles(Bukkit.getPlayer("CrustyDolphin").getLocation(), player.getLocation());
                                        if (shot.get(player).getClassType() == ClassEnum.MEDIC && shot.get(player).getPower() <= 0) {
                                            game.getWorld().getEntities().stream().filter(entity -> player.hasLineOfSight(entity) && entity.getLocation().distance(player.getLocation()) <= 5 && entity instanceof Player).forEach(entity -> {
                                                if (GunThreadUtil.player == null) {
                                                    GunThreadUtil.player = (Player) entity;
                                                } else if (entity.getLocation().distance(player.getLocation()) < GunThreadUtil.player.getLocation().distance(player.getLocation())) {
                                                    GunThreadUtil.player = (Player) entity;
                                                }
                                            });
                                            Particles.spawnHealthParticles(GunThreadUtil.player.getLocation().add(0, GunThreadUtil.player.getEyeHeight()/2, 0), player.getLocation().add(0, player.getEyeHeight()/2, 0), PlayerTeams.getPlayer(GunThreadUtil.player));
                                            if (playerHealth.getHealth(GunThreadUtil.player) + 6 > playerHealth.getMaxHealth(GunThreadUtil.player)) {
                                                playerHealth.addHealth(GunThreadUtil.player, playerHealth.getMaxHealth(GunThreadUtil.player));
                                            } else {
                                                playerHealth.addHealth(GunThreadUtil.player, playerHealth.getHealth(GunThreadUtil.player) + 6);
                                            }
                                            playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                                        } else {
                                            if (shot.get(player).isShotgun()) {
                                                for (int i = 0; i < shot.get(player).getShotgun_bullet(); i++) {
                                                    new Bullet(player, shot.get(player));
                                                }
                                            } else {
                                                new Bullet(player, shot.get(player));
                                            }
                                            nope = false;
                                        }
                                        if (!nope) {
                                            playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                                        }
                                        nope = true;
                                    }
                                });
                            }

                        }
                    }
                } else {
                    exec.shutdown();
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**/