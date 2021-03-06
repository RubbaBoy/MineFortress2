package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.*;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.data.PlayerSlots;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ClassChooser implements Listener {

    static private Inventory inv;
    PlayerHealth playerHealth = new PlayerHealth();

    private static Main main;

    public ClassChooser(Main main) {
        ClassChooser.main = main;
    }

    public void openGUI(Player p) {
        try {

            inv = Bukkit.createInventory(p, 27, "Choose your class");

            createDisplay(Material.EMERALD_BLOCK, inv, 9, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Scout", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the scout class");
            createDisplay(Material.EMERALD_BLOCK, inv, 10, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Soldier", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the soldier class");
            createDisplay(Material.EMERALD_BLOCK, inv, 11, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Pyro", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the pyro class");
            createDisplay(Material.EMERALD_BLOCK, inv, 12, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Demoman", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the demoman class");
            createDisplay(Material.EMERALD_BLOCK, inv, 13, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Heavy", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the Heavy class");
            createDisplay(Material.EMERALD_BLOCK, inv, 14, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Engineer", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the engineer class");
            createDisplay(Material.EMERALD_BLOCK, inv, 15, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Medic", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the medic class");
            createDisplay(Material.EMERALD_BLOCK, inv, 16, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Sniper", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the sniper class");
            createDisplay(Material.EMERALD_BLOCK, inv, 17, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Spy", ChatColor.RESET + "" + ChatColor.BOLD + "Click to choose the spy class");

            p.openInventory(inv);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
        try {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            ArrayList<String> Lore = new ArrayList<>();
            Lore.add(lore);
            meta.setLore(Lore);
            item.setItemMeta(meta);
            inv.setItem(Slot, item);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();
            if (inventory.getName().equals(inv.getName())) {
                event.setCancelled(true);
                Loadout loadout = new Loadout(main);
                switch (event.getSlot()) {
                    case 9:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.SCOUT);
                        loadout.openGUI(player);
                        break;
                    case 10:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.SOLDIER);
                        loadout.openGUI(player);
                        break;
                    case 11:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.PYRO);
                        loadout.openGUI(player);
                        break;
                    case 12:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.DEMOMAN);
                        loadout.openGUI(player);
                        break;
                    case 13:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.HEAVY);
                        loadout.openGUI(player);
                        break;
                    case 14:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.ENGINEER);
                        PlayerMetal.addPlayer(player, 200);
                        loadout.openGUI(player);
                        break;
                    case 15:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.MEDIC);
                        loadout.openGUI(player);
                        break;
                    case 16:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.SNIPER);
                        loadout.openGUI(player);
                        break;
                    case 17:
                        PlayerSlots.clearPlayer(player);
                        clearEngi(player);
                        PlayerClasses.setPlayerClass(player, ClassEnum.SPY);
                        loadout.openGUI(player);
                        break;
                }
                playerHealth.addPlayer(player, PlayerClasses.getPlayerClass(player).getHealth());
            }
        } catch (Exception ignored) {}
    }

    private void clearEngi(Player player) {
        if (PlayerClasses.getPlayerClass(player) != null && PlayerClasses.getPlayerClass(player) == ClassEnum.ENGINEER) {
            Sentries.removeSentryBy(player);
            Dispensers.removeDispenserBy(player);
            Teleporters.removeEntranceBy(player);
            Teleporters.removeExitBy(player);
        }
    }

    @EventHandler
    public void onWindowClose(InventoryCloseEvent event) {
        try {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();
            if (inventory.getName().equals(inv.getName())) {
                if (!PlayerClasses.isSet(player)) {
                    main.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> openGUI(player), 1L);
                }
            }
        } catch (Exception ignored) {}
    }

}