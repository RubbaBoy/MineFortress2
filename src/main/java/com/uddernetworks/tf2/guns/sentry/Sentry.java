package com.uddernetworks.tf2.guns.sentry;

import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.threads.SentryThreadUtil;
import net.minecraft.server.v1_10_R1.EntityItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Sentry implements Listener {

    private int range;

    private int tier = 0;

    private double damage_1 = 16;
    private double power_1 = 10;
    private long cooldown_1 = 250;
    private double accuracy_1 = 7;
    private int health_1 = 150;

    private double damage_2 = 16;
    private double power_2 = 10;
    private long cooldown_2 = 125;
    private double accuracy_2 = 4;
    private int health_2 = 180;

    private double damage_3 = 16;
    private double power_3 = 10;
    private long cooldown_3 = 60;
    private double accuracy_3 = 2;
    private int health_3 = 216;

    private double damage = damage_1;
    private double power = power_1;
    private long cooldown = cooldown_1;
    private double accuracy = accuracy_1;
    private int health = health_1;

    private Location location;
    private ArmorStand sentry_obj;
    private Entity target;

    private Player owner;

    public Sentry(Location location, Player owner, int range) {
        this.location = location;
        this.range = range;
        this.owner = owner;

        Sentries.addSentry(this);
    }

    public void spawnSentry() {
        sentry_obj = location.getWorld().spawn(location, ArmorStand.class);
        sentry_obj.setHelmet(new ItemStack(Material.WOOL, (short) 0));

        BukkitScheduler scheduler = Main.getPlugin().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
            Entity closest_entity = null;

            for (Entity entity : location.getWorld().getEntities()) {
                if (closest_entity == null) {
                    if (location.distance(entity.getLocation()) <= range && entity != sentry_obj) {
                        if (!(entity instanceof Arrow) && !(entity instanceof EntityItem) && !(entity instanceof Player)) {
                            closest_entity = entity;
                        }
                    }
                } else {
                    if (location.distance(entity.getLocation()) <= location.distance(closest_entity.getLocation()) && entity != sentry_obj) {
                        if (!(entity instanceof Arrow) && !(entity instanceof EntityItem) && !(entity instanceof Player)) {
                            closest_entity = entity;
                        }
                    }
                }
            }

            target = closest_entity;

            if (closest_entity != null) {

                float angle = (float) Math.toDegrees(Math.atan2(location.getZ() - closest_entity.getLocation().getZ(), location.getX() - closest_entity.getLocation().getX()));
                float angle2 = (float) Math.toDegrees(location.getY() - closest_entity.getLocation().getY());

                EulerAngle angle3 = new EulerAngle(Math.toRadians(angle2), Math.toRadians(angle + 45), 0F);
                sentry_obj.setHeadPose(angle3);

                if (!SentryThreadUtil.sentries.containsKey(this) || !SentryThreadUtil.sentries.get(this)) {
                    SentryThreadUtil.sentries.put(this, true);
                    SentryThreadUtil.sentries.setT(this, System.currentTimeMillis() + this.getCooldown());
                }
            } else {
                SentryThreadUtil.sentries.put(this, false);
            }
        }, 0L, 1L);
    }

    public void upgrade() {
        if (tier == 0) {
            Bukkit.getPlayer("RubbaBoy").sendMessage("Upgrading sentry from level " + tier);
            tier = 1;
            sentry_obj.setHelmet(new ItemStack(Material.WOOL, (short) 8));
            damage = damage_2;
            power = power_2;
            cooldown = cooldown_2;
            accuracy = accuracy_2;
            health = health_2;
        } else if (tier == 1) {
            Bukkit.getPlayer("RubbaBoy").sendMessage("Upgrading sentry from level " + tier);
            tier = 2;
            sentry_obj.setHelmet(new ItemStack(Material.WOOL, (short) 7));
            damage = damage_3;
            power = power_3;
            cooldown = cooldown_3;
            accuracy = accuracy_3;
            health = health_3;
        } else {
            Bukkit.getPlayer("RubbaBoy").sendMessage("Sentries cant be upgraded past tier 3!");
        }
    }

    public void remove() {
        Sentries.removeSentry(Sentries.getSentryId(this));
        SentryThreadUtil.sentries.remove(this);
        sentry_obj.remove();
    }

    public int getTier() {
        return tier;
    }

    public double getDamage() {
        return damage;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getPower() {
        return power;
    }

    public long getCooldown() {
        return cooldown;
    }

    public ArmorStand getObj() {
        return sentry_obj;
    }

    public Entity getTarget() {
        return target;
    }

    public Player getPlayer() {
        return owner;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0) {
            remove();
        } else {
            this.health = health;
        }
    }
}
