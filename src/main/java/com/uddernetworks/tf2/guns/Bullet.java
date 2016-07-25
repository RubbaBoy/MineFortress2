package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.utils.Hitbox;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Bullet implements Listener {

    static HashMap<Integer, Bullet> bullets = new HashMap<>();

    private GunObject gun;
    private Player shooter;

    public Bullet() {

    }

    public Bullet(Player shooter, GunObject gunObject) {
        Location loc = shooter.getLocation();
        gun = gunObject;
        this.shooter = shooter;

        if (loc.getBlock().getType() == Material.STEP) {

            if (loc.getBlock().getData() <= 7) {
                if (loc.getY() <= loc.getBlockY() + 0.5) {
                    shoot(shooter, gunObject);
                }
            } else if (loc.getBlock().getData() >= 8) {
                if (loc.getY() >= loc.getBlockY() + 0.5) {
                    shoot(shooter, gunObject);
                }
            }

        } else if (loc.getBlock().getType().isSolid()) {
            if (gun.getDamage() >= 2 && gun.getDamage() < 5) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    EntityArmorStand entity;
                    entity = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
                    ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

                    PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), (byte) 2);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                }
            } else if (gun.getDamage() >= 5 && gun.getDamage() < 10) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    EntityArmorStand entity;
                    entity = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
                    ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

                    PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), (byte) 5);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                }
            } else if (gun.getDamage() >= 10 && gun.getDamage() < 20) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    EntityArmorStand entity;
                    entity = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
                    ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

                    PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), (byte) 7);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                }
            } else if (gun.getDamage() >= 20) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    EntityArmorStand entity;
                    entity = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
                    ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

                    PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), (byte) 9);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                }
            }
        } else {
            shoot(shooter, gunObject);
        }
    }

    private void shoot(Player shooter, GunObject gunObject) {
        boolean inBlock = false;

        if (shooter != null && gunObject != null) {
            if (gunObject.isSniper()) {
                Location loc = shooter.getLocation();
                Vector direction = loc.getDirection().normalize();
                for (int i = 0; i < 100; i++) {
                    double x = direction.getX() * i;
                    double y = direction.getY() * i + shooter.getEyeHeight();
                    double z = direction.getZ() * i;
                    loc.add(x, y, z);

                    if (gunObject.getTracers()) {
                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.CRIT, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1, null);
                        ((CraftPlayer) shooter).getHandle().playerConnection.sendPacket(packet);
                    }

                    if (loc.getBlock().getType() == Material.STEP) {

                        if (loc.getBlock().getData() <= 7) {
                            if (loc.getY() <= loc.getBlockY() + 0.5) {

                            }
                        } else if (loc.getBlock().getData() >= 8) {
                            if (loc.getY() >= loc.getBlockY() + 0.5) {

                            }
                        } else {
                            i = 100;
                            inBlock = true;
                        }

                    } else if (loc.getBlock().getType().isSolid()) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            EntityArmorStand entity;
                            entity = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
                            ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

                            PacketPlayOutBlockBreakAnimation packet9 = new PacketPlayOutBlockBreakAnimation(entity.getId(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), (byte) 9);
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet9);
                        }
                        i = 100;
                        inBlock = true;
                    }

                    List<Entity> near = shooter.getWorld().getEntities();
                    if (!inBlock) {
                        for (Entity e : near) {
                            if (!e.equals(shooter)) {
                                if (e instanceof LivingEntity) {
                                    if (((LivingEntity) e).getEyeLocation().distance(loc) < 3) {
                                        Hitbox hitbox = new Hitbox(e);
                                        if (hitbox.contains(loc)) {
                                            ((Damageable) e).damage(gunObject.getDamage());
                                            DamageIndicator.spawnIndicator(gunObject.getDamage(), loc.getWorld(), x, y, z);
                                            i = 100;
                                        }
                                    } else if (e.getLocation().distance(loc) < 3) {
                                        Hitbox hitbox = new Hitbox(e);
                                        if (hitbox.contains(loc)) {
                                            ((Damageable) e).damage(gunObject.getDamage());
                                            DamageIndicator.spawnIndicator(gunObject.getDamage(), loc.getWorld(), x, y, z);
                                            i = 100;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    loc.subtract(x, y, z);

                }
            } else {
                Arrow bullet = shooter.getWorld().spawn(shooter.getEyeLocation(), Arrow.class);
                bullets.put(bullet.getEntityId(), this);
                bullet.setShooter(shooter);
                Location loc = shooter.getLocation();
                Vector direction = loc.getDirection().normalize();
                Random random = new Random();
                if (gunObject.getAccuracy() < 10) {
                    double x = direction.getX() + (random.nextGaussian() / (10 + gunObject.getAccuracy() * 3.5)) / 2;
                    double y = direction.getY() + (random.nextGaussian() / (10 + gunObject.getAccuracy() * 3.5)) / 2;
                    double z = direction.getZ() + (random.nextGaussian() / (10 + gunObject.getAccuracy() * 3.5)) / 2;
                    bullet.setVelocity(new Vector(x, y, z).multiply(gunObject.getPower()));
                } else {
                    bullet.setVelocity(direction.multiply(gunObject.getPower()));
                }

            }
        }

    }

    public GunObject getGun() {
        return this.gun;
    }

    public Player getShooter() {
        return this.shooter;
    }
}