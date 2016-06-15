package com.uddernetworks.tf2.arena;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private final int id;
    final Location spawn;
    private final List<UUID> players = new ArrayList<UUID>();

    public Arena(Location spawn, int id) {
        this.spawn = spawn;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public List<UUID> getPlayers() {
        return this.players;
    }

}
