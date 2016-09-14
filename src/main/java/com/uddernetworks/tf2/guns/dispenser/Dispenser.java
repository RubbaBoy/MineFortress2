package com.uddernetworks.tf2.guns.dispenser;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Dispenser {

    private Location location;
    private Block topBlock;
    private int level = 1;
    private int health_0 = 150;
    private int health_1 = 180;
    private int health_2 = 216;
    private int health = health_0;

    private Player owner;

    public Dispenser(Location location, Player owner) {
        this.location = location;
        this.owner = owner;

        Dispensers.addDispenser(this);
    }

    public void spawnDispenser() {
        if (level == 1) {
            if (location.getBlock().getType() == Material.AIR && location.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                location.getBlock().setType(Material.QUARTZ_ORE);
                location.clone().add(0, 1, 0).getBlock().setType(Material.DIAMOND_ORE);
            } else {
                owner.sendMessage(ChatColor.RED + "You can't be in a block when you place a dispenser");
            }
        } else if (level == 2) {
            location.getBlock().setType(Material.IRON_ORE);
            location.clone().add(0, 1, 0).getBlock().setType(Material.GOLD_ORE);
        } else if (level == 3) {
            location.getBlock().setType(Material.IRON_ORE);
            location.clone().add(0, 1, 0).getBlock().setType(Material.GOLD_ORE);
            location.clone().add(0, 2, 0).getBlock().setType(Material.ICE);
        }
    }

    public void upgrade() {
        if (level == 1) {
            level = 2;
            health = health_1;
            spawnDispenser();
        } else if (level == 2) {
            level = 3;
            health = health_2;
            topBlock = location.clone().add(0, 2, 0).getBlock();
            spawnDispenser();
        }
    }

    public void remove() {
        if (topBlock != null) {
            location.clone().add(0, 2, 0).getBlock().setType(topBlock.getType());
            location.clone().add(0, 2, 0).getBlock().setData(topBlock.getData());
        }
        location.clone().getBlock().setType(Material.AIR);
        location.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
        Dispensers.removeDispenser(Dispensers.getDipenserId(this));
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return owner;
    }

    public int getLevel() {
        return level;
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
