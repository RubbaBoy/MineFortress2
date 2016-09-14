package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.SQLLoadout;
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

public class Loadout implements Listener {

    static private Inventory inv;

    private static Main main;

    public Loadout(Main main) {
        Loadout.main = main;
    }

    public void openGUI(Player p) {

        inv = Bukkit.createInventory(p, 54, "Loadout");

        int slot;
        SQLLoadout sqlLoadout = new SQLLoadout(main);
        ArrayList<GunObject> loadout = sqlLoadout.getPlayerLoadout(p);
        System.out.println("Player loadout is: " + loadout);
        for (int i = 0; i < loadout.size(); i++) {
            GunObject gun = loadout.get(i);
            switch (i) {
                case 0:
                    slot = 10;
                    break;
                case 1:
                    slot = 19;
                    break;
                case 2:
                    slot = 28;
                    break;
                case 3:
                    slot = 37;
                    break;
                case 4:
                    slot = 46;
                    break;
                default:
                    slot = 0;
                    break;
            }

            createDisplay(gun.getItemStack().getType(), inv, slot, ChatColor.RESET + gun.getName(), ChatColor.RESET + gun.getLore());
        }

        createDisplay(Material.BARRIER, inv, 16, ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Cosmetics not yet implemented", ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Check back another time, they may be implemented sometime");
        createDisplay(Material.BARRIER, inv, 25, ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Cosmetics not yet implemented", ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Check back another time, they may be implemented sometime");
        createDisplay(Material.BARRIER, inv, 34, ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Cosmetics not yet implemented", ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Check back another time, they may be implemented sometime");
        createDisplay(Material.BARRIER, inv, 43, ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Cosmetics not yet implemented", ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Check back another time, they may be implemented sometime");

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
                EditLoadout gunlist = new EditLoadout(main);

                switch (event.getSlot()) {
                    case 10:
                        gunlist.openGUI(player, 0);
                        break;
                    case 19:
                        gunlist.openGUI(player, 1);
                        break;
                    case 28:
                        gunlist.openGUI(player, 2);
                        break;
                    case 37:
                        gunlist.openGUI(player, 3);
                        break;
                }

            }
        } catch (Exception ignored) {}

    }

    @EventHandler
    public void onWindowClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        try {
            PlayerGuns playerGuns = new PlayerGuns();
            SQLLoadout sqlLoadout = new SQLLoadout(main);
            playerGuns.addPlayerGun(player, sqlLoadout.getPlayerLoadout(player).get(0));
        } catch (Exception ignored) {}
    }
}
