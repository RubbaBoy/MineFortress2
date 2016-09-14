package com.uddernetworks.tf2.utils.data;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.HashMap3;
import com.uddernetworks.tf2.utils.SQLLoadout;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSlots {

    private static HashMap3<Player, Integer, ArrayList<GunObject>> playerslots = new HashMap3<>();
    private static HashMap<Player, Boolean> runsecond = new HashMap<>();
    private static Main main;

    public PlayerSlots(Main main) {
        PlayerSlots.main = main;
    }

    public void addPlayer(Player player, int newSelected) {
        try {
            if (!playerslots.containsKey(player) || !runsecond.containsKey(player)) {
                SQLLoadout sqlLoadout = new SQLLoadout(main);
                playerslots.put(player, 0);
                playerslots.setT(player, sqlLoadout.getPlayerLoadout(player));
                runsecond.put(player, false);
            }
            if (!runsecond.get(player)) {
                runsecond.put(player, true);
                int selected = 0;
                if (newSelected == 1) {
                    selected = playerslots.get(player) + 1;
                } else if (newSelected == 8) {
                    selected = playerslots.get(player) - 1;
                }

                if (selected < 0) {
                    selected = playerslots.getT(player).size() - 1;
                } else if (selected > playerslots.getT(player).size() - 1) {
                    selected = 0;
                }

                playerslots.put(player, selected);
                PlayerGuns playerGuns = new PlayerGuns();
                if (playerslots.getT(player).get(selected).showGUI()) {
                    playerGuns.addPlayerGun(player, playerslots.getT(player).get(selected));
                } else {
                    playerGuns.addPlayerGun(player, playerslots.getT(player).get(selected));
                    player.getInventory().setItem(2, new ItemStack(Material.AIR, 1));
                    player.getInventory().setItem(4, new ItemStack(Material.AIR, 1));
                    player.getInventory().setItem(6, new ItemStack(Material.AIR, 1));
                    player.getInventory().setItem(7, new ItemStack(Material.AIR, 1));
                    player.getInventory().setItem(8, new ItemStack(Material.AIR, 1));
                }
            } else {
                runsecond.put(player, false);
            }
        } catch (NullPointerException ignored) {}
    }

    public boolean contains(Player player) {
        return playerslots.containsKey(player);
    }

    public int getSelectedSlot(Player player) {
        if (playerslots.containsKey(player)) {
            return playerslots.get(player);
        } else {
            SQLLoadout sqlLoadout = new SQLLoadout(main);
            playerslots.put(player, 0);
            playerslots.setT(player, sqlLoadout.getPlayerLoadout(player));
            return 0;
        }
    }

    public void rawAdd(Player player, int slot) {
        if (playerslots.containsKey(player)) {
            playerslots.put(player, slot);
        }
    }

    public ArrayList<GunObject> getPlayerGuns(Player player) {
        if (playerslots.containsKey(player)) {
            return playerslots.getT(player);
        } else {
            System.out.println("Trying to call for a player that is not registered in player slots");
            return null;
        }
    }

    public HashMap3<Player, Integer, ArrayList<GunObject>> getHashMap() {
        return playerslots;
    }

}
