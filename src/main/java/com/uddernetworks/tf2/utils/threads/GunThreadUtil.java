package com.uddernetworks.tf2.utils.threads;

import com.uddernetworks.tf2.guns.Bullet;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.HashMap3;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GunThreadUtil extends Thread {

    private boolean stahp = false;

    public static HashMap<String, Long> clickPlayers = new HashMap<>();

    private PlayerGuns playerGuns = new PlayerGuns();

    Main main;

    public static HashMap3<Player, GunObject, Long> shot = new HashMap3<>();

    public GunThreadUtil(Main main) {
        super();
        this.main = main;
        this.start();
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
                                        if (shot.get(player).getClassType() == ClassEnum.MEDIC && shot.get(player).getPower() <= 0) {
                                            Particles.spawnHealthParticles(Bukkit.getPlayer("CrustyDolphin").getLocation(), player.getLocation());
                                        } else {
                                            new Bullet(player, shot.get(player));
                                        }
                                        playerGuns.setClip(player, playerGuns.getClip(player) - 1);
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