package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.guns.Gun;
import com.uddernetworks.tf2.guns.GunList;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
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

public class AdminGunList implements Listener {

    private PlayerGuns playerGuns = new PlayerGuns();

    private static int pageId = 1;

    private static int maxPage = 1;

    private static LinkedHashMap<String, GunObject> gun_slot = new LinkedHashMap<>();

    static private Inventory inv;

    public void openGUI(Player p) {

        int i_ = 0;
        for (int i = 0; i < GunList.getGunlist().size(); i++) {
            GunObject gun = GunList.getGunAt(i);
            gun_slot.put(gun.getItemStack().serialize().toString(), gun);
            System.out.println("Putting in gun: " + gun.getItemStack().serialize().toString());
            i_++;
        }

        this.maxPage = (((gun_slot.size() / 45) + ((gun_slot.size() % 45 == 0) ? 0 : 1)) * 45) / 45;

        inv = Bukkit.createInventory(p, 54, "Admin Gun List «" + pageId + "/" + maxPage + "»");

        if (gun_slot.size() <= 45) {
            for (int i = 0; i < gun_slot.size(); i++) {
                createDisplay(new ArrayList<GunObject>(gun_slot.values()).get(i).getItemStack().getType(), inv, i, (new ArrayList<GunObject>(gun_slot.values())).get(i).getName(), (new ArrayList<GunObject>(gun_slot.values())).get(i).getLore());
            }
        } else {

            if (pageId * 45 > gun_slot.size()) {
                for (int i = 0; i < gun_slot.size() - (((pageId - 1) * 45)); i++) {
                    createDisplay(new ArrayList<GunObject>(gun_slot.values()).get(i + ((pageId - 1) * 45)).getItemStack().getType(), inv, i, (new ArrayList<GunObject>(gun_slot.values())).get(i + ((pageId - 1) * 45)).getName(), (new ArrayList<GunObject>(gun_slot.values())).get(i + ((pageId - 1) * 45)).getLore());
                }
            } else {
                for (int i = 0; i < 45; i++) {
                    createDisplay(new ArrayList<GunObject>(gun_slot.values()).get((pageId - 1) * 45).getItemStack().getType(), inv, i, (new ArrayList<GunObject>(gun_slot.values())).get((pageId - 1) * 45).getName(), (new ArrayList<GunObject>(gun_slot.values())).get((pageId - 1) * 45).getLore());
                }
            }
        }

        createDisplay(Material.REDSTONE_BLOCK, inv, 45, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RED + "Back", ChatColor.RESET + "" + ChatColor.BOLD + "Go back a page or exit the menu");

        createDisplay(Material.EMERALD_BLOCK, inv, 53, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Next", ChatColor.RESET + "" + ChatColor.BOLD + "Go to the next page");

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

                if (event.getSlot() == 45) {
                    if (pageId <= 1) {
                        player.closeInventory();
                    } else {
                        pageId -= 1;

                        openGUI(player);
                    }
                } else if (event.getSlot() == 53) {
                    if (gun_slot.size() > 45) {
                        if (pageId < maxPage) {
                            pageId += 1;

                            openGUI(player);
                        }
                    }
                } else if (event.getSlot() <= 45) {
                    if (gun_slot.containsKey(clicked.serialize().toString())) {
                        playerGuns.addPlayerGun(player, gun_slot.get(clicked.serialize().toString()));
                    }
                }

            }
        } catch (Exception ignored) {}
    }

}