package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.guns.PlayerMetal;
import com.uddernetworks.tf2.guns.dispenser.Dispenser;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.sentry.Sentry;
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

public class ConstructionChooser implements Listener {

    static private Inventory inv;

    private static Main main;
    public ConstructionChooser(Main main) {
        ConstructionChooser.main = main;
    }

    public void openGUI(Player p) {

        inv = Bukkit.createInventory(p, 27, "Choose what to create");

        if (!Sentries.hasSentry(p)) {
            createDisplay(Material.EMERALD_BLOCK, inv, 12, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Sentry (125 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "Click to create a sentry");
        } else {
            createDisplay(Material.CLAY, inv, 12, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Sentry (125 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "You already have a sentry");
        }
        if (!Dispensers.hasDispenser(p)) {
            createDisplay(Material.EMERALD_BLOCK, inv, 13, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Dispenser (100 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "Click to create a dispenser");
        } else {
            createDisplay(Material.CLAY, inv, 13, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Dispenser (100 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "You already have a dispenser");
        }
        if (!Teleporters.hasEntrance(p)) {
            createDisplay(Material.EMERALD_BLOCK, inv, 14, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Teleporter entrance (50 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "Click to create a teleporter entrance");
        } else {
            createDisplay(Material.CLAY, inv, 14, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Teleporter entrance (50 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "You already have a teleporter entrance");
        }
        if (!Teleporters.hasExit(p)) {
            createDisplay(Material.EMERALD_BLOCK, inv, 15, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Teleporter exit (50 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "Click to create a teleporter exit");
        } else {
            createDisplay(Material.CLAY, inv, 15, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GRAY + "Teleporter exit (50 metal)", ChatColor.RESET + "" + ChatColor.BOLD + "You already have a teleporter exit");
        }

        p.openInventory(inv);
    }

    public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(lore);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        inv.setItem(Slot, item);
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
                            if (!Sentries.hasSentry(player)) {
                                if (PlayerMetal.getPlayer(player) >= 125) {
                                    PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) - 125);
                                    Sentry sentry = new Sentry(player.getLocation(), player, 10);
                                    sentry.spawnSentry();
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have enough metal for that!");
                                }
                            } else {
                                break;
                            }
                            break;
                        case 13:
                            if (!Dispensers.hasDispenser(player)) {
                                if (PlayerMetal.getPlayer(player) >= 100) {
                                    PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) - 100);
                                    Dispenser dispenser = new Dispenser(player.getLocation(), player);
                                    dispenser.spawnDispenser();
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have enough metal for that!");
                                }
                            } else {
                                break;
                            }
                            break;
                        case 14:
                            if (!Teleporters.hasEntrance(player)) {
                                if (PlayerMetal.getPlayer(player) >= 50) {
                                    PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) - 50);
                                    TeleporterEntrance teleporterEntrance = new TeleporterEntrance(player.getLocation(), player);
                                    teleporterEntrance.spawnTeleporter();
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have enough metal for that!");
                                }
                            } else {
                                break;
                            }
                            break;
                        case 15:
                            if (!Teleporters.hasExit(player)) {
                                if (PlayerMetal.getPlayer(player) >= 50) {
                                    PlayerMetal.addPlayer(player, PlayerMetal.getPlayer(player) - 50);
                                    TeleporterExit teleporterExit = new TeleporterExit(player.getLocation(), player);
                                    teleporterExit.spawnTeleporter();
                                } else {
                                    player.sendMessage(ChatColor.RED + "You don't have enough metal for that!");
                                }
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
