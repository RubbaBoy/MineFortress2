package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.inv.Anvil;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AnvilChooser {

    private static HashMap3<Player, Throwable, Integer> players = new HashMap3<>();
    private static HashMap3<Player, String, String> player_data = new HashMap3<>();

    public AnvilChooser(Player player, String data) {
        if (Main.getPlugin().silentError()) {

            String message = "";
            message += "\nos.arch: " + System.getProperty("os.arch") + "\n";
            message += "os.name: " + System.getProperty("os.name") + "\n";
            message += "os.version: " + System.getProperty("os.version") + "\n";

            message += "Server version: " + Bukkit.getVersion() + "\n";

            for (int i = 0; i < players.get(player).getStackTrace().length; i++) {
                message += "\n" + players.get(player).getStackTrace()[i];
            }
            message += "\n\n=====Plugins=====\n";
            Plugin[] plugins = Bukkit.getServer().getPluginManager().getPlugins();
            for (Plugin plugin : plugins) {
                message += plugin.getDescription().getFullName() + " - " + plugin.getDescription().getVersion() + "\n";
            }
            message += "=====End of Plugins=====\n";
            new ErrorHandler(player, "anonymous@error.com", player_data.getT(player), message);

        } else if (!Main.getPlugin().anonError() && players.getT(player) == 0) {
            if (!players.containsKey(player)) {
                players.put(player, null);
            }
            player_data.put(player, data);
            players.setT(player, 1);
            Anvil anvil = new Anvil();
            anvil.openAnvilInventory(player, null);
        } else if (Main.getPlugin().anonError() || players.getT(player) == 1) {
            player_data.setT(player, data);
            String message = "";
            message += "\nos.arch: " + System.getProperty("os.arch") + "\n";
            message += "os.name: " + System.getProperty("os.name") + "\n";
            message += "os.version: " + System.getProperty("os.version") + "\n";

            message += "Server version: " + Bukkit.getVersion() + "\n";

            for (int i = 0; i < players.get(player).getStackTrace().length; i++) {
                message += "\n" + players.get(player).getStackTrace()[i];
            }
            message += "\n\n=====Plugins=====\n";
            Plugin[] plugins = Bukkit.getServer().getPluginManager().getPlugins();
            for (Plugin plugin : plugins) {
                message += plugin.getDescription().getFullName() + " - " + plugin.getDescription().getVersion() + "\n";
            }
            message += "=====End of Plugins=====\n";
            new ErrorHandler(player, "anonymous@error.com", player_data.getT(player), message);
        } else {
            if (!players.containsKey(player)) {
                players.put(player, null);
                player_data.put(player, data);
                players.setT(player, 1);
                Anvil anvil = new Anvil();
                anvil.openAnvilInventory(player, null);
            }
        }
    }

    public static int getPlayer(Player player) {
        if (players.containsKey(player)) {
            return players.getT(player);
        } else {
            players.put(player, null);
            players.setT(player, 0);
            return 0;
        }
    }

    public static void addPlayer(Player player, Throwable throwable) {
        players.put(player, throwable);
        players.setT(player, 0);

        player_data.put(player, "");
        player_data.setT(player, "");
    }

    public static void setThrow(Player player, Throwable throwable) {
        players.put(player, throwable);
    }

    public static void removePlayer(Player player) {
        if (players.containsKey(player)) {
            players.remove(player);
            player_data.remove(player);
        }
    }

    public static void clear(Player player) {
        players.remove(player);
        player_data.remove(player);
    }
}
