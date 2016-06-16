package com.uddernetworks.tf2.main;

import com.uddernetworks.tf2.guns.Gun;
import com.uddernetworks.tf2.guns.GunList;
import com.uddernetworks.tf2.guns.GunListener;
import com.uddernetworks.tf2.guns.GunObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;

    Gun gun = new Gun(this);

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getPluginManager().registerEvents(new GunListener(), this);

        try {
            gun.loadGuns();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Object gun_obj : GunList.getGunlist().values()) {
            GunObject gun = (GunObject) gun_obj;
            gun.giveGun(Bukkit.getPlayer("RubbaBoy"));
        }
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static Main getPlugin() {
        return plugin;
    }

}