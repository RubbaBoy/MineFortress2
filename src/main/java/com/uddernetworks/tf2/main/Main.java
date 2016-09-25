package com.uddernetworks.tf2.main;

import com.uddernetworks.tf2.arena.ArenaManager;
import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.arena.TeamChooser;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.game.GameState;
import com.uddernetworks.tf2.guns.*;
import com.uddernetworks.tf2.guns.dispenser.Dispenser;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import com.uddernetworks.tf2.inv.*;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.*;
import com.uddernetworks.tf2.utils.data.Locations;
import com.uddernetworks.tf2.utils.threads.GunThreadUtil;
import com.uddernetworks.tf2.utils.threads.SentryThreadUtil;
import net.minecraft.server.v1_10_R1.EntityArrow;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftSnowball;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.material.Door;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    GunThreadUtil thread;
    SentryThreadUtil sentry_thread;

    public static Main plugin;
    boolean running = false;

    Gun gun = new Gun(this);

    private static PlayerGuns playerGuns = new PlayerGuns();
    private static PlayerHealth playerHealth = new PlayerHealth();

    private Game game = new Game(this);

    private static ArrayList<Player> dispenser_players = new ArrayList<>();
    private ArrayList<Location> blue_barriers = new ArrayList<>();
    private ArrayList<Location> red_barriers = new ArrayList<>();
    public ArrayList<String> worlds = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        new ArenaManager(this);
        new Locations(this);

        Bukkit.getPluginManager().registerEvents(new TeamChooser(this), this);
        Bukkit.getPluginManager().registerEvents(new GunListener(this, thread), this);
        Bukkit.getPluginManager().registerEvents(new AdminGunList(), this);
        Bukkit.getPluginManager().registerEvents(new ClassChooser(this), this);
        Bukkit.getPluginManager().registerEvents(new Loadout(this), this);
        Bukkit.getPluginManager().registerEvents(new EditLoadout(this), this);
        Bukkit.getPluginManager().registerEvents(new ConstructionChooser(this), this);
        Bukkit.getPluginManager().registerEvents(new DestructionChooser(this), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        MySQL mySQL = new MySQL(this);

        System.out.println("Creating the Loadouts table if it doesn't exist...");
        mySQL.query("CREATE TABLE IF NOT EXISTS 'Loadouts'(UUID TEXT NOT NULL, CLASS TEXT NOT NULL, TYPE TEXT NOT NULL, ID int(0) NOT NULL, NAME TEXT NOT NULL);");

        worlds.addAll(this.getConfig().getConfigurationSection("playworlds").getKeys(false).stream().collect(Collectors.toList()));
        for (int i2 = 0; i2 < worlds.size(); i2++) {
            World world = Bukkit.getWorld(worlds.get(i2));
            ArrayList<String> numbs = new ArrayList<>();
            numbs.addAll(this.getConfig().getConfigurationSection("playworlds." + world.getName() + ".red.barriers").getKeys(false).stream().collect(Collectors.toList()));
            for (int i = 0; i < numbs.size(); i++) {
                for (int x = getConfig().getInt("playworlds." + world.getName() + ".red.barriers." + i + "1-X"); x <= getConfig().getInt("playworlds." + world.getName() + ".red.barriers." + i + "2-X"); x++) {
                    for (int y = getConfig().getInt("playworlds." + world.getName() + ".red.barriers." + i + "1-Y"); y <= getConfig().getInt("playworlds." + world.getName() + ".red.barriers." + i + "2-Y"); y++) {
                        for (int z = getConfig().getInt("playworlds." + world.getName() + ".red.barriers." + i + "1-Z"); z <= getConfig().getInt("playworlds." + world.getName() + ".red.barriers." + i + "2-Z"); z++) {
                            red_barriers.add(new Location(world, x, y, z));
                        }
                    }
                }
            }
            numbs.clear();
            numbs.addAll(this.getConfig().getConfigurationSection("playworlds." + world.getName() + ".blue.barriers").getKeys(false).stream().collect(Collectors.toList()));
            for (int i = 0; i < numbs.size(); i++) {
                for (int x = getConfig().getInt("playworlds." + world.getName() + ".blue.barriers." + i + "1-X"); x <= getConfig().getInt("playworlds." + world.getName() + ".blue.barriers." + i + "2-X"); x++) {
                    for (int y = getConfig().getInt("playworlds." + world.getName() + ".blue.barriers." + i + "1-Y"); y <= getConfig().getInt("playworlds." + world.getName() + ".blue.barriers." + i + "2-Y"); y++) {
                        for (int z = getConfig().getInt("playworlds." + world.getName() + ".blue.barriers." + i + "1-Z"); z <= getConfig().getInt("playworlds." + world.getName() + ".blue.barriers." + i + "2-Z"); z++) {
                            blue_barriers.add(new Location(world, x, y, z));
                        }
                    }
                }
            }
        }

        thread = new GunThreadUtil(this);
        sentry_thread = new SentryThreadUtil(this);

        try {
            gun.loadGuns();
        } catch (Exception e) {
            e.printStackTrace();
        }

        doPlayerEffects();
        running = true;

        SQLLoadout sqlLoadout = new SQLLoadout(plugin);
        sqlLoadout.reload();
    }

    @Override
    public void onDisable() {
        thread.stahp();
        sentry_thread.stahp();
        running = false;

        for (Player player2 : Bukkit.getOnlinePlayers()) {
            ArenaManager.getManager().removePlayer(player2);
        }
        ArenaManager.getManager().clearArenas();

        plugin = null;
    }


    public ArrayList<Location> getRefillDoorBlocks(TeamEnum team, World world) {
        try {
            ArrayList<Location> list = new ArrayList<>();
            reloadConfig();
            Location temp;
            if (world != null) {
                if (team == TeamEnum.BLUE) {
                    temp = new Location(world, getConfig().getInt("playworlds." + world.getName() + ".blue.refill-door-X"), getConfig().getInt("playworlds." + world.getName() + ".blue.refill-door-Y"), getConfig().getInt("playworlds." + world.getName() + ".blue.refill-door-Z"));
                } else {
                    temp = new Location(world, getConfig().getInt("playworlds." + world.getName() + ".red.refill-door-X"), getConfig().getInt("playworlds." + world.getName() + ".red.refill-door-Y"), getConfig().getInt("playworlds." + world.getName() + ".red.refill-door-Z"));
                }
                Location second_door = null;
                if (temp.clone().getBlock().getType() == Material.BIRCH_DOOR || temp.clone().getBlock().getType() == Material.ACACIA_DOOR) {
                    Door door = (Door) temp.clone().getBlock().getState().getData();
                    Block block = temp.getBlock();
                    if (door.isTopHalf()) {
                        block = temp.clone().getBlock().getRelative(BlockFace.DOWN);
                        temp = temp.clone().getBlock().getRelative(BlockFace.DOWN).getLocation();
                    }
                    if (block.getType() == Material.BIRCH_DOOR) {

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
                                        Locations.firstDoorLoc = temp;
                                        Locations.secondDoorLoc = second_door;
                                        list.add(temp.clone().add(-1, 0, 0));
                                        list.add(second_door.clone().add(-1, 0, 0));
                                    } else {
                                        throw new Exception("Coordinate given is not a refill door.");
                                    }
                                } else if (temp.clone().getBlockZ() + 1 == second_door.clone().getBlockZ()) {
                                    if (temp.clone().add(1, 0, 0).getBlock().getType() == Material.DISPENSER
                                            && temp.clone().add(1, 1, 0).getBlock().getType() == Material.PISTON_STICKY_BASE
                                            && temp.clone().add(1, 0, 1).getBlock().getType() == Material.DROPPER
                                            && temp.clone().add(1, 1, 1).getBlock().getType() == Material.PISTON_BASE) {
                                        Locations.firstDoorLoc = temp;
                                        Locations.secondDoorLoc = second_door;
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
                                        Locations.firstDoorLoc = temp;
                                        Locations.secondDoorLoc = second_door;
                                        list.add(temp.clone().add(0, 0, -1));
                                        list.add(second_door.clone().add(0, 0, -1));
                                    } else {
                                        throw new Exception("Coordinate given is not a refill door.");
                                    }
                                } else if (temp.clone().getBlockX() - 1 == second_door.clone().getBlockX()) {
                                    if (temp.clone().add(0, 0, 1).getBlock().getType() == Material.DISPENSER
                                            && temp.clone().add(0, 1, 1).getBlock().getType() == Material.PISTON_STICKY_BASE
                                            && temp.clone().add(-1, 0, 1).getBlock().getType() == Material.DROPPER
                                            && temp.clone().add(-1, 1, 1).getBlock().getType() == Material.PISTON_BASE) {
                                        Locations.firstDoorLoc = temp;
                                        Locations.secondDoorLoc = second_door;
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
                    throw new Exception("Coordinate given is not a refill door.");
                }
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Block> getSurroundingHorizontalBlocks(Location location) {
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(location.clone().add(0, 0, 1).getBlock());
        blocks.add(location.clone().add(0, 0, -1).getBlock());
        blocks.add(location.clone().add(1, 0, 0).getBlock());
        blocks.add(location.clone().add(-1, 0, 0).getBlock());
        return blocks;
    }

    public Location getSpectateLocation(World world) {
        return new Location(world, getConfig().getInt("playworlds." + world.getName() + ".spectate-X"), getConfig().getInt("playworlds." + world.getName() + ".spectate-Y"), getConfig().getInt("playworlds." + world.getName() + ".spectate-Z"));
    }

    public boolean isInBarrier(Location location, TeamEnum teamOfPlayer) {
        if (teamOfPlayer == TeamEnum.BLUE) {
            return red_barriers.contains(location);
        } else {
            return blue_barriers.contains(location);
        }
    }

    HashMap<Player, Boolean> players = new HashMap<>();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (ArenaManager.getManager().isInGame(event.getPlayer())) {
            if (!players.containsKey(event.getPlayer())) {
                players.put(event.getPlayer(), false);
            }
            try {
                if (Game.getGameState() == GameState.INGAME) {
                    if (game.getGameType() != null) {
                        if (game.getWorld() != null) {
                            World world = game.getWorld();
                            for (int i2 = 0; i2 < 2; i2++) {
                                TeamEnum team = TeamEnum.BLUE;
                                if (i2 == 0) {
                                    team = TeamEnum.BLUE;
                                } else if (i2 == 1) {
                                    team = TeamEnum.RED;
                                }
                                if (getRefillDoorBlocks(team, world).get(0).getBlockX() == event.getPlayer().getLocation().getBlockX()
                                        && getRefillDoorBlocks(team, world).get(0).getBlockY() == event.getPlayer().getLocation().getBlockY()
                                        && getRefillDoorBlocks(team, world).get(0).getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                                    if (!players.get(event.getPlayer())) {
                                        toggleDoorState(Locations.firstDoorLoc.getBlock());
                                        toggleDoorState(Locations.secondDoorLoc.getBlock());

                                        playerHealth.addHealth(event.getPlayer(), playerHealth.getMaxHealth(event.getPlayer()));
                                        playerGuns.fillAll(event.getPlayer());
                                        if (PlayerClasses.getPlayerClass(event.getPlayer()) == ClassEnum.ENGINEER) {
                                            PlayerMetal.addPlayer(event.getPlayer(), 200);
                                        }
                                        players.put(event.getPlayer(), true);
                                    }
                                } else if (getRefillDoorBlocks(team, world).get(1).getBlockX() == event.getPlayer().getLocation().getBlockX()
                                        && getRefillDoorBlocks(team, world).get(1).getBlockY() == event.getPlayer().getLocation().getBlockY()
                                        && getRefillDoorBlocks(team, world).get(1).getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                                    if (!players.get(event.getPlayer())) {
                                        toggleDoorState(Locations.firstDoorLoc.getBlock());
                                        toggleDoorState(Locations.secondDoorLoc.getBlock());

                                        playerHealth.addHealth(event.getPlayer(), playerHealth.getMaxHealth(event.getPlayer()));
                                        playerGuns.fillAll(event.getPlayer());
                                        if (PlayerClasses.getPlayerClass(event.getPlayer()) == ClassEnum.ENGINEER) {
                                            PlayerMetal.addPlayer(event.getPlayer(), 200);
                                        }
                                        players.put(event.getPlayer(), true);
                                    }
                                } else if (Locations.firstDoorLoc.getBlockX() == event.getPlayer().getLocation().getBlockX()
                                        && Locations.firstDoorLoc.getBlockY() == event.getPlayer().getLocation().getBlockY()
                                        && Locations.firstDoorLoc.getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                                    if (!players.get(event.getPlayer())) {
                                        toggleDoorState(Locations.firstDoorLoc.getBlock());
                                        toggleDoorState(Locations.secondDoorLoc.getBlock());

                                        playerHealth.addHealth(event.getPlayer(), playerHealth.getMaxHealth(event.getPlayer()));
                                        playerGuns.fillAll(event.getPlayer());
                                        if (PlayerClasses.getPlayerClass(event.getPlayer()) == ClassEnum.ENGINEER) {
                                            PlayerMetal.addPlayer(event.getPlayer(), 200);
                                        }
                                        players.put(event.getPlayer(), true);
                                    }
                                } else if (Locations.secondDoorLoc.getBlockX() == event.getPlayer().getLocation().getBlockX()
                                        && Locations.secondDoorLoc.getBlockY() == event.getPlayer().getLocation().getBlockY()
                                        && Locations.secondDoorLoc.getBlockZ() == event.getPlayer().getLocation().getBlockZ()) {
                                    if (!players.get(event.getPlayer())) {
                                        toggleDoorState(Locations.firstDoorLoc.getBlock());
                                        toggleDoorState(Locations.secondDoorLoc.getBlock());

                                        playerHealth.addHealth(event.getPlayer(), playerHealth.getMaxHealth(event.getPlayer()));
                                        playerGuns.fillAll(event.getPlayer());
                                        if (PlayerClasses.getPlayerClass(event.getPlayer()) == ClassEnum.ENGINEER) {
                                            PlayerMetal.addPlayer(event.getPlayer(), 200);
                                        }
                                        players.put(event.getPlayer(), true);
                                    }
                                } else {
                                    if (players.get(event.getPlayer())) {
                                        toggleDoorState(Locations.firstDoorLoc.getBlock());
                                        toggleDoorState(Locations.secondDoorLoc.getBlock());
                                        players.put(event.getPlayer(), false);
                                    }
                                }
                            }

                            if (Teleporters.isEntrance(event.getTo())) {
                                Teleporters.getEntrance(event.getTo()).teleport(event.getPlayer());
                            }

                            if (Dispensers.isInRange(event.getPlayer()) && !dispenser_players.contains(event.getPlayer())) {
                                dispenser_players.add(event.getPlayer());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static ArrayList<Player> tochange = new ArrayList<>();
    public void doPlayerEffects() {
        new BukkitRunnable() {
            public void run() {
                if (!running) {
                    cancel();
                }
                tochange = (ArrayList<Player>) dispenser_players.clone();
                for (Player player : dispenser_players) {
                    if (Dispensers.isInRange(player)) {
                        Dispenser dispenser = Dispensers.getNearestDispenser(player);
                        assert dispenser != null;
                        if (dispenser.getLevel() == 1) {
                            if (playerHealth.getHealth(player) != playerHealth.getHealth(player)) {
                                if (playerHealth.getHealth(player) + 10 > playerHealth.getMaxHealth(player)) {
                                    playerHealth.addHealth(player, playerHealth.getMaxHealth(player));
                                } else {
                                    playerHealth.addHealth(player, playerHealth.getHealth(player) + 10);
                                }
                            }
                            if (playerGuns.getAmmo(player) != playerGuns.getMaxAmmo(player)) {
                                if (playerGuns.getAmmo(player) + playerGuns.getMaxAmmo(player) * 0.2 > playerGuns.getMaxAmmo(player)) {
                                    playerGuns.setAmmo(player, playerGuns.getMaxAmmo(player));
                                } else {
                                    playerGuns.setAmmo(player, (int) (playerGuns.getAmmo(player) + playerGuns.getMaxAmmo(player) * 0.2));
                                }
                            } else if (playerGuns.getClip(player) != playerGuns.getMaxClip(player)) {
                                if (playerGuns.getClip(player) + playerGuns.getMaxClip(player) * 0.2 > playerGuns.getMaxClip(player)) {
                                    playerGuns.setClip(player, playerGuns.getMaxClip(player));
                                } else {
                                    playerGuns.setClip(player, (int) (playerGuns.getClip(player) + playerGuns.getMaxClip(player) * 0.2));
                                }
                            }
                            if (PlayerClasses.getPlayerClass(player) == ClassEnum.ENGINEER) {
                                if (PlayerMetal.getPlayer(player) != 200) {
                                    if (PlayerMetal.getPlayer(player) + 25 > 200) {
                                        PlayerMetal.addPlayer(player, 200);
                                    } else {
                                        PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) + 25);
                                    }
                                }
                            }
                        } else if (dispenser.getLevel() == 2) {
                            if (playerHealth.getHealth(player) != playerHealth.getHealth(player)) {
                                if (playerHealth.getHealth(player) + 15 > playerHealth.getMaxHealth(player)) {
                                    playerHealth.addHealth(player, playerHealth.getMaxHealth(player));
                                } else {
                                    playerHealth.addHealth(player, playerHealth.getHealth(player) + 15);
                                }
                            }
                            if (playerGuns.getAmmo(player) != playerGuns.getMaxAmmo(player)) {
                                if (playerGuns.getAmmo(player) + playerGuns.getMaxAmmo(player) * 0.3 > playerGuns.getMaxAmmo(player)) {
                                    playerGuns.setAmmo(player, playerGuns.getMaxAmmo(player));
                                } else {
                                    playerGuns.setAmmo(player, (int) (playerGuns.getAmmo(player) + playerGuns.getMaxAmmo(player) * 0.3));
                                }
                            } else if (playerGuns.getClip(player) != playerGuns.getMaxClip(player)) {
                                if (playerGuns.getClip(player) + playerGuns.getMaxClip(player) * 0.3 > playerGuns.getMaxClip(player)) {
                                    playerGuns.setClip(player, playerGuns.getMaxClip(player));
                                } else {
                                    playerGuns.setClip(player, (int) (playerGuns.getClip(player) + playerGuns.getMaxClip(player) * 0.3));
                                }
                            }
                            if (PlayerClasses.getPlayerClass(player) == ClassEnum.ENGINEER) {
                                if (PlayerMetal.getPlayer(player) != 200) {
                                    if (PlayerMetal.getPlayer(player) + 25 > 200) {
                                        PlayerMetal.addPlayer(player, 200);
                                    } else {
                                        PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) + 25);
                                    }
                                }
                            }
                        } else if (dispenser.getLevel() == 3) {
                            if (playerHealth.getHealth(player) != playerHealth.getHealth(player)) {
                                if (playerHealth.getHealth(player) + 20 > playerHealth.getMaxHealth(player)) {
                                    playerHealth.addHealth(player, playerHealth.getMaxHealth(player));
                                } else {
                                    playerHealth.addHealth(player, playerHealth.getHealth(player) + 20);
                                }
                            }
                            if (playerGuns.getAmmo(player) != playerGuns.getMaxAmmo(player)) {
                                if (playerGuns.getAmmo(player) + playerGuns.getMaxAmmo(player) * 0.4 > playerGuns.getMaxAmmo(player)) {
                                    playerGuns.setAmmo(player, playerGuns.getMaxAmmo(player));
                                } else {
                                    playerGuns.setAmmo(player, (int) (playerGuns.getAmmo(player) + playerGuns.getMaxAmmo(player) * 0.4));
                                }
                            } else if (playerGuns.getClip(player) != playerGuns.getMaxClip(player)) {
                                if (playerGuns.getClip(player) + playerGuns.getMaxClip(player) * 0.4 > playerGuns.getMaxClip(player)) {
                                    playerGuns.setClip(player, playerGuns.getMaxClip(player));
                                } else {
                                    playerGuns.setClip(player, (int) (playerGuns.getClip(player) + playerGuns.getMaxClip(player) * 0.4));
                                }
                            }
                            if (PlayerClasses.getPlayerClass(player) == ClassEnum.ENGINEER) {
                                if (PlayerMetal.getPlayer(player) != 200) {
                                    if (PlayerMetal.getPlayer(player) + 50 > 200) {
                                        PlayerMetal.addPlayer(player, 200);
                                    } else {
                                        PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) + 50);
                                    }
                                }
                            }
                        }
                    } else {
                        tochange.remove(player);
                    }
                }
                dispenser_players = tochange;
                tochange.clear();
            }
        }.runTaskTimer(Main.getPlugin(), 0, 40);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (ArenaManager.getManager().isInGame(event.getPlayer())) {
            if (event.getItem().getItemStack().getTypeId() == 284) {
                event.setCancelled(true);
                event.getItem().remove();
                playerHealth.addHealth(event.getPlayer(), playerHealth.getMaxHealth(event.getPlayer()));
                event.getPlayer().setFoodLevel(20);
            } else if (event.getItem().getItemStack().getTypeId() == 256) {
                event.setCancelled(true);
                event.getItem().remove();
                playerGuns.setClip(event.getPlayer(), playerGuns.getPlayerGun(event.getPlayer()).getMaxClip());
                playerGuns.setAmmo(event.getPlayer(), playerGuns.getPlayerGun(event.getPlayer()).getMaxAmmo());
            } else if (event.getItem().getCustomName().equals("GrenadeLauncher")) {
                event.setCancelled(true);
                event.getItem().remove();
            } else if (event.getItem().getCustomName().equals("StickyBomb")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent e) {
        if(e.getEntityType() == EntityType.ARROW) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                try {
                    EntityArrow e1 = ((CraftArrow)e.getEntity()).getHandle();
                    Field fieldX = EntityArrow.class.getDeclaredField("h");
                    Field fieldY = EntityArrow.class.getDeclaredField("au");
                    Field fieldZ = EntityArrow.class.getDeclaredField("av");
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
                } catch (Exception e1) {
                    e1.printStackTrace();
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

    public ArrayList<Location> getSpawnBlocks(World world, TeamEnum team) {
        try {
            int spawn1_x;
            int spawn1_y;
            int spawn1_z;
            int spawn2_x;
            int spawn2_y;
            int spawn2_z;
            if (team == TeamEnum.BLUE) {
                reloadConfig();
                spawn1_x = getConfig().getInt("playworlds." + world.getName() + ".blue.spawn1-X");
                spawn1_y = getConfig().getInt("playworlds." + world.getName() + ".blue.spawn1-Y");
                spawn1_z = getConfig().getInt("playworlds." + world.getName() + ".blue.spawn1-Z");

                spawn2_x = getConfig().getInt("playworlds." + world.getName() + ".blue.spawn2-X");
                spawn2_y = getConfig().getInt("playworlds." + world.getName() + ".blue.spawn2-Y");
                spawn2_z = getConfig().getInt("playworlds." + world.getName() + ".blue.spawn2-Z");
            } else {
                reloadConfig();
                spawn1_x = getConfig().getInt("playworlds." + world.getName() + ".red.spawn1-X");
                spawn1_y = getConfig().getInt("playworlds." + world.getName() + ".red.spawn1-Y");
                spawn1_z = getConfig().getInt("playworlds." + world.getName() + ".red.spawn1-Z");

                spawn2_x = getConfig().getInt("playworlds." + world.getName() + ".red.spawn2-X");
                spawn2_y = getConfig().getInt("playworlds." + world.getName() + ".red.spawn2-Y");
                spawn2_z = getConfig().getInt("playworlds." + world.getName() + ".red.spawn2-Z");
            }

            HashSet<Location> locs = new HashSet<>();

            int topBlockX = (spawn1_x < spawn2_x ? spawn2_x : spawn1_x);
            int bottomBlockX = (spawn1_x > spawn2_x ? spawn2_x : spawn1_x);

            int topBlockY = (spawn1_y < spawn2_y ? spawn2_y : spawn1_y);
            int bottomBlockY = (spawn1_y > spawn2_y ? spawn2_y : spawn1_y);

            int topBlockZ = (spawn1_z < spawn2_z ? spawn2_z : spawn1_z);
            int bottomBlockZ = (spawn1_z > spawn2_z ? spawn2_z : spawn1_z);

            for(int x = bottomBlockX; x <= topBlockX; x++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                        locs.add(new Location(world, x, y, z));
                    }
                }
            }

            return new ArrayList<>(locs);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mf2")) {
            TeamChooser chooser = new TeamChooser(plugin);
            try {
                chooser.sendPlayers();
                ArenaManager.getManager().createArena();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private boolean isValidBlock(int x, int y, int z) {
        return x != -1 && y != -1 && z != -1;
    }

    public static Main getPlugin() {
        return plugin;
    }

}