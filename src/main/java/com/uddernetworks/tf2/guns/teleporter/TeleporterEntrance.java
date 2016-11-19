package com.uddernetworks.tf2.guns.teleporter;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.*;
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
        try {
            this.location = location;
            this.owner = owner;

            Teleporters.addTeleporterEntrance(this);
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
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void teleport(Player player) {
        try {
            if (Teleporters.hasCounterpart(this)) {
                player.teleport(Teleporters.getCounterpart(this).getLocation());
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void remove(boolean update) {
        try {
            tag.remove();
            location.getBlock().setType(Material.AIR);
            if (update && Teleporters.hasCounterpart(this)) {
                Teleporters.removeTeleporterEntrance(this);
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
                remove(false);
            } else {
                this.health = health;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}
