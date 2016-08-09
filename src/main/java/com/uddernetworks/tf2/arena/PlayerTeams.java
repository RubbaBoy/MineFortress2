package com.uddernetworks.tf2.arena;

import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PlayerTeams {

    static HashMap<Player, TeamEnum> teams = new HashMap<>();

    public static void addPlayer(Player player, TeamEnum team) {
        teams.put(player, team);
    }

    public static ArrayList<Player> getPlayersInTeam(TeamEnum team) {
        return teams.keySet().stream().filter(player -> teams.get(player) == team).collect(Collectors.toCollection(ArrayList::new));
    }

    public static TeamEnum getPlayer(Player player) {
        if (teams.containsKey(player)) {
            return teams.get(player);
        } else {
            System.out.println("Tried to get player not in team list");
            return null;
        }
    }

    public static boolean isSet(Player player) {
        return teams.containsKey(player);
    }

}