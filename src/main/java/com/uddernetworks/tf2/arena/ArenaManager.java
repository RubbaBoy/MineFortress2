package com.uddernetworks.tf2.arena;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArenaManager {

    static Main plugin;

    private static ArenaManager am;
    private TeamChooser teamChooser = new TeamChooser(plugin);

    private final Map<UUID, Location> locs = new HashMap<>();
    private final Map<UUID, ItemStack[]> inv = new HashMap<>();
    private final Map<UUID, ItemStack[]> armor = new HashMap<>();

    private final List<Arena> arenas = new ArrayList<>();
    private int arenaSize = 0;

    public ArenaManager(Main main) {
        plugin = main;
    }

    public static ArenaManager getManager() {
        if (am == null)
            am = new ArenaManager(plugin);

        return am;
    }

    public Arena getArena(){
        for (Arena a : this.arenas) {
            if (a.getId() == 1) {
                return a;
            }
        }

        return null;
    }

    public void addPlayer(Player p) {
        try {
            Arena a = this.getArena();
            if (a == null) {
                p.sendMessage("Invalid arena!");
                return;
            }

            if (this.isInGame(p)) {
                p.sendMessage("Cannot join more than 1 game!");
                return;
            }

            a.getPlayers().add(p.getUniqueId());

            inv.put(p.getUniqueId(), p.getInventory().getContents());
            armor.put(p.getUniqueId(), p.getInventory().getArmorContents());

            p.getInventory().setArmorContents(null);
            p.getInventory().clear();

            p.setGameMode(GameMode.SURVIVAL);

            locs.put(p.getUniqueId(), p.getLocation());

            p.setSaturation(Integer.MAX_VALUE);

            p.setFlying(false);
            p.setFoodLevel(20);

            Random random = new Random();
            Game game = new Game(plugin);

            try {
                p.teleport(plugin.getSpawnBlocks(game.getWorld(), PlayerTeams.getPlayer(p)).get(random.nextInt(plugin.getSpawnBlocks(p.getWorld(), PlayerTeams.getPlayer(p)).size())));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void removePlayer(Player p) {
        try {
            Arena a = null;

            for (Arena arena : this.arenas) {
                if (arena.getPlayers().contains(p.getUniqueId()))
                    a = arena;
            }

            if (a == null) {
                return;
            }

            a.getPlayers().remove(p.getUniqueId());

            p.getActivePotionEffects().clear();
            p.setSaturation(5);

            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            p.getInventory().setContents(inv.get(p.getUniqueId()));
            p.getInventory().setArmorContents(armor.get(p.getUniqueId()));

            inv.remove(p.getUniqueId());
            armor.remove(p.getUniqueId());

            teamChooser.sendBack(p);
            locs.remove(p.getUniqueId());

            p.setGameMode(GameMode.SURVIVAL);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public Arena createArena() {
        this.arenaSize++;

        Arena a = new Arena(this.arenaSize);
        this.arenas.add(a);

        return a;
    }


    public void clearArenas() {
        this.arenas.clear();
        this.arenaSize = 0;
        this.locs.clear();
        this.inv.clear();
        this.armor.clear();

        Sentries.removeAll();
        Dispensers.removeAll();
        Teleporters.removeAll();
    }

    public boolean isInGame(Player p) {
        for (Arena a : this.arenas) {
            if (a.getPlayers().contains(p.getUniqueId()))
                return true;
        }
        return false;
    }

    public String serializeLoc(Location l){
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public Location deserializeLoc(String s){
        String[] st = s.split(",");
        return new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3]));
    }

}
