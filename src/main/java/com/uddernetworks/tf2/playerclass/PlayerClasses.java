package com.uddernetworks.tf2.playerclass;

import com.uddernetworks.tf2.utils.ClassEnum;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PlayerClasses {

    static HashMap<Player, ClassEnum> classes = new HashMap<>();

    public static void setPlayerClass(Player player, ClassEnum classtype) {
        classes.put(player, classtype);
    }

    public static ArrayList<Player> getPlayersOfClassType(ClassEnum classtype) {
        return PlayerClasses.classes.keySet().stream().filter(player -> PlayerClasses.classes.get(player) == classtype).collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean isSet(Player player) {
        return PlayerClasses.classes.containsKey(player);
    }

    public static ClassEnum getPlayerClass(Player player) {
        return classes.get(player);
    }

}