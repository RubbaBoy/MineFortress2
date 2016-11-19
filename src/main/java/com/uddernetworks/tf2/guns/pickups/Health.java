package com.uddernetworks.tf2.guns.pickups;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Health {

    private Location location;
    private Item item;

    public Health(Location location) {
        this.location = location;
    }

    public void spawn() {
        try {
            item = location.getWorld().dropItem(location, new ItemStack(Material.GOLD_SPADE, 1));
            item.setVelocity(new Vector(0, 0, 0));
            item.teleport(location);
            Healths.addHealth(this);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void remove() {
        try {
            if (item != null) {
                if (!item.isDead()) {
                    item.remove();
                }
            }

            if (Healths.exists(this)) {
                Healths.removeHealth(this);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public Item getItem() {
        return item;
    }

    public Location getLocation() {
        return location;
    }
}
