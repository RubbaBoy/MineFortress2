package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.dispenser.Dispenser;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.teleporter.TeleporterEntrance;
import com.uddernetworks.tf2.guns.teleporter.TeleporterExit;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class DestructionChooser implements Listener {

    static private Inventory inv;

    private static Main main;

    public DestructionChooser(Main main) {
        DestructionChooser.main = main;
    }

    public void openGUI(Player p) {
        try {

            inv = Bukkit.createInventory(p, 27, "Choose what to remove");

            if (Sentries.hasSentry(p)) {
                createDisplay(Material.EMERALD_BLOCK, inv, 12, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Sentry", ChatColor.RESET + "" + ChatColor.BOLD + "Click to remove your sentry");
            } else {
                createDisplay(Material.CLAY, inv, 12, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Sentry", ChatColor.RESET + "" + ChatColor.BOLD + "You don't have a sentry");
            }
            if (Dispensers.hasDispenser(p)) {
                createDisplay(Material.EMERALD_BLOCK, inv, 13, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Dispenser", ChatColor.RESET + "" + ChatColor.BOLD + "Click to remove your dispenser");
            } else {
                createDisplay(Material.CLAY, inv, 13, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Dispenser", ChatColor.RESET + "" + ChatColor.BOLD + "You don't have a dispenser");
            }
            if (Teleporters.hasEntrance(p)) {
                createDisplay(Material.EMERALD_BLOCK, inv, 14, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Teleporter entrance", ChatColor.RESET + "" + ChatColor.BOLD + "Click to remove your teleporter entrance");
            } else {
                createDisplay(Material.CLAY, inv, 14, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Teleporter entrance", ChatColor.RESET + "" + ChatColor.BOLD + "You don't have a teleporter entrance");
            }
            if (Teleporters.hasExit(p)) {
                createDisplay(Material.EMERALD_BLOCK, inv, 15, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Teleporter exit", ChatColor.RESET + "" + ChatColor.BOLD + "Click to remove your teleporter exit");
            } else {
                createDisplay(Material.CLAY, inv, 15, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Teleporter exit", ChatColor.RESET + "" + ChatColor.BOLD + "You don't have a teleporter exit");
            }

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
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack clicked = event.getCurrentItem();
        try {
            if (inventory.getName().equals(inv.getName())) {
                event.setCancelled(true);

                if (clicked.getType() == Material.EMERALD_BLOCK) {
                    switch (event.getSlot()) {
                        case 12:
                            if (Sentries.hasSentry(player)) {
                                Sentries.getSentry(player).remove();
                            } else {
                                break;
                            }
                            break;
                        case 13:
                            if (Dispensers.hasDispenser(player)) {
                                Dispensers.removeDispenserBy(player);
                            } else {
                                break;
                            }
                            break;
                        case 14:
                            if (Teleporters.hasEntrance(player)) {
                                Teleporters.removeEntranceBy(player);
                            } else {
                                break;
                            }
                            break;
                        case 15:
                            if (Teleporters.hasExit(player)) {
                                Teleporters.removeExitBy(player);
                            } else {
                                break;
                            }
                            break;
                    }
                }
            }
        } catch (Exception ignored) {}
    }

}