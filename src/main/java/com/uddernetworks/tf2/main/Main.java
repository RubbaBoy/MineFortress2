package com.uddernetworks.tf2.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public static Main getPlugin() {
        return plugin;
    }

}
