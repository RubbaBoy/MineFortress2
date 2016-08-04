package com.uddernetworks.tf2.utils.threads;

import com.uddernetworks.tf2.guns.Bullet;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.guns.sentry.Sentry;
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

public class SentryThreadUtil extends Thread {

    private boolean stahp = false;

    public static HashMap3<Sentry, Boolean, Long> sentries = new HashMap3<>();

    Main main;

    public SentryThreadUtil(Main main) {
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

                    for (Sentry sentry : sentries.keySet()) {
                        if (sentries.get(sentry)) {
                            if (sentries.getT(sentry) <= System.currentTimeMillis()) {
                                sentries.setT(sentry, System.currentTimeMillis() + sentry.getCooldown());
                                Bukkit.getScheduler().runTask(main, new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        new Bullet(sentry);
//                                        Bukkit.getPlayer("RubbaBoy").sendMessage("Spawned bullet");
                                    }
                                });
                            } else {
//                                Bukkit.getPlayer("RubbaBoy").sendMessage(sentries.getT(sentry) + " ISNT <= " + System.currentTimeMillis());
                            }
                        }
                    }
                } else {
                    exec.shutdown();
                }
            }, 0, 10, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}