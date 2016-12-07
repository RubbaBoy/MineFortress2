package com.uddernetworks.tf2.main;

import com.mojang.util.UUIDTypeAdapter;
import com.uddernetworks.tf2.arena.ArenaManager;
import com.uddernetworks.tf2.arena.TeamChooser;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.game.GameState;
import com.uddernetworks.tf2.guns.*;
import com.uddernetworks.tf2.guns.dispenser.Dispenser;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.pickups.Ammos;
import com.uddernetworks.tf2.guns.pickups.Healths;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import com.uddernetworks.tf2.inv.*;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.*;
import com.uddernetworks.tf2.utils.data.Locations;
import com.uddernetworks.tf2.utils.threads.GunThreadUtil;
import com.uddernetworks.tf2.utils.threads.SentryThreadUtil;
import net.minecraft.server.v1_10_R1.EntityArrow;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutSpawnEntityLiving;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.Door;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sun.awt.shell.ShellFolder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    GunThreadUtil thread;
    SentryThreadUtil sentry_thread;

    public static Main plugin;
    boolean running = false;
    private boolean config_reset_wait = false;
    private boolean loadout_reset_wait = false;

    Gun gun = new Gun(this);

    private static PlayerGuns playerGuns = new PlayerGuns();
    private static PlayerHealth playerHealth = new PlayerHealth();
    private ClassChooser chooser = new ClassChooser(this);

    private Game game = new Game(this);
    private SQLLoadout loadouts = new SQLLoadout(this);

    private static ArrayList<Player> dispenser_players = new ArrayList<>();
    private ArrayList<Location> blue_barriers = new ArrayList<>();
    private ArrayList<Location> red_barriers = new ArrayList<>();
    public ArrayList<String> worlds = new ArrayList<>();

    private String[] classpaths = {
            "poi-ooxml-3.15-beta1.jar",
            "poi-3.15-beta1.jar",
            "xmlbeans-2.6.0.jar",
            "ooxml-schemas-1.3.jar",
            "ErrorReport.jar",
            "commons-logging-1.2.jar",
            "httpclient-4.5.2.jar",
            "httpcore-4.4.5.jar"
    };

    @Override
    public void onEnable() {
        plugin = this;
        try {

            System.out.println("Checking if libraries are found...");
            File lib_path = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent() + File.separator + "libs");

            boolean Continue = true;

            for (String path : classpaths) {
                if (new File(lib_path + File.separator + path).exists()) {
                    System.out.println("Library " + path + " is present in folder. Adding it to classpath...");

                    addFile(lib_path + File.separator + path);
                } else {
                    System.out.println("Library " + path + " not found! Please make sure you follow the installation tutorial correctly.");
                    Continue = false;
                }
            }

            if (!Continue) {
                Bukkit.getPluginManager().disablePlugin(Main.plugin);
            } else {

                getConfig().options().copyDefaults(true);
                saveConfig();

                new ArenaManager(this);
                new Locations(this);

                Bukkit.getPluginManager().registerEvents(new TeamChooser(this), this);
                Bukkit.getPluginManager().registerEvents(new Game(this), this);
                Bukkit.getPluginManager().registerEvents(new GunListener(this, thread), this);
                Bukkit.getPluginManager().registerEvents(new Anvil(), this);
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
                System.out.println(Arrays.toString(this.getConfig().getConfigurationSection("playworlds").getKeys(false).stream().collect(Collectors.toList()).toArray()));
                for (String world1 : worlds) {
                    World world = Bukkit.getWorld(world1);
                    if (world == null) {
                        System.out.println("Invalid world name: " + world1);
                    } else {
                        blue_barriers.addAll(getBarrier(TeamEnum.BLUE, world));
                        red_barriers.addAll(getBarrier(TeamEnum.RED, world));
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
        } catch (Exception e) {
            new ExceptionReporter(e);
        }

    }

    public HashSet<Location> getBarrier(TeamEnum team, World world) {
        ArrayList<String> numbs = new ArrayList<>();
        HashSet<Location> locs = new HashSet<>();

        int barrier1_x;
        int barrier1_y;
        int barrier1_z;
        int barrier2_x;
        int barrier2_y;
        int barrier2_z;

        String team_str = team == TeamEnum.BLUE ? "blue" : "red";

        reloadConfig();
        numbs.addAll(this.getConfig().getConfigurationSection("playworlds." + world.getName() + "." + team_str + ".barriers").getKeys(false).stream().collect(Collectors.toList()));
        for (int i = 1; i < numbs.size() + 1; i++) {
            barrier1_x = getConfig().getInt("playworlds." + world.getName() + "." + team_str + ".barriers.barrier-" + i + ".1-X");
            barrier1_y = getConfig().getInt("playworlds." + world.getName() + "." + team_str + ".barriers.barrier-" + i + ".1-Y");
            barrier1_z = getConfig().getInt("playworlds." + world.getName() + "." + team_str + ".barriers.barrier-" + i + ".1-Z");
            barrier2_x = getConfig().getInt("playworlds." + world.getName() + "." + team_str + ".barriers.barrier-" + i + ".2-X");
            barrier2_y = getConfig().getInt("playworlds." + world.getName() + "." + team_str + ".barriers.barrier-" + i + ".2-Y");
            barrier2_z = getConfig().getInt("playworlds." + world.getName() + "." + team_str + ".barriers.barrier-" + i + ".2-Z");

            int topBlockX = (barrier1_x < barrier2_x ? barrier2_x : barrier1_x);
            int bottomBlockX = (barrier1_x > barrier2_x ? barrier2_x : barrier1_x);

            int topBlockY = (barrier1_y < barrier2_y ? barrier2_y : barrier1_y);
            int bottomBlockY = (barrier1_y > barrier2_y ? barrier2_y : barrier1_y);

            int topBlockZ = (barrier1_z < barrier2_z ? barrier2_z : barrier1_z);
            int bottomBlockZ = (barrier1_z > barrier2_z ? barrier2_z : barrier1_z);

            for(int x = bottomBlockX; x <= topBlockX; x++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                        locs.add(new Location(world, x, y, z));
                    }
                }
            }
        }

        return locs;
    }

    public boolean anonError() {
        reloadConfig();
        return getConfig().getBoolean("anonymous-error-messages");
    }

    public boolean silentError() {
        reloadConfig();
        return getConfig().getBoolean("silent-error-reports");
    }

    @Override
    public void onDisable() {
        try {
            if (thread.isRunning()) {
                thread.stahp();
            }

            if (sentry_thread.isRunning()) {
                sentry_thread.stahp();
            }
            running = false;

            for (Player player2 : Bukkit.getOnlinePlayers()) {
                ArenaManager.getManager().removePlayer(player2);
            }
            ArenaManager.getManager().clearArenas();
        } catch (Exception e) {
            new ExceptionReporter(e);
        }

        plugin = null;
    }

    public ArrayList<Location> getHealthPackLocs(World world) {
        reloadConfig();
        ArrayList<Location> locs = new ArrayList<>();
        ArrayList<String> numbs = new ArrayList<>();
        numbs.addAll(getConfig().getConfigurationSection("playworlds." + world.getName() + ".healths").getKeys(false).stream().collect(Collectors.toList()));
        for (int i = 0; i < numbs.size(); i++) {
            locs.add(new Location(world, getConfig().getDouble("playworlds." + world.getName() + ".healths.health-" + i + ".X"), getConfig().getDouble("playworlds." + world.getName() + ".healths.health-" + i + ".Y"), getConfig().getDouble("playworlds." + world.getName() + ".healths.health-" + i + ".Z")));
        }
        return locs;
    }

    public ArrayList<Location> getAmmoPackLocs(World world) {
        reloadConfig();
        ArrayList<Location> locs = new ArrayList<>();
        ArrayList<String> numbs = new ArrayList<>();
        numbs.addAll(getConfig().getConfigurationSection("playworlds." + world.getName() + ".ammos").getKeys(false).stream().collect(Collectors.toList()));
        for (int i = 0; i < numbs.size(); i++) {
            locs.add(new Location(world, getConfig().getDouble("playworlds." + world.getName() + ".ammos.ammo-" + i + ".X"), getConfig().getDouble("playworlds." + world.getName() + ".ammos.ammo-" + i + ".Y"), getConfig().getDouble("playworlds." + world.getName() + ".ammos.ammo-" + i + ".Z")));
        }
        return locs;
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
                            System.out.println("Coordinate given is not a refill door.");
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
                                        System.out.println("Coordinate given is not a refill door.");
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
                                        System.out.println("Coordinate given is not a refill door.");
                                    }
                                } else {
                                    System.out.println("Coordinate given is not a refill door.");
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
                                        System.out.println("Coordinate given is not a refill door.");
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
                                        System.out.println("Coordinate given is not a refill door.");
                                    }
                                } else {
                                    System.out.println("Coordinate given is not a refill door.");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Coordinate given is not a refill door.");
                }
                return list;
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
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
        try {
            return new Location(world, getConfig().getInt("playworlds." + world.getName() + ".spectate-X"), getConfig().getInt("playworlds." + world.getName() + ".spectate-Y"), getConfig().getInt("playworlds." + world.getName() + ".spectate-Z"));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public boolean isInBarrier(Location location, TeamEnum teamOfPlayer) {
        try {
            if (teamOfPlayer == TeamEnum.BLUE) {
                return red_barriers.contains(location.getBlock().getLocation());
            } else {
                return blue_barriers.contains(location.getBlock().getLocation());
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    HashMap<Player, Boolean> players = new HashMap<>();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            if (ArenaManager.getManager().isInGame(event.getPlayer())) {
                if (!players.containsKey(event.getPlayer())) {
                    players.put(event.getPlayer(), false);
                }
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
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    static ArrayList<Player> tochange = new ArrayList<>();
    public void doPlayerEffects() {
        try {
            new BukkitRunnable() {
                public void run() {
                    if (!running) {
                        cancel();
                    }
                    tochange = (ArrayList<Player>) dispenser_players.clone();
                    for (Player player : dispenser_players) {
                        if (Dispensers.isInRange(player)) {
                            if (!DeadMode.isInDeadMode(player)) {
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
                            }
                        } else {
                            tochange.remove(player);
                        }
                    }
                    dispenser_players = tochange;
                    tochange.clear();
                }
            }.runTaskTimer(Main.getPlugin(), 0, 40);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        try {
            if (ArenaManager.getManager().isInGame(event.getPlayer())) {
                if (event.getItem().getItemStack().getType() == Material.GOLD_SPADE) {
                    if (Healths.getItem(event.getItem()) != null) {
                        event.setCancelled(true);
                        Healths.getItem(event.getItem()).remove();
                        playerHealth.addHealth(event.getPlayer(), playerHealth.getMaxHealth(event.getPlayer()));
                        event.getPlayer().setFoodLevel(20);
                    }
                } else if (event.getItem().getItemStack().getType() == Material.IRON_SPADE) {
                    if (Ammos.getItem(event.getItem()) != null) {
                        event.setCancelled(true);
                        event.getItem().remove();
                        Ammos.getItem(event.getItem()).remove();
                        playerGuns.fillAll(event.getPlayer());
                    }
                } else if (event.getItem().getCustomName() != null) {
                    if (event.getItem().getCustomName().equals("GrenadeLauncher")) {
                        event.setCancelled(true);
                        event.getItem().remove();
                    } else if (event.getItem().getCustomName().equals("StickyBomb")) {
                        event.setCancelled(true);
                    }
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent e) {
        if(e.getEntityType() == EntityType.ARROW) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                try {
                    EntityArrow e1 = ((CraftArrow) e.getEntity()).getHandle();
                    Field fieldX = EntityArrow.class.getDeclaredField("h");
                    Field fieldY = EntityArrow.class.getDeclaredField("au");
                    Field fieldZ = EntityArrow.class.getDeclaredField("av");
                    fieldX.setAccessible(true);
                    fieldY.setAccessible(true);
                    fieldZ.setAccessible(true);
                    int x = fieldX.getInt(e1);
                    int y = fieldY.getInt(e1);
                    int z = fieldZ.getInt(e1);
                    if (isValidBlock(x, y, z)) {
                        Block block = e.getEntity().getWorld().getBlockAt(x, y, z);
                        Bukkit.getServer().getPluginManager().callEvent(new ArrowHitBlockEvent((Arrow) e.getEntity(), block));
                    }
                } catch (Throwable throwable) {
                    new ExceptionReporter(throwable);
                }

            });
        }

    }

    public void toggleDoorState(Block block) {
        try {
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
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
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
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mf2")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("start")) {
                    if (sender.hasPermission("mf2.start")) {
                        if (Game.getGameState() == GameState.NOTHING) {
                            sender.sendMessage(ChatColor.GOLD + "Starting game...");
                            TeamChooser chooser = new TeamChooser(plugin);
                            try {
                                chooser.sendPlayers();
                                ArenaManager.getManager().createArena();
                            } catch (Throwable throwable) {
                                new ExceptionReporter(throwable);
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "The game is already running!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: Insufficient permissions");
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (Game.getGameState() != GameState.NOTHING) {
                        sender.sendMessage(ChatColor.GOLD + "Stopping game...");
                        for (Player player2 : Bukkit.getOnlinePlayers()) {
                            ArenaManager.getManager().removePlayer(player2);
                        }
                        ArenaManager.getManager().clearArenas();
                        Game.stopGameState();
                        TeamChooser.setBothDoorTrue();
                    } else {
                        sender.sendMessage(ChatColor.RED + "The game must be running for you to do this!");
                    }
                } else if (args[0].equalsIgnoreCase("class")) {
                    if (sender.hasPermission("mf2.class")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                chooser.openGUI((Player) sender);
                            } else {
                                sender.sendMessage(ChatColor.RED + "This command is only to be used by players!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /mf2 loadouts remove <Player> <Class>");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: Insufficient permissions");
                    }
                } else if (args[0].equalsIgnoreCase("loadouts")) {
                    if (sender.hasPermission("mf2.loadouts")) {
                        if (args.length > 1) {
                            if (args[1].equalsIgnoreCase("remove")) {
                                if (args.length != 4) {
                                    sender.sendMessage(ChatColor.RED + "Usage: /mf2 loadouts remove <Player> <Class>");
                                } else {
                                    UUID uuid = Bukkit.getOfflinePlayer(args[2]).getUniqueId();
                                    if (loadouts.containsPlayer(uuid)) {
                                        if (args[3].equalsIgnoreCase("all")) {
                                            loadouts.deletePlayer(uuid);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "all" + ChatColor.GOLD + " classes from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("SCOUT")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.SCOUT);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "SCOUT" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("SOLDIER")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.SOLDIER);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "SOLDIER" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("PYRO")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.PYRO);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "PYRO" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("DEMOMAN")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.DEMOMAN);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "DEMOMAN" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("HEAVY")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.HEAVY);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "HEAVY" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("ENGINEER")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.ENGINEER);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "ENGINEER" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("MEDIC")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.MEDIC);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "MEDIC" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("SNIPER")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.SNIPER);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "SNIPER" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else if (args[3].equalsIgnoreCase("SPY")) {
                                            loadouts.deletePlayer(uuid, ClassEnum.SPY);
                                            sender.sendMessage(ChatColor.GOLD + "Deleted " + ChatColor.RED + "SPY" + ChatColor.GOLD + " class from " + ChatColor.RED + args[2] + ChatColor.GOLD + "'s loadout table");
                                        } else {
                                            sender.sendMessage(ChatColor.RED + "Invalid class. Valid classes: all, SCOUT, SOLDIER, PYRO, DEMOMAN, HEAVY, ENGINEER, MEDIC, SNIPER, SPY");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Error: Player is not found registered in the loadout database");
                                    }
                                }
                            } else if (args[1].equalsIgnoreCase("reset")) {
                                if (args.length == 3) {
                                    if (args[2].equalsIgnoreCase("confirm")) {
                                        if (loadout_reset_wait) {
                                            loadout_reset_wait = false;
                                            sender.sendMessage(ChatColor.GOLD + "Resetting player loadouts...");
                                            loadouts.reset();
                                            sender.sendMessage(ChatColor.GOLD + "Reset " + ChatColor.RED + "all" + ChatColor.GOLD + " player loadouts");
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "Usage: /mf2 loadouts <remove|reset|reload>");
                                    }
                                } else if (args.length == 2) {
                                    sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Are you SURE you want to reset ALL player's loadout tables? This can NOT be undone! Type " + ChatColor.ITALIC + "/mf2 loadout reset confirm" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + " to continue.");
                                    loadout_reset_wait = true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Usage: /mf2 loadouts reset");
                                }
                            } else if (args[1].equalsIgnoreCase("reload")) {
                                sender.sendMessage(ChatColor.GOLD + "Reloading player loadouts...");
                                loadouts.reload();
                                sender.sendMessage(ChatColor.GOLD + "Reloaded player loadouts");
                            } else {
                                sender.sendMessage(ChatColor.RED + "Usage: /mf2 loadouts <remove|reset|reload>");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /mf2 loadouts <remove|reset|reload>");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: Insufficient permissions");
                    }
                } else if (args[0].equalsIgnoreCase("config")) {
                    if (sender.hasPermission("mf2.config")) {
                        if (args.length > 1) {
                            if (args[1].equalsIgnoreCase("reload")) {
                                if (args.length == 2) {
                                    sender.sendMessage(ChatColor.GOLD + "Reloading config.yml for Mine Fortress 2 version " + getDescription().getVersion() + "...");
                                    reloadConfig();
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Usage: /mf2 config <reload|reset>");
                                }
                            } else if (args[1].equalsIgnoreCase("reset")) {
                                if (args.length == 3) {
                                    if (args[2].equalsIgnoreCase("confirm")) {
                                        if (config_reset_wait) {
                                            config_reset_wait = false;
                                            sender.sendMessage(ChatColor.GOLD + "Resetting config.yml for Mine Fortress 2 version " + getDescription().getVersion() + "...");
                                            sender.sendMessage(ChatColor.GOLD + "Downloading default config...");
                                            if (downloadConfig() == 404) {
                                                sender.sendMessage(ChatColor.RED + "Error: File not found! Please contact " + ChatColor.BOLD + "contact@uddernetworks.com" + ChatColor.RESET + ChatColor.RED + " and tell them to upload the file for version " + ChatColor.GOLD + getDescription().getVersion() + ChatColor.RED + ".");
                                            } else {
                                                sender.sendMessage(ChatColor.GOLD + "Successfully download and reset config.yml");
                                                reloadConfig();
                                            }
                                        } else {
                                            sender.sendMessage(ChatColor.RED + "Usage: /mf2 config <reload|reset>");
                                        }
                                    }
                                } else if (args.length == 2) {
                                    sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Are you SURE you want to reset your config.yml? This can NOT be undone! Type " + ChatColor.ITALIC + "/mf2 config reset confirm" + ChatColor.RESET + ChatColor.RED + ChatColor.BOLD + " to continue.");
                                    config_reset_wait = true;
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Usage: /mf2 config <reload|reset>");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Usage: /mf2 config <reload|reset>");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /mf2 config <reload|reset>");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /mf2 <start|stop|class|loadouts|config>");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /mf2 <start|stop|class|loadouts|config>");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /mf2 <start|stop|class|loadouts|config>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Error: Insufficient permissions");
        }
        return true;
    }

    public int downloadConfig() {
        try {
            URL url = new URL("http://uddernetworks.com/file_download/MF2%20configs/" + getDescription().getVersion() + ".yml");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            if (huc.getResponseCode() == 404) {
                return 404;
            } else {
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                FileOutputStream fis = new FileOutputStream(getPlugin().getDataFolder().getAbsolutePath() + File.separator + "config.yml");
                byte[] buffer = new byte[1024];
                int count;
                while ((count = bis.read(buffer, 0, 1024)) != -1) {
                    fis.write(buffer, 0, count);
                }
                fis.close();
                bis.close();
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return -1;
        }
        return 20;
    }


    private static final Class[] parameters = new Class[]{URL.class};

    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }

    public static void addFile(File f) throws IOException {
        addURL(f.toURL());
    }


    public static void addURL(URL u) throws IOException {

        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{u});
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }

    }


    private boolean isValidBlock(int x, int y, int z) {
        return x != -1 && y != -1 && z != -1;
    }

    public static Main getPlugin() {
        return plugin;
    }

}