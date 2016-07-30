package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.guns.Bullet;
import com.uddernetworks.tf2.guns.GunObject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HitByBulletEvent extends Event {

    Entity entity;
    GunObject gun;

    public HitByBulletEvent(Entity entity, GunObject gun) {
        this.entity = entity;
        this.gun = gun;
    }

    public Entity getEntity() {
        return entity;
    }

    public GunObject getGun() {
        return gun;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
