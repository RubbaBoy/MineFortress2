package com.uddernetworks.tf2.main;

import com.uddernetworks.tf2.command.CommandTF2;
import com.uddernetworks.tf2.guns.*;
import com.uddernetworks.tf2.inv.AdminGunList;
import com.uddernetworks.tf2.utils.ArrowHitBlockEvent;
import com.uddernetworks.tf2.utils.GunThreadUtil;
import com.uddernetworks.tf2.utils.WeaponType;
import net.minecraft.server.v1_9_R1.EntityArrow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftArrow;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    GunThreadUtil thread;

    public static Main plugin;

    Gun gun = new Gun(this);

    private Location firstDoorLoc;
    private Location secondDoorLoc;

    private PlayerGuns playerGuns = new PlayerGuns();

    @Override
    public void onEnable() {
        plugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

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


    public ArrayList<Location> getRefillDoorBlocks() throws Exception {
        ArrayList<Location> list = new ArrayList<>();
        reloadConfig();
        Location temp = new Location(Bukkit.getWorld(getConfig().getString("world")), getConfig().getInt("door-X"), getConfig().getInt("door-Y"), getConfig().getInt("door-Z"));
        Location second_door = null;
        if (temp.clone().getBlock().getType() == Material.BIRCH_DOOR || temp.clone().getBlock().getType() == Material.ACACIA_DOOR) {
            if (temp.clone().getBlock().getType() == Material.BIRCH_DOOR) {

                for (int i = 0; i < getSurroundingHorizontalBlocks(temp.clone()).size(); i++) {
                    if (getSurroundingHorizontalBlocks(temp.clone()).get(i).getType() == Material.ACACIA_DOOR) {
                        second_door = getSurroundingHorizontalBlocks(temp.clone()).get(i).getLocation();
                    }
                }
                if (second_door == null) {
                    throw new Exception("Coordinate given is not a refill door.");
                } else {
                    if (temp.clone().getBlockX() == second_door.clone().getBlockX()) {
                        if (temp.clone().getBlockZ() - 1 == second_door.clone().getBlockZ()) {
                            if (temp.clone().add(-1, 0, 0).getBlock().getType() == Material.DISPENSER
                                    && temp.clone().add(-1, 1, 0).getBlock().getType() == Material.PISTON_STICKY_BASE
                                    && temp.clone().add(-1, 0, -1).getBlock().getType() == Material.DROPPER
                                    && temp.clone().add(-1, 1, -1).getBlock().getType() == Material.PISTON_BASE) {
                                firstDoorLoc = temp;
                                secondDoorLoc = second_door;
                                list.add(temp.clone().add(-1, 0, 0));
                                list.add(second_door.clone().add(-1, 0, 0));
                            } else {
                                throw new Exception("Coordinate given is not a refill door.");
                            }
                        } if (temp.clone().getBlockZ() + 1 == second_door.clone().getBlockZ()) {
                            if (temp.clone().add(1, 0, 0).getBlock().getType() == Material.DISPENSER
                                    && temp.clone().add(1, 1, 0).getBlock().getType() == Material.PISTON_STICKY_BASE
                                    && temp.clone().add(1, 0, 1).getBlock().getType() == Material.DROPPER
                                    && temp.clone().add(1, 1, 1).getBlock().getType() == Material.PISTON_BASE) {
                                firstDoorLoc = temp;
                                secondDoorLoc = second_door;
                                list.add(temp.clone().add(1, 0, 0));
                                list.add(second_door.clone().add(1, 0, 0));
                            } else {
                                throw new Exception("Coordinate given is not a refill door.");
                            }
                        } else {
                            throw new Exception("Coordinate given is not a refill door.");
                        }
                    } else if (temp.clone().getBlockZ() == second_door.clone().getBlockZ()) {
                        if (temp.clone().getBlockX() + 1 == second_door.clone().getBlockX()) {
                            if (temp.clone().add(0, 0, -1).getBlock().getType() == Material.DISPENSER
                                    && temp.clone().add(0, 1, -1).getBlock().getType() == Material.PISTON_STICKY_BASE
                                    && temp.clone().add(1, 0, -1).getBlock().getType() == Material.DROPPER
                                    && temp.clone().add(1, 1, -1).getBlock().getType() == Material.PISTON_BASE) {
                                firstDoorLoc = temp;
                                secondDoorLoc = second_door;
                                list.add(temp.clone().add(0, 0, -1));
                                list.add(second_door.clone().add(0, 0, -1));
                            } else {
                                throw new Exception("Coordinate given is not a refill door.");
                            }
                        } if (temp.clone().getBlockX() - 1 == second_door.clone().getBlockX()) {
                            if (temp.clone().add(0, 0, 1).getBlock().getType() == Material.DISPENSER
                                    && temp.clone().add(0, 1, 1).getBlock().getType() == Material.PISTON_STICKY_BASE
                                    && temp.clone().add(-1, 0, 1).getBlock().getType() == Material.DROPPER
                                    && temp.clone().add(-1, 1, 1).getBlock().getType() == Material.PISTON_BASE) {
                                firstDoorLoc = temp;
                                secondDoorLoc = second_door;
                                list.add(temp.clone().add(0, 0, -1));
                                list.add(second_door.clone().add(0, 0, -1));
                            } else {
                                throw new Exception("Coordinate given is not a refill door.");
                            }
                        } else {
                            throw new Exception("Coordinate given is not a refill door.");
                        }
                    }
                }
            }
        } else {
            throw new Exception("Coordinate given is not a refill door.888");
        }
        return list;
    }

    public ArrayList<Block> getSurroundingHorizontalBlocks(Location location) {
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(location.clone().add(0, 0, 1).getBlock());
        blocks.add(location.clone().add(0, 0, -1).getBlock());
        blocks.add(location.clone().add(1, 0, 0).getBlock());
        blocks.add(location.clone().add(-1, 0, 0).getBlock());
        return blocks;
    }

    HashMap<Player, Boolean> players = new HashMap<>();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!players.containsKey(event.getPlayer())) {
            players.put(event.getPlayer(), false);
        }
        try {
            if (getRefillDoorBlocks().get(0).getBlockX() == event.getPlayer().getLocation().getBlockX()
                    && getRefillDoorBlocks().get(0).getBlockY() == event.getPlayer().getLocation().getBlockY()
                    && getRefillDoorBlocks().get(0).getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                if (!players.get(event.getPlayer())) {
                    toggleDoorState(firstDoorLoc.getBlock());
                    toggleDoorState(secondDoorLoc.getBlock());

                    event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
                    for (Object gun_obj : GunList.getGunlist().values()) {
                        GunObject gun = (GunObject) gun_obj;
                        if (gun.getType() == WeaponType.PRIMARY || gun.getType() == WeaponType.SECONDARY) {
                            if (event.getPlayer().getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {
                                playerGuns.setClip(event.getPlayer(), gun.getMaxClip());
                                playerGuns.setAmmo(event.getPlayer(), gun.getMaxAmmo());
                            }
                        }
                    }
                    players.put(event.getPlayer(), true);
                }
            } else if (getRefillDoorBlocks().get(1).getBlockX() == event.getPlayer().getLocation().getBlockX()
                    && getRefillDoorBlocks().get(1).getBlockY() == event.getPlayer().getLocation().getBlockY()
                    && getRefillDoorBlocks().get(1).getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                if (!players.get(event.getPlayer())) {
                    toggleDoorState(firstDoorLoc.getBlock());
                    toggleDoorState(secondDoorLoc.getBlock());

                    event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
                    for (Object gun_obj : GunList.getGunlist().values()) {
                        GunObject gun = (GunObject) gun_obj;
                        if (gun.getType() == WeaponType.PRIMARY || gun.getType() == WeaponType.SECONDARY) {
                            if (event.getPlayer().getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {
                                playerGuns.setClip(event.getPlayer(), gun.getMaxClip());
                                playerGuns.setAmmo(event.getPlayer(), gun.getMaxAmmo());
                            }
                        }
                    }
                    players.put(event.getPlayer(), true);
                }
            } else if (firstDoorLoc.getBlockX() == event.getPlayer().getLocation().getBlockX()
                    && firstDoorLoc.getBlockY() == event.getPlayer().getLocation().getBlockY()
                    && firstDoorLoc.getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                if (!players.get(event.getPlayer())) {
                    toggleDoorState(firstDoorLoc.getBlock());
                    toggleDoorState(secondDoorLoc.getBlock());

                    event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
                    for (Object gun_obj : GunList.getGunlist().values()) {
                        GunObject gun = (GunObject) gun_obj;
                        if (gun.getType() == WeaponType.PRIMARY || gun.getType() == WeaponType.SECONDARY) {
                            if (event.getPlayer().getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {
                                playerGuns.setClip(event.getPlayer(), gun.getMaxClip());
                                playerGuns.setAmmo(event.getPlayer(), gun.getMaxAmmo());
                            }
                        }
                    }
                    players.put(event.getPlayer(), true);
                }
            } else if (secondDoorLoc.getBlockX() == event.getPlayer().getLocation().getBlockX()
                    && secondDoorLoc.getBlockY() == event.getPlayer().getLocation().getBlockY()
                    && secondDoorLoc.getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                if (!players.get(event.getPlayer())) {
                    toggleDoorState(firstDoorLoc.getBlock());
                    toggleDoorState(secondDoorLoc.getBlock());

                    event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
                    for (Object gun_obj : GunList.getGunlist().values()) {
                        GunObject gun = (GunObject) gun_obj;
                        if (gun.getType() == WeaponType.PRIMARY || gun.getType() == WeaponType.SECONDARY) {
                            if (event.getPlayer().getInventory().getItemInMainHand().serialize().toString().equals(gun.getItemStack().serialize().toString())) {
                                playerGuns.setClip(event.getPlayer(), gun.getMaxClip());
                                playerGuns.setAmmo(event.getPlayer(), gun.getMaxAmmo());
                            }
                        }
                    }
                    players.put(event.getPlayer(), true);
                }
            } else {
                if (players.get(event.getPlayer())) {
                    toggleDoorState(firstDoorLoc.getBlock());
                    toggleDoorState(secondDoorLoc.getBlock());
                    players.put(event.getPlayer(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack().getTypeId() == 284) {
            event.setCancelled(true);
            event.getItem().remove();
            event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
            event.getPlayer().setFoodLevel(20);
        } else if (event.getItem().getItemStack().getTypeId() == 256) {
            event.setCancelled(true);
            event.getItem().remove();
            playerGuns.setClip(event.getPlayer(), playerGuns.getPlayerGun(event.getPlayer()).getMaxClip());
            playerGuns.setAmmo(event.getPlayer(), playerGuns.getPlayerGun(event.getPlayer()).getMaxAmmo());
        }
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent e) {
        if(e.getEntityType() == EntityType.ARROW) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
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

            });
        }

    }

    public void toggleDoorState(Block block) {
        String doorName = block.getType().getData().getName();
        if (doorName.equalsIgnoreCase("org.bukkit.material.door")) {
            BlockState state = block.getState();
            Door door = (Door) state.getData();
            if (door.isOpen()) {
                door.setOpen(false);
            } else {
                door.setOpen(true);
            }
            state.update();
        }
    }

    private boolean isValidBlock(int x, int y, int z) {
        return x != -1 && y != -1 && z != -1;
    }

    public static Main getPlugin() {
        return plugin;
    }

}