package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.arena.ArenaManager;
import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.dispenser.Dispenser;
import com.uddernetworks.tf2.guns.dispenser.Dispensers;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.guns.sentry.Sentry;
import com.uddernetworks.tf2.guns.teleporter.TeleporterEntrance;
import com.uddernetworks.tf2.guns.teleporter.TeleporterExit;
import com.uddernetworks.tf2.guns.teleporter.Teleporters;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.Hitbox;
import net.minecraft.server.v1_10_R1.*;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Bullet implements Listener {

    public static Map<Integer, Bullet> bullets = new HashMap<>();

    private GunObject gun = null;
    private Sentry sentry = null;
    private Player shooter;

    PlayerHealth playerHealth = new PlayerHealth();

    private List<Entity> near;

    public Bullet() {}

    public Bullet(Player shooter, GunObject gunObject) {
        try {
            gun = gunObject;
            this.shooter = shooter;

            near = shooter.getWorld().getEntities();

            shoot(shooter, gun);

        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public Bullet(Sentry sentry) {
        try {
            this.sentry = sentry;
            shoot(sentry);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    private void shoot(Player shooter, GunObject gunObject) {
        try {
            boolean inBlock = false;

            if (shooter != null && gunObject != null) {
                if (gunObject.isSniper()) {
                    Location loc = shooter.getLocation();
                    Vector direction = loc.getDirection().normalize();
                    for (int i = 0; i < 200; i++) {
                        double i2 = i;
                        double x = direction.getX() * (i2 * 0.5);
                        double y = direction.getY() * (i2 * 0.5) + shooter.getEyeHeight();
                        double z = direction.getZ() * (i2 * 0.5);
                        loc.add(x, y, z);

                        if (gunObject.getTracers()) {
                            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.CRIT, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1, null);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                            }
                        }

                        if (loc.getBlock().getType() == Material.STEP) {

                            if (loc.getBlock().getData() <= 7) {
                                if (loc.getY() <= loc.getBlockY() + 0.5) {

                                }
                            } else if (loc.getBlock().getData() >= 8) {
                                if (loc.getY() >= loc.getBlockY() + 0.5) {

                                }
                            } else {
                                i = 200;
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
                            i = 200;
                            inBlock = true;

                            if (Dispensers.isObjectDispenser(loc.getBlock())) {
                                Dispenser dispenser = Dispensers.getDispenser(loc.getBlock());
                                dispenser.setHealth(dispenser.getHealth() - (int) gun.getDamage());
                            } else if (Teleporters.isEntrance(loc)) {
                                TeleporterEntrance entrance = Teleporters.getEntrance(loc);
                                entrance.setHealth(entrance.getHealth() - (int) gun.getDamage());
                            } else if (Teleporters.isExit(loc)) {
                                TeleporterExit exit = Teleporters.getExit(loc);
                                exit.setHealth(exit.getHealth() - (int) gun.getDamage());
                            }
                        }

                        if (!inBlock) {
                            for (Entity e : near) {
                                if (!e.equals(shooter)) {
                                    if (e instanceof Player) {
                                        if (ArenaManager.getManager().isInGame((Player) e)) {
                                            if (PlayerTeams.getPlayer((Player) e) != PlayerTeams.getPlayer(shooter)) {
                                                if (isWithinEntityBoundingBox(loc, e)) {
                                                    if (playerHealth.addHealth((Player) e, playerHealth.getHealth((Player) e) - gunObject.getDamage())) {
                                                        DeathMessage deathMessage = new DeathMessage((Player) e, shooter, gun);
                                                        deathMessage.sendMessage();
                                                    }
                                                    DamageIndicator.spawnIndicator(gunObject.getDamage(), loc.getWorld(), e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ());
                                                    i = 200;
                                                }
                                            }
                                        }
                                    } else if (e instanceof ArmorStand) {
                                        if (Sentries.isObjectSentry((ArmorStand) e)) {
                                            Sentry sentry = Sentries.getSentry((ArmorStand) e);
                                            sentry.setHealth(sentry.getHealth() - (int) gun.getDamage());
                                        }
                                    }
                                }
                            }
                        }
                        loc.subtract(x, y, z);

                    }
                } else {
                    Arrow bullet = shooter.getWorld().spawn(shooter.getEyeLocation(), Arrow.class);
                    bullet.setShooter(shooter);
                    Location loc = shooter.getLocation();
                    Vector direction = loc.getDirection().normalize();
                    Random random = new Random();
                    if (gunObject.getAccuracy() < 10) {
                        double x = direction.getX() + (random.nextGaussian() / (10 + gunObject.getAccuracy() * 4)) / 2;
                        double y = direction.getY() + (random.nextGaussian() / (10 + gunObject.getAccuracy() * 4)) / 2;
                        double z = direction.getZ() + (random.nextGaussian() / (10 + gunObject.getAccuracy() * 4)) / 2;
                        bullet.setBounce(false);
                        bullet.setVelocity(new Vector(x, y, z).multiply(gunObject.getPower()));
                        bullet.setCustomName(String.valueOf(GunList.getIndexOf(this.getGun())));
                        bullets.put(bullet.getEntityId(), this);
                    } else {
                        bullet.setBounce(false);
                        bullet.setVelocity(direction.multiply(gunObject.getPower()));
                        bullet.setCustomName(String.valueOf(GunList.getIndexOf(this.getGun())));
                        bullets.put(bullet.getEntityId(), this);
                    }

                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    private void shoot(Sentry sentry) {
        try {
            boolean inBlock = false;

            if (sentry != null) {
                Arrow bullet = sentry.getObj().getWorld().spawn(sentry.getObj().getEyeLocation(), Arrow.class);
                bullet.setShooter(sentry.getObj());
                Location loc = sentry.getObj().getLocation();
                Vector direction2 = new Vector(sentry.getObj().getLocation().getX() - sentry.getTarget().getLocation().getX(), sentry.getObj().getLocation().getY() - sentry.getTarget().getLocation().getY(), sentry.getObj().getLocation().getZ() - sentry.getTarget().getLocation().getZ()).normalize();
                Vector direction = direction2.clone().subtract(direction2.clone().multiply(3));
                Random random = new Random();
                if (sentry.getAccuracy() < 10) {
                    double x = direction.getX() + (random.nextGaussian() / (10 + sentry.getAccuracy() * 3.5)) / 2;
                    double y = direction.getY() + (random.nextGaussian() / (10 + sentry.getAccuracy() * 3.5)) / 2;
                    double z = direction.getZ() + (random.nextGaussian() / (10 + sentry.getAccuracy() * 3.5)) / 2;
                    bullet.setVelocity(new Vector(x, y, z));
                    bullet.setCustomName("Sentry-" + Sentries.getSentryId(sentry));
                    bullets.put(bullet.getEntityId(), this);
                } else {
                    bullet.setVelocity(direction);
                    bullet.setCustomName("Sentry-" + Sentries.getSentryId(sentry));
                    bullets.put(bullet.getEntityId(), this);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public boolean isBulletFromSentry(String name) {
        try {
            return name.startsWith("Sentry-");
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public boolean isBullet(String name) {
        try {
            return NumberUtils.isNumber(name) && GunList.isGunId(Integer.parseInt(name));
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return false;
        }
    }

    public Sentry getSentry() {
        try {
            return this.sentry;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public GunObject getGun() {
        try {
            return this.gun;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public Player getShooter() {
        try {
            return this.shooter;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public static boolean isWithinEntityBoundingBox(Location location, Entity entity) {

        AxisAlignedBB bb = ((CraftEntity) entity).getHandle().getBoundingBox();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return
                x >= bb.a &&
                        x <= bb.d &&
                        y >= bb.b &&
                        y <= bb.e &&
                        z >= bb.c &&
                        z <= bb.f;

    }
}