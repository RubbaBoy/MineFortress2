package com.uddernetworks.tf2.guns.dispenser;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Dispenser {

    private ArmorStand tag;

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
        if (tag == null) {
            tag = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, 0.5, 0), EntityType.ARMOR_STAND);

            tag.setGravity(false);
            tag.setCanPickupItems(false);
            if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Dispenser Level 1 - " + owner.getName());
            } else {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Dispenser Level 1 - " + owner.getName());
            }
            tag.setCustomNameVisible(true);
            tag.setVisible(false);
            tag.setSmall(true);
            tag.setCollidable(false);
            tag.setSilent(true);
        }
        if (level == 1) {
            if (location.getBlock().getType() == Material.AIR && location.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                if (PlayerTeams.getPlayer(owner) == TeamEnum.RED) {
                    location.getBlock().setType(Material.QUARTZ_ORE);
                    location.clone().add(0, 1, 0).getBlock().setType(Material.DIAMOND_ORE);
                } else {
                    location.getBlock().setType(Material.LAPIS_ORE);
                    location.clone().add(0, 1, 0).getBlock().setType(Material.COAL_ORE);
                }
            } else {
                owner.sendMessage(ChatColor.RED + "You can't be in a block when you place a dispenser");
            }
        } else if (level == 2) {
            if (PlayerTeams.getPlayer(owner) == TeamEnum.RED) {
                location.getBlock().setType(Material.IRON_ORE);
                location.clone().add(0, 1, 0).getBlock().setType(Material.GOLD_ORE);
            } else {
                location.getBlock().setType(Material.EMERALD_ORE);
                location.clone().add(0, 1, 0).getBlock().setType(Material.REDSTONE_BLOCK);
            }
        } else if (level == 3) {
            if (PlayerTeams.getPlayer(owner) == TeamEnum.RED) {
                location.getBlock().setType(Material.IRON_ORE);
                location.clone().add(0, 1, 0).getBlock().setType(Material.GOLD_ORE);
                location.clone().add(0, 2, 0).getBlock().setType(Material.ICE);
            } else {
                location.getBlock().setType(Material.EMERALD_ORE);
                location.clone().add(0, 1, 0).getBlock().setType(Material.REDSTONE_BLOCK);
                location.clone().add(0, 2, 0).getBlock().setType(Material.BEACON);
            }
        }
    }

    public void upgrade() {
        if (level == 1) {
            level = 2;
            if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Dispenser Level 2 - " + owner.getName());
            } else {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Dispenser Level 2 - " + owner.getName());
            }
            health = health_1;
            spawnDispenser();
        } else if (level == 2) {
            topBlock = location.clone().add(0, 2, 0).getBlock();
            level = 3;
            if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                tag.teleport(location.clone().add(0, 1, 0));
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Dispenser Level 3 - " + owner.getName());
            } else {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Dispenser Level 3 - " + owner.getName());
            }
            health = health_2;
            spawnDispenser();
        }
    }

    public void remove() {
        tag.remove();
        if (topBlock != null) {
            location.clone().add(0, 2, 0).getBlock().setType(topBlock.getType());
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
