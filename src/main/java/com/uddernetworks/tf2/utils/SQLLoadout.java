package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.guns.GunList;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.playerclass.PlayerClasses;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class SQLLoadout {

    //        [PlayerName]@[Class]  Gun                   Cosmetic (Not yet supported)
    private static HashMap3<String, ArrayList<GunObject>, ArrayList<ItemStack>> loadouts = new HashMap3<>();

    private Main main;
    private MySQL mySQL;

    public SQLLoadout(Main main) {
        this.main = main;
         mySQL = new MySQL(this.main);
    }

    public void reload() {
        System.out.println("Reloading internal data set of loadouts");
        try {
//            loadouts.clear();
            QueryResults results = mySQL.queryReturnable("SELECT * FROM 'Loadouts' ORDER BY ID DESC");

            for (QueryResult result : results.getList()) {
                String UUID = result.getUUID();
                GunObject gun = GunList.getGunByName(result.getNAME());
                ArrayList<GunObject> gunObjects = new ArrayList<>();
                if (loadouts.get(UUID) != null) gunObjects = new ArrayList<>(loadouts.get(UUID));
                gunObjects.add(gun);
                System.out.println("Adding: " + UUID);
                loadouts.put(UUID, gunObjects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayerLoadout(Player player, GunObject... guns) {
        for (int i = 0; i < guns.length; i++) {
            mySQL.query("INSERT INTO Loadouts VALUES ('" + player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString() + "', '" + guns[i].getClassType().toString() + "', 'WEAPON', " + i + ", '" + guns[i].getName() + "')");
        }
    }

    public void setPlayerLoadout(Player player, ArrayList<GunObject> guns) {
        for (int i = 0; i < guns.size(); i++) {
            mySQL.query("INSERT INTO Loadouts VALUES ('" + player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString() + "', '" + guns.get(i).getClassType().toString() + "', 'WEAPON', " + i + ", '" + guns.get(i).getName() + "')");
        }
    }

    public void setPlayerLoadout(Player player, GunObject gun, int id) {
        mySQL.query("INSERT INTO Loadouts VALUES ('" + player.getUniqueId().toString() + "@" + PlayerClasses.getPlayerClass(player).toString() + "', '" + gun.getClassType().toString() + "', 'WEAPON', " + id + ", '" + gun.getName() + "')");
    }

    public ArrayList<GunObject> getPlayerLoadout(Player player) {
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
                    System.out.println("Null1");
                    return null;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Null2");
                return null;
            }
        } else {
            System.out.println("Null3");
            return null;
        }
    }

}