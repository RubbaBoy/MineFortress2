package com.uddernetworks.tf2.utils.data;

import com.uddernetworks.tf2.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Locations {

    Main main;

    public static Location firstDoorLoc;
    public static Location secondDoorLoc;

    public static Location randomDoor;
    public static Location blueDoor;
    public static Location redDoor;
    public static Location spectateDoor;
    public static Location teamChooseSpawn;
    public static Location blueSign;
    public static Location redSign;

    public Locations(Main main) {
        this.main = main;
        this.main.reloadConfig();
        randomDoor = new Location(Bukkit.getWorld(this.main.getConfig().getString("world")), this.main.getConfig().getInt("random-door-X"), this.main.getConfig().getInt("random-door-Y"), this.main.getConfig().getInt("random-door-Z"));
        blueDoor = new Location(Bukkit.getWorld(this.main.getConfig().getString("world")), this.main.getConfig().getInt("blue-door-X"), this.main.getConfig().getInt("blue-door-Y"), this.main.getConfig().getInt("blue-door-Z"));
        redDoor = new Location(Bukkit.getWorld(this.main.getConfig().getString("world")), this.main.getConfig().getInt("red-door-X"), this.main.getConfig().getInt("red-door-Y"), this.main.getConfig().getInt("red-door-Z"));
        spectateDoor = new Location(Bukkit.getWorld(this.main.getConfig().getString("world")), this.main.getConfig().getInt("spectate-door-X"), this.main.getConfig().getInt("spectate-door-Y"), this.main.getConfig().getInt("spectate-door-Z"));

        teamChooseSpawn = new Location(Bukkit.getWorld(this.main.getConfig().getString("world")), this.main.getConfig().getInt("team-choose-spawn-X"), this.main.getConfig().getInt("team-choose-spawn-Y"), this.main.getConfig().getInt("team-choose-spawn-Z"));
        redSign = new Location(Bukkit.getWorld(this.main.getConfig().getString("world")), this.main.getConfig().getInt("red-sign-X"), this.main.getConfig().getInt("red-sign-Y"), this.main.getConfig().getInt("red-sign-Z"));
        blueSign = new Location(Bukkit.getWorld(this.main.getConfig().getString("world")), this.main.getConfig().getInt("blue-sign-X"), this.main.getConfig().getInt("blue-sign-Y"), this.main.getConfig().getInt("blue-sign-Z"));
    }

}
