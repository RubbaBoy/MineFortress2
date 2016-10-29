package com.uddernetworks.tf2.guns.custom.demoman;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class StickyBombPlayers {

    private static HashMap<Player, ArrayList<Long>> player_times = new HashMap<>();
    private static HashMap<Player, ArrayList<StickyBomb>> players = new HashMap<>();

    public void setPlayer(Player player, StickyBomb stickyBomb, long time) {
        try {
            if (players.containsKey(player)) {
                ArrayList<Long> times = new ArrayList<>(player_times.get(player));
                times.add(time);
                player_times.put(player, times);
                ArrayList<StickyBomb> bombs = new ArrayList<>(players.get(player));
                bombs.add(stickyBomb);
                players.put(player, bombs);
            } else {
                ArrayList<Long> times = new ArrayList<>();
                times.add(time);
                player_times.put(player, times);
                ArrayList<StickyBomb> bombs = new ArrayList<>();
                bombs.add(stickyBomb);
                players.put(player, bombs);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public boolean canHaveMore(Player player) {
        try {
            return !players.containsKey(player) || players.get(player).size() < 6;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public void explode(Player player) {
        try {
            if (players.containsKey(player)) {
                ArrayList<Integer> toRemove = new ArrayList<>();
                for (int i = 0; i < players.get(player).size(); i++) {
                    if (System.currentTimeMillis() - player_times.get(player).get(i) >= 700) {
                        StickyBomb stickyBomb = players.get(player).get(i);
                        stickyBomb.explode();
                        toRemove.add(i);
                    }
                }
                for (int i : toRemove) {
                    players.get(player).remove(i);
                    player_times.get(player).remove(i);
                }
                players.remove(player);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}