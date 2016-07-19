package com.uddernetworks.tf2.main;

import com.uddernetworks.tf2.command.CommandTF2;
import com.uddernetworks.tf2.guns.Bullet;
import com.uddernetworks.tf2.guns.Gun;
import com.uddernetworks.tf2.guns.GunListener;
import com.uddernetworks.tf2.inv.AdminGunList;
import com.uddernetworks.tf2.utils.ArrowHitBlockEvent;
import com.uddernetworks.tf2.utils.GunThreadUtil;
import net.minecraft.server.v1_9_R1.EntityArrow;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class Main extends JavaPlugin implements Listener {

    GunThreadUtil thread;

    public static Main plugin;

    Gun gun = new Gun(this);

    @Override
    public void onEnable() {
        plugin = this;

        thread = new GunThreadUtil(this);
        Bukkit.getPluginManager().registerEvents(new GunListener(this, thread), this);
        Bukkit.getPluginManager().registerEvents(new AdminGunList(), this);
        Bukkit.getPluginManager().registerEvents(new Bullet(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("tf2").setExecutor(new CommandTF2(this));

        try {
            gun.loadGuns();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        thread.stahp();
        plugin = null;
    }

    @EventHandler
    private void onProjectileHit(final ProjectileHitEvent e) {
        if(e.getEntityType() == EntityType.ARROW) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                    try {
                        EntityArrow e1 = ((CraftArrow)e.getEntity()).getHandle();
                        Field fieldX = EntityArrow.class.getDeclaredField("h");
                        Field fieldY = EntityArrow.class.getDeclaredField("as");
                        Field fieldZ = EntityArrow.class.getDeclaredField("at");
                        fieldX.setAccessible(true);
                        fieldY.setAccessible(true);
                        fieldZ.setAccessible(true);
                        int x = fieldX.getInt(e1);
                        int y = fieldY.getInt(e1);
                        int z = fieldZ.getInt(e1);
                        if(isValidBlock(x, y, z)) {
                            Block block = e.getEntity().getWorld().getBlockAt(x, y, z);
                            Bukkit.getServer().getPluginManager().callEvent(new ArrowHitBlockEvent((Arrow)e.getEntity(), block));
                        }
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException var9) {
                        var9.printStackTrace();
                    }

                }
            });
        }

    }

    private boolean isValidBlock(int x, int y, int z) {
        return x != -1 && y != -1 && z != -1;
    }

    public static Main getPlugin() {
        return plugin;
    }

}