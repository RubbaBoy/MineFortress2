package com.uddernetworks.tf2.guns.pickups;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Ammo {

    private Location location;
    private Item item;

    public Ammo(Location location) {
        this.location = location;
    }

    public void spawn() {
        try {
            item = location.getWorld().dropItem(location, new ItemStack(Material.IRON_SPADE, 1));
            item.setVelocity(new Vector(0, 0, 0));
            item.teleport(location);
            Ammos.addAmmo(this);
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
            if (Ammos.exists(this)) {
                Ammos.removeAmmo(this);
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
