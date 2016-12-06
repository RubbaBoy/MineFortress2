package com.uddernetworks.tf2.utils.threads;

import com.uddernetworks.tf2.exception.ExceptionReporter;
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
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SentryThreadUtil {

    private boolean stahp = false;

    public static HashMap3<Sentry, Boolean, Long> sentries = new HashMap3<>();

    Main main;

    public SentryThreadUtil(Main main) {
        super();
        try {
            this.main = main;

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
                    sentries.keySet().stream().filter(sentry -> sentries.get(sentry)).filter(sentry -> sentries.getT(sentry) <= System.currentTimeMillis()).forEach(sentry -> {
                        sentries.setT(sentry, System.currentTimeMillis() + sentry.getCooldown());
                        Bukkit.getScheduler().runTask(main, new BukkitRunnable() {
                            @Override
                            public void run() {
                                new Bullet(sentry);
                            }
                        });
                    });
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