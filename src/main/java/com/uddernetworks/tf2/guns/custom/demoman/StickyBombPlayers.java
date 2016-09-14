package com.uddernetworks.tf2.guns.custom.demoman;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class StickyBombPlayers {

    private static HashMap<Player, ArrayList<StickyBomb>> players = new HashMap<>();

    public void setPlayer(Player player, StickyBomb stickyBomb) {
        if (players.containsKey(player)) {
            ArrayList<StickyBomb> bombs = new ArrayList<>(players.get(player));
            bombs.add(stickyBomb);
            players.put(player, bombs);
        } else {
            ArrayList<StickyBomb> bombs = new ArrayList<>();
            bombs.add(stickyBomb);
            players.put(player, bombs);
        }
    }

    public boolean canHaveMore(Player player) {
        return !players.containsKey(player) || players.get(player).size() < 6;
    }

    public void explode(Player player) {
        if (players.containsKey(player)) {
            players.get(player).forEach(StickyBomb::explode);
            players.remove(player);
        }
    }

}