package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FireballHitEvent extends Event {

    private Fireball fireball;
    private Player player;

    public FireballHitEvent(Fireball fireball) {
        this.fireball = fireball;
    }

    public FireballHitEvent(Fireball fireball, Player player) {
        this.fireball = fireball;
        this.player = player;
    }

    public boolean hitPlayer() {
        try {
            return player != null;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public Fireball getFireball() {
        try {
            return fireball;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public Player getPlayer() {
        try {
            return player;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}