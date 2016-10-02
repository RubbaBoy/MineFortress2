package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.GunList;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.SQLLoadout;
import com.uddernetworks.tf2.utils.WeaponType;
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
import java.util.LinkedHashMap;

public class EditLoadout implements Listener {

    static private Inventory inv;

    private static int pageId = 1;

    private static int maxPage = 1;

    private static LinkedHashMap<String, GunObject> gun_slot = new LinkedHashMap<>();

    private static Main main;

    private static int idToEdit = 0;

    public EditLoadout(Main main) {
        EditLoadout.main = main;
    }

    public void openGUI(Player p, int idToEdit) {
        try {

            this.idToEdit = idToEdit;

            int i_ = 0;
            for (int i = 0; i < GunList.getGunsOfType(PlayerClasses.getPlayerClass(p)).size(); i++) {
                GunObject gun = GunList.getGunsOfType(PlayerClasses.getPlayerClass(p)).get(i);
                WeaponType type = WeaponType.NULL;
                if (idToEdit == 0) {
                    type = WeaponType.PRIMARY;
                } else if (idToEdit == 1) {
                    type = WeaponType.SECONDARY;
                } else if (idToEdit == 2) {
                    type = WeaponType.MELEE;
                } else if (idToEdit == 3) {
                    type = WeaponType.PDA;
                }
                if (gun.getType() == type) {
                    gun_slot.put(gun.getItemStack().serialize().toString(), gun);
                }
                i_++;
            }

            this.maxPage = (((gun_slot.size() / 45) + ((gun_slot.size() % 45 == 0) ? 0 : 1)) * 45) / 45;

            inv = Bukkit.createInventory(p, 54, "Choose a weapon «" + pageId + "/" + maxPage + "»");

            if (gun_slot.size() <= 45) {
                for (int i = 0; i < gun_slot.size(); i++) {
                    createDisplay(new ArrayList<>(gun_slot.values()).get(i).getItemStack().getType(), inv, i, (new ArrayList<>(gun_slot.values())).get(i).getName(), (new ArrayList<>(gun_slot.values())).get(i).getLore());
                }
            } else {

                if (pageId * 45 > gun_slot.size()) {
                    for (int i = 0; i < gun_slot.size() - (((pageId - 1) * 45)); i++) {
                        createDisplay(new ArrayList<>(gun_slot.values()).get(i + ((pageId - 1) * 45)).getItemStack().getType(), inv, i, (new ArrayList<>(gun_slot.values())).get(i + ((pageId - 1) * 45)).getName(), (new ArrayList<>(gun_slot.values())).get(i + ((pageId - 1) * 45)).getLore());
                    }
                } else {
                    for (int i = 0; i < 45; i++) {
                        createDisplay(new ArrayList<>(gun_slot.values()).get((pageId - 1) * 45).getItemStack().getType(), inv, i, (new ArrayList<>(gun_slot.values())).get((pageId - 1) * 45).getName(), (new ArrayList<>(gun_slot.values())).get((pageId - 1) * 45).getLore());
                    }
                }
            }

            createDisplay(Material.REDSTONE_BLOCK, inv, 45, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.RED + "Back", ChatColor.RESET + "" + ChatColor.BOLD + "Go back a page or exit the menu");

            createDisplay(Material.EMERALD_BLOCK, inv, 53, ChatColor.RESET + "" + ChatColor.BOLD + "" + ChatColor.GREEN + "Next", ChatColor.RESET + "" + ChatColor.BOLD + "Go to the next page");

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
            ArrayList<String> Lore = new ArrayList<String>();
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

                if (event.getSlot() == 45) {
                    if (pageId <= 1) {
                        player.closeInventory();
                    } else {
                        pageId -= 1;

                        openGUI(player, idToEdit);
                    }
                } else if (event.getSlot() == 53) {
                    if (gun_slot.size() > 45) {
                        if (pageId < maxPage) {
                            pageId += 1;

                            openGUI(player, idToEdit);
                        }
                    }
                } else if (event.getSlot() <= 45) {
                    if (gun_slot.containsKey(clicked.serialize().toString())) {
                        SQLLoadout sqlLoadout = new SQLLoadout(main);
                        sqlLoadout.setPlayerLoadout(player, gun_slot.get(clicked.serialize().toString()), idToEdit);
                    }
                }

            }
        } catch (Exception ignored) {}
    }
}