package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.guns.Gun;
import com.uddernetworks.tf2.guns.GunList;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.ClassEnum;
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
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ClassChooser implements Listener {

    static private Inventory inv;

    public void openGUI(Player p) {

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
    }

    public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
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
                AdminGunList gunlist = new AdminGunList();

                switch (event.getSlot()) {
                    case 9:
                        PlayerClasses.setPlayerClass(player, ClassEnum.SCOUT);
                        gunlist.openGUI(player);
                        break;
                    case 10:
                        PlayerClasses.setPlayerClass(player, ClassEnum.SOLDIER);
                        gunlist.openGUI(player);
                        break;
                    case 11:
                        PlayerClasses.setPlayerClass(player, ClassEnum.PYRO);
                        gunlist.openGUI(player);
                        break;
                    case 12:
                        PlayerClasses.setPlayerClass(player, ClassEnum.DEMOMAN);
                        gunlist.openGUI(player);
                        break;
                    case 13:
                        PlayerClasses.setPlayerClass(player, ClassEnum.HEAVY);
                        gunlist.openGUI(player);
                        break;
                    case 14:
                        PlayerClasses.setPlayerClass(player, ClassEnum.ENGINEER);
                        gunlist.openGUI(player);
                        break;
                    case 15:
                        PlayerClasses.setPlayerClass(player, ClassEnum.MEDIC);
                        gunlist.openGUI(player);
                        break;
                    case 16:
                        PlayerClasses.setPlayerClass(player, ClassEnum.SNIPER);
                        gunlist.openGUI(player);
                        break;
                    case 17:
                        PlayerClasses.setPlayerClass(player, ClassEnum.SPY);
                        gunlist.openGUI(player);
                        break;
                }

            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

}