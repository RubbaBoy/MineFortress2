package com.uddernetworks.tf2.game;

import com.uddernetworks.tf2.arena.ArenaManager;
import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.arena.TeamChooser;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.inv.ClassChooser;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.GameEnum;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Game implements Listener {

    public GameEnum gameType = GameEnum.ARENA;

    public int count = 0;
    private Main main;
    TeamChooser chooser = new TeamChooser(main);

    static World world;

    static GameState state = GameState.NOTHING;

    public Game(Main main) {
        this.main = main;
    }

    public void sendPlayer(Player player) {
        try {
            state = GameState.WAITING;
            ClassChooser chooser = new ClassChooser(main);
            chooser.openGUI(player);

            if (gameType == GameEnum.ARENA) {
//            world = Bukkit.getWorld(main.worlds.get(random.nextInt(main.worlds.size())));
                if (world == null && Bukkit.getWorld(main.worlds.get(0)) != null) {
                    try {
                        world = Bukkit.getWorld(main.worlds.get(0));
                    } catch (NullPointerException ignored) {
                    }
                }
            }

            ArenaManager.getManager().addPlayer(player);

            if (gameType == GameEnum.ARENA) {
                state = GameState.INGAME;
            } else {
                state = GameState.COUNTDOWN;
                count = 0;
                new BukkitRunnable() {
                    public void run() {
                        if (count < 5) {
                            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                sendTitle(player, "Starting game in " + (5 - count), "Get ready!", 0, 20, 0);
                            }
                            count++;
                        } else if (count == 6) {
                            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                                sendTitle(player, "The game has begun!", "Good luck!", 5, 30, 5);
                            }
                            count++;
                        } else {
                            count = 0;
                            this.cancel();
//                        start();
                        }
                    }
                }.runTaskTimer(Main.getPlugin(), 20, 20);
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public static GameState getGameState(){
        return state;
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeInTime, int staytime, int fadeOutTime) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}");
        IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComponent);
        PacketPlayOutTitle thyme = new PacketPlayOutTitle(fadeInTime, staytime, fadeOutTime);

        connection.sendPacket(titlePacket);
        connection.sendPacket(subtitlePacket);
        connection.sendPacket(thyme);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getGameState() != GameState.NOTHING) {
            chooser.sendPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            if (getGameState() == GameState.INGAME) {
                if (ArenaManager.getManager().isInGame(event.getPlayer())) {
                    if (main.isInBarrier(event.getTo(), PlayerTeams.getPlayer(event.getPlayer()))) {
                        if (event.getFrom() != event.getTo()) {
                            Location to = event.getFrom();
                            to.setPitch(event.getTo().getPitch());
                            to.setYaw(event.getTo().getYaw());
                            event.setTo(to);
                        }
                    }
                }
            } else if (getGameState() == GameState.COUNTDOWN || getGameState() == GameState.WAITING) {
                if (event.getFrom() != event.getTo()) {
                    Location to = event.getFrom();
                    to.setPitch(event.getTo().getPitch());
                    to.setYaw(event.getTo().getYaw());
                    event.setTo(to);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        try {
            final Player player = event.getPlayer();
            if (Game.getGameState() == GameState.INGAME) {
                if (ArenaManager.getManager().isInGame(player)) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (player.getInventory().getItemInMainHand().getType() == Material.WOOD_HOE || player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public GameEnum getGameType() {
        return gameType;
    }

    public World getWorld() {
        return world;
    }
}
