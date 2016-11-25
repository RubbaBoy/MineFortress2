package com.uddernetworks.tf2.utils;

import java.util.ArrayList;
import java.util.List;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

public class Hitbox {
    Entity e = null;

    private Location low = null;
    private Location high = null;
    private boolean update = true;

    public Hitbox(Entity e){
        this.e = e;
        this.update();
    }
    public Hitbox(Location low, Location high){
        this.low = low;
        this.high = high;
    }
    private void update(){
        try {
            if (e != null) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    double y = 1.8;
                    if (p.isSneaking()) {
                        y = 1.65;
                    }
                    low = p.getLocation().clone().add(-0.3, 0, -0.3);
                    high = p.getLocation().clone().add(0.3, y, 0.3);
                } else if (e instanceof Slime) {
                    Slime s = (Slime) e;
                    double side = s.getSize() * 0.51;
                    double half = side / 2;
                    low = s.getLocation().clone().add(half * -1, 0, half * -1);
                    high = s.getLocation().clone().add(half, side, half);
                } else if (e instanceof Sheep) {
                    Sheep s = (Sheep) e;
                    double y;
                    double side;
                    if (s.getAge() < 0) {
                        y = 0.65625;
                        side = 0.5;
                    } else {
                        y = 1.3;
                        side = 0.9;
                    }

                    double half = side / 2;
                    low = s.getLocation().clone().add(half * -1, 0, half * -1);
                    high = s.getLocation().clone().add(half, y, half);
                } else if (e instanceof Villager) {
                    double y = 1.95;
                    low = e.getLocation().clone().add(-0.3, 0, -0.3);
                    high = e.getLocation().clone().add(0.3, y, 0.3);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public boolean contains(Location l){
        try {
            if (update) {
                this.update();
            }
            return low.getX() <= l.getX()
                    && low.getY() <= l.getY()
                    && low.getZ() <= l.getZ()
                    && high.getX() >= l.getX()
                    && high.getY() >= l.getY()
                    && high.getZ() >= l.getZ();
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public boolean intersects(Hitbox box){
        try {
            if (box == null) {
                return false;
            }
            List<Location> edges = box.getEdges();
            box.update = false;
            this.update = false;
            for (Location l : edges) {
                if (this.contains(l)) {
                    box.update = true;
                    this.update = true;
                    return true;
                }
            }
            box.update = true;
            this.update = true;
            return false;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public List<Location> getEdges(){
        try {
            this.update();
            List<Location> r = new ArrayList<>();
            World world = low.getWorld();
            double lx = low.getX();
            double ly = low.getY();
            double lz = low.getZ();

            double hx = high.getX();
            double hy = high.getY();
            double hz = high.getZ();
            r.add(new Location(world, lx, ly, lz));
            r.add(new Location(world, lx, hy, lz));
            r.add(new Location(world, lx, ly, hz));
            r.add(new Location(world, lx, hy, hz));

            r.add(new Location(world, hx, ly, lz));
            r.add(new Location(world, hx, hy, lz));
            r.add(new Location(world, hx, ly, hz));
            r.add(new Location(world, hx, hy, hz));
            return r;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }
    public Location getLow(){
        try {
            return low;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public Location getHigh(){
        try {
            return high;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }
}