package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.GunList;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class SQLLoadout {

    //        [PlayerName]@[Class]  Gun                   Cosmetic (Not yet supported)
    private static HashMap3<String, ArrayList<GunObject>, ArrayList<ItemStack>> loadouts = new HashMap3<>();

    private Main main;
    private MySQL mySQL;

    public SQLLoadout(Main main) {
        try {
            this.main = main;
            mySQL = new MySQL(this.main);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void reload() {
        System.out.println("Reloading internal data set of loadouts");
        try {
            QueryResults results = mySQL.queryReturnable("SELECT * FROM 'Loadouts' ORDER BY ID DESC");

            for (QueryResult result : results.getList()) {
                String UUID = result.getUUID();
                String uuid_ = UUID.substring(0, UUID.indexOf("@"));
                GunObject gun = GunList.getGunByName(result.getNAME());
                if (gun != null) {
                    ArrayList<GunObject> gunObjects = new ArrayList<>();
                    if (loadouts.get(UUID) != null) gunObjects = new ArrayList<>(loadouts.get(UUID));
                    gunObjects.add(gun);
                    System.out.println("Adding for player: " + UUID + " (" + Bukkit.getOfflinePlayer(uuid_).getName() + ")");
                    loadouts.put(UUID, gunObjects);
                } else {
                    System.out.println("A gun from " + Bukkit.getOfflinePlayer(uuid_).getName() + " was not found, and most likely changed from last restart/reload. Deleting gun from player's class to prevent errors...");
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void reset() {
        mySQL.query("DELETE FROM Loadouts");
        reload();
    }

    public void deletePlayer(UUID uuid) {
        for (ClassEnum classEnum : ClassEnum.values()) {
            mySQL.query("DELETE FROM Loadouts WHERE UUID='" + uuid.toString() + "@" + classEnum.toString() + "' AND Class='" + classEnum.toString() + "'");
        }
    }

    public void deletePlayer(UUID uuid, ClassEnum classEnum) {
        mySQL.query("DELETE FROM Loadouts WHERE UUID='" + uuid.toString() + "@" + classEnum.toString() + "' AND Class='" + classEnum.toString() + "'");
    }

    public boolean containsPlayer(UUID uuid) {
        QueryResults results = mySQL.queryReturnable("SELECT * FROM 'Loadouts' ORDER BY ID DESC");

        for (QueryResult result : results.getList()) {
            String uuid_ = result.getUUID().substring(0, result.getUUID().indexOf("@"));
            if (uuid_.equals(uuid.toString())) {
                return true;
            }
        }
        return false;
    }

    public void setPlayerLoadout(Player player, GunObject... guns) {
        try {
            for (int i = 0; i < guns.length; i++) {
                mySQL.query("INSERT INTO Loadouts VALUES ('" + player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString() + "', '" + guns[i].getClassType().toString() + "', 'WEAPON', " + i + ", '" + guns[i].getName() + "')");
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void setPlayerLoadout(Player player, ArrayList<GunObject> guns) {
        try {
            for (int i = 0; i < guns.size(); i++) {
                mySQL.query("INSERT INTO Loadouts VALUES ('" + player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString() + "', '" + guns.get(i).getClassType().toString() + "', 'WEAPON', " + i + ", '" + guns.get(i).getName() + "')");
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void setPlayerLoadout(Player player, GunObject gun, int id) {
        try {
            mySQL.query("INSERT INTO Loadouts VALUES ('" + player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString() + "', '" + gun.getClassType().toString() + "', 'WEAPON', " + id + ", '" + gun.getName() + "')");
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public ArrayList<GunObject> getPlayerLoadout(Player player) {
        try {
            if (player.isOnline()) {
                try {
                    if (PlayerClasses.getPlayerClass(player) != null) {
                        if (loadouts.containsKey(player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString())) {
                            return loadouts.get(player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString());
                        } else {
                            System.out.println("Adding player to database...");
                            setPlayerLoadout(player, GunList.getDefaultGuns(PlayerClasses.getPlayerClass(player)));
                            reload();
                            return loadouts.get((player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString()));
                        }
                    } else {
                        return null;
                    }
                } catch (NullPointerException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

}