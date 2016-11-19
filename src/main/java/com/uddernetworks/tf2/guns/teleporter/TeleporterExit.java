package com.uddernetworks.tf2.guns.teleporter;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TeleporterExit {

    private Location location;
    private ArmorStand tag;

    private int health = 150;

    private Player owner;

    public TeleporterExit(Location location, Player owner) {
        try {
            this.location = location;
            this.owner = owner;

            Teleporters.addTeleporterExit(this);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void spawnTeleporter() {
        try {
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
                            tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Exit Active - " + owner.getName());
                        } else {
                            tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Exit Active - " + owner.getName());
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
                            tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Exit Unactive - " + owner.getName());
                        } else {
                            tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Exit Unactive - " + owner.getName());
                        }
                    }
                }
            } else {
                owner.sendMessage(ChatColor.RED + "Please stand in an open block");
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void update() {
        try {
            location.getBlock().setType(Material.CARPET);
            if (Teleporters.hasCounterpart(this)) {

                if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                    location.getBlock().setData(DyeColor.BLUE.getData());
                } else {
                    location.getBlock().setData(DyeColor.RED.getData());
                }

                tag.teleport(location.clone().subtract(0, 1.5, 0));
                if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                    tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Exit Active - " + owner.getName());
                } else {
                    tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Exit Active - " + owner.getName());
                }
            } else {

                if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                    location.getBlock().setData(DyeColor.LIGHT_BLUE.getData());
                } else {
                    location.getBlock().setData(DyeColor.ORANGE.getData());
                }

                tag.teleport(location.clone().subtract(0, 1.5, 0));
                if (PlayerTeams.getPlayer(owner) == TeamEnum.BLUE) {
                    tag.setCustomName(ChatColor.BOLD + "" + ChatColor.BLUE + "Teleporter Exit Unactive - " + owner.getName());
                } else {
                    tag.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + "Teleporter Exit Unactive - " + owner.getName());
                }
            }

            tag.setGravity(false);
            tag.setCanPickupItems(false);
            tag.setCustomNameVisible(true);
            tag.setVisible(false);
            tag.setSmall(true);
            tag.setCollidable(false);
            tag.setSilent(true);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void remove(boolean update) {
        try {
            tag.remove();
            location.getBlock().setType(Material.AIR);
            if (update && Teleporters.hasCounterpart(this)) {
                Teleporters.removeTeleporterExit(this);
                Teleporters.getCounterpart(this).update();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public Player getPlayer() {
        try {
            return owner;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public Location getLocation() {
        try {
            return location;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public int getHealth() {
        try {
            return health;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public void setHealth(int health) {
        try {
            if (health < 0) {
                remove(true);
            } else {
                this.health = health;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}
