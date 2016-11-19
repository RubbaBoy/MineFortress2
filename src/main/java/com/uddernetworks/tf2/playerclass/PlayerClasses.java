package com.uddernetworks.tf2.playerclass;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.gui.MetalGUI;
import com.uddernetworks.tf2.guns.PlayerMetal;
import com.uddernetworks.tf2.utils.ClassEnum;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PlayerClasses {

    static HashMap<Player, ClassEnum> classes = new HashMap<>();

    public static void setPlayerClass(Player player, ClassEnum classtype) {
        try {
            if (classtype == ClassEnum.ENGINEER) {
                PlayerMetal.addPlayer(player, 0);
            } else {
                PlayerMetal.addPlayer(player, -1);
            }
            new MetalGUI(player);
            classes.put(player, classtype);

            player.setWalkSpeed(((classtype.getSpeed() / 100F) * 0.2F));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static ArrayList<Player> getPlayersOfClassType(ClassEnum classtype) {
        try {
            return PlayerClasses.classes.keySet().stream().filter(player -> PlayerClasses.classes.get(player) == classtype).collect(Collectors.toCollection(ArrayList::new));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static boolean isSet(Player player) {
        try {
            return PlayerClasses.classes.containsKey(player);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public static ClassEnum getPlayerClass(Player player) {
        try {
            if (classes.containsKey(player)) {
                return classes.get(player);
            } else {
                return null;
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

}