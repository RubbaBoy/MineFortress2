package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.gui.BulletGUI;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import com.uddernetworks.tf2.utils.HashMap3;
import com.uddernetworks.tf2.utils.SQLLoadout;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlayerGuns {

    private static ArrayList<GunPersonalized> player_guns = new ArrayList<>();
    private static HashMap<Player, GunObject> curr_gun = new HashMap<>();

    private PlayerHealth playerHealth = new PlayerHealth();

    public void addPlayerGun(Player player, GunObject gun) {
        try {
            boolean used = false;
            for (GunPersonalized gunPersonalized : player_guns) {
                if (gunPersonalized.getPlayer() == player && gunPersonalized.getGun() == gun) {
                    used = true;
                }
            }

            if (!used) {
                GunPersonalized gunPersonalized = new GunPersonalized(player, gun);
                gunPersonalized.setAmmo(gun.getMaxAmmo());
                gunPersonalized.setClip(gun.getMaxClip());
                player_guns.add(gunPersonalized);
            }

            curr_gun.put(player, gun);

            playerHealth.addHealth(player, PlayerClasses.getPlayerClass(player).getHealth());

            ItemStack itemgun = new ItemStack(getPlayerGun(player).getItemStack());
            ItemMeta meta = itemgun.getItemMeta();
            meta.setDisplayName(getPlayerGun(player).getName());
            itemgun.setItemMeta(meta);
            player.getInventory().setItem(0, itemgun);
            if (gun.showGUI()) {
                new BulletGUI(player);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public GunObject getPlayerGun(Player player) {
        try {
            if (curr_gun.containsKey(player)) {
                return curr_gun.get(player);
            } else {
                for (int i = 0; i < GunList.getGunlist().size(); i++) {
                    GunObject gun = GunList.getGunAt(i);
                    if (player.getInventory().getItemInMainHand().toString().equals(gun.getItemStack().toString())) {
                        addPlayerGun(player, gun);
                        return gun;
                    }
                }
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public void setAmmo(Player player, int ammoNum) {
        try {
            GunPersonalized gunPersonalized2;
            int i = 0;
            for (GunPersonalized gunPersonalized : player_guns) {
                if (gunPersonalized.getGun() == getPlayerGun(player) && gunPersonalized.getPlayer() == player) {
                    gunPersonalized2 = gunPersonalized;
                    gunPersonalized2.setAmmo(ammoNum);
                    player_guns.set(i, gunPersonalized2);

                    if (getPlayerGun(player).showGUI()) {
                        new BulletGUI(player);
                    }
                }
                i++;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void setClip(Player player, int clipNum) {
        try {
            GunPersonalized gunPersonalized2;
            int i = 0;
            for (GunPersonalized gunPersonalized : player_guns) {
                if (gunPersonalized.getGun() == getPlayerGun(player) && gunPersonalized.getPlayer() == player) {
                    gunPersonalized2 = gunPersonalized;
                    gunPersonalized2.setClip(clipNum);
                    player_guns.set(i, gunPersonalized2);

                    if (getPlayerGun(player).showGUI()) {
                        new BulletGUI(player);
                    }
                }
                i++;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public int getAmmo(Player player) {
        try {
            for (GunPersonalized gunPersonalized : player_guns) {
                if (gunPersonalized.getPlayer() == player && gunPersonalized.getGun() == getPlayerGun(player)) {
                    return gunPersonalized.getAmmo();
                }
            }
            return 0;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public int getClip(Player player) {
        try {
            for (GunPersonalized gunPersonalized : player_guns) {
                if (gunPersonalized.getPlayer() == player && gunPersonalized.getGun() == getPlayerGun(player)) {
                    return gunPersonalized.getClip();
                }
            }
            return 0;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public int getMaxAmmo(Player player) {
        try {
            return getPlayerGun(player).getMaxAmmo();
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public int getMaxClip(Player player) {
        try {
            return getPlayerGun(player).getMaxClip();
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return 0;
        }
    }

    public void reloadGun(Player player) {
        try {
            if (getAmmo(player) - getClip(player) > 0) {
                if (getClip(player) <= getAmmo(player)) {
                    setAmmo(player, getAmmo(player) - (getPlayerGun(player).getMaxClip() - getClip(player)));
                    setClip(player, getPlayerGun(player).getMaxClip() - getClip(player));
                } else {
                    setClip(player, getAmmo(player));
                    setAmmo(player, 0);
                }
                if (getPlayerGun(player).showGUI()) {
                    new BulletGUI(player);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void fillAll(Player player) {
        try {
            player_guns.stream().filter(gunPersonalized -> gunPersonalized.getPlayer() == player).forEach(gunPersonalized -> {
                gunPersonalized.setAmmo(gunPersonalized.getGun().getMaxAmmo());
                gunPersonalized.setClip(gunPersonalized.getGun().getMaxClip());
            });
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}
