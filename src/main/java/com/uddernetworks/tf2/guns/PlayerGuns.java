package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.gui.BulletGUI;
import com.uddernetworks.tf2.utils.HashMap3;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class PlayerGuns {

    static int HEALTH_NEED_CHANGE = 100;

    static HashMap<Player, GunObject> guns = new HashMap<>();
    static HashMap3<Player, Integer, Integer> ammo = new HashMap3<>();

    private PlayerHealth playerHealth = new PlayerHealth();

    public void addPlayerGun(Player player, GunObject gun) {

        try {
            guns.put(player, gun);
            ammo.put(player, 0);
            ammo.setT(player, 0);
            setClip(player, gun.getMaxClip());
            setAmmo(player, gun.getMaxAmmo());

            playerHealth.addPlayer(player, HEALTH_NEED_CHANGE);

            ItemStack itemgun = new ItemStack(guns.get(player).getItemStack());
            ItemMeta meta = itemgun.getItemMeta();
            meta.setDisplayName(guns.get(player).getName());
            itemgun.setItemMeta(meta);
            player.getInventory().setItem(0, itemgun);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GunObject getPlayerGun(Player player) {
        if (guns.containsKey(player)) {
            return guns.get(player);
        } else {
            return null;
        }
    }

    public void setAmmo(Player player, int ammoNum) {
        ammo.put(player, ammoNum);
        new BulletGUI(player);
    }

    public void setClip(Player player, int clipNum) {
        ammo.setT(player, clipNum);
        new BulletGUI(player);
    }

    public int getAmmo(Player player) {
        if (ammo.containsKey(player)) {
            return ammo.get(player);
        } else {
            int temp_return = 0;
            for (int i = 0; i < GunList.getGunlist().size(); i++) {
                GunObject gun = GunList.getGunAt(i);
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    addPlayerGun(player, gun);
                    temp_return = ammo.get(player);
                }
            }
            return temp_return;
        }
    }

    public int getClip(Player player) {
        if (ammo.containsKey(player)) {
            return ammo.getT(player);
        } else {
            int temp_return = 0;
            for (int i = 0; i < GunList.getGunlist().size(); i++) {
                GunObject gun = GunList.getGunAt(i);
                if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                    addPlayerGun(player, gun);
                    temp_return = ammo.getT(player);
                }
            }
            return temp_return;
        }
    }

    public void reloadGun(Player player) {
        if (getAmmo(player) - getClip(player) > 0) {
            if (getClip(player) <= getAmmo(player)) {
                setAmmo(player, getAmmo(player) - (guns.get(player).getMaxClip() - getClip(player)));
                setClip(player, guns.get(player).getMaxClip() - getClip(player));
            } else {
                setClip(player, getAmmo(player));
                setAmmo(player, 0);
            }
            new BulletGUI(player);
        }
    }

}
