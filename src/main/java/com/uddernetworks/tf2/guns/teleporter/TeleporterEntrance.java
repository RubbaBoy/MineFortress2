package com.uddernetworks.tf2.guns.teleporter;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TeleporterEntrance {

    private Location location;
    private ArmorStand tag;

    private int health = 150;

    private Player owner;

    public TeleporterEntrance(Location location, Player owner) {
        this.location = location;
        this.owner = owner;

        Teleporters.addTeleporterEntrance(this);
    }

    public void spawnTeleporter() {
        if (location.getBlock().getType() == Material.AIR) {
            location.getBlock().setType(Material.CARPET);
            if (Teleporters.hasCounterpart(this)) {
                Teleporters.getCounterpart(this).update();
                if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                    location.getBlock().setData(DyeColor.BLUE.getData());
                } else {
                    location.getBlock().setData(DyeColor.RED.getData());
                }

                if (tag == null) {
                    tag = (ArmorStand) location.getWorld().spawnEntity(location.clone().subtract(0, 1.5, 0), EntityType.ARMOR_STAND);
                    tag.setGravity(false);
                    tag.setCanPickupItems(false);
                    tag.setCustomNameVisible(true);
                    tag.setVisible(false);
                    tag.setSmall(true);
                    tag.setCollidable(false);
                    tag.setSilent(true);

                    if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                        tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Entrance Active - " + owner.getName());
                    } else {
                        tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Entrance Active - " + owner.getName());
                    }
                }
            } else {
                if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                    location.getBlock().setData(DyeColor.LIGHT_BLUE.getData());
                } else {
                    location.getBlock().setData(DyeColor.ORANGE.getData());
                }

                if (tag == null) {
                    tag = (ArmorStand) location.getWorld().spawnEntity(location.clone().subtract(0, 1.5, 0), EntityType.ARMOR_STAND);
                    tag.setGravity(false);
                    tag.setCanPickupItems(false);
                    tag.setCustomNameVisible(true);
                    tag.setVisible(false);
                    tag.setSmall(true);
                    tag.setCollidable(false);
                    tag.setSilent(true);

                    if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                        tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Entrance Unactive - " + owner.getName());
                    } else {
                        tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Entrance Unactive - " + owner.getName());
                    }
                }
            }
        } else {
            owner.sendMessage(ChatColor.RED + "Please stand in an open block");
        }
    }

    public void update() {
        location.getBlock().setType(Material.CARPET);
        if (Teleporters.hasCounterpart(this)) {

            if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                location.getBlock().setData(DyeColor.BLUE.getData());
            } else {
                location.getBlock().setData(DyeColor.RED.getData());
            }

            tag.teleport(location.clone().subtract(0, 1.5, 0));
            if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Entrance Active - " + owner.getName());
            } else {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Entrance Active - " + owner.getName());
            }
        } else {

            if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                location.getBlock().setData(DyeColor.LIGHT_BLUE.getData());
            } else {
                location.getBlock().setData(DyeColor.ORANGE.getData());
            }

            tag.teleport(location.clone().subtract(0, 1.5, 0));
            if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Entrance Unactive - " + owner.getName());
            } else {
                tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Entrance Unactive - " + owner.getName());
            }
        }

        tag.setGravity(false);
        tag.setCanPickupItems(false);
        tag.setCustomNameVisible(true);
        tag.setVisible(false);
        tag.setSmall(true);
        tag.setCollidable(false);
        tag.setSilent(true);
    }

    public void teleport(Player player) {
        if (Teleporters.hasCounterpart(this)) {
            player.teleport(Teleporters.getCounterpart(this).getLocation());
        }
    }

    public void remove() {
        tag.remove();
        location.getBlock().setType(Material.AIR);
        Teleporters.removeTeleporterEntrance(this);
        if (Teleporters.hasCounterpart(this)) {
            Teleporters.getCounterpart(this).update();
        }
    }

    public Player getPlayer() {
        return owner;
    }

    public Location getLocation() {
        return location;
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
