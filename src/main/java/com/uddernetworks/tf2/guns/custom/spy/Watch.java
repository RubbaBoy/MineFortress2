package com.uddernetworks.tf2.guns.custom.spy;

import com.uddernetworks.tf2.arena.ArenaManager;
import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.ActionBar;
import com.uddernetworks.tf2.utils.TeamEnum;
import net.minecraft.server.v1_10_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Watch extends Spy {

    static boolean stop = false;
    final String complete_counter_text = "||||||||||||||||||||||||||||||||||||||||||||||||||";
    String counter_text = "||||||||||||||||||||||||||||||||||||||||||||||||||";
    int counter = 0;

    static HashMap<Player, Boolean> invis_players = new HashMap<>();

    public Watch(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
        player.sendMessage("Turning you invisible!!");
        if (held) {
            if (!invis_players.containsKey(player)) {
                invis_players.put(player, false);
            }
            if (!invis_players.get(player)) {
                timer(true);
                setCanProceed(false);
                invis_players.put(player, true);
                Entity en = ((CraftPlayer) player).getHandle();
                en.setSilent(true);
                Bukkit.getOnlinePlayers().stream().filter(player2 -> PlayerTeams.getPlayer(player2) != PlayerTeams.getPlayer(player)).forEach(player2 -> {
                    player2.hidePlayer(player);
                });
            } else {
                timer(false);
                showPlayer();
            }
        }
    }

    public void timer(boolean running) {
        new BukkitRunnable() {
            public void run() {
                if (running) {
                    if (counter < 49) {
                        counter++;
                        if (PlayerTeams.getPlayer(getPlayer()) == TeamEnum.BLUE) {
                            new ActionBar(getPlayer(), counter_text, ChatColor.BLUE, ChatColor.BOLD);
                        } else {
                            new ActionBar(getPlayer(), counter_text, ChatColor.RED, ChatColor.BOLD);
                        }
                        counter_text = complete_counter_text;
                        counter_text = counter_text.substring(0, (50 - counter)) + ChatColor.GRAY + ChatColor.BOLD + counter_text.substring((50 - counter), counter_text.length());

                    } else {
                        new ActionBar(getPlayer(), "");
                        showPlayer();
                        counter = 0;
                        counter_text = complete_counter_text;
                        this.cancel();
                    }
                } else {
                    new ActionBar(getPlayer(), "");
                    showPlayer();
                    counter = 0;
                    counter_text = complete_counter_text;
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 4L);
    }

    public void showPlayer() {
        invis_players.put(getPlayer(), false);
        Entity en = ((CraftPlayer) getPlayer()).getHandle();
        en.setSilent(false);
        ArenaManager.getManager().getArena().getPlayers().stream().filter(player2 -> PlayerTeams.getPlayer(Bukkit.getPlayer(player2)) != PlayerTeams.getPlayer(getPlayer())).forEach(player2 -> {
            Bukkit.getPlayer(player2).showPlayer(getPlayer());
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                setCanProceed(true);
            }
        }.runTaskLater(Main.getPlugin(), 20);
    }

    @Override
    public void sendStopNotify() {
        stop = true;
    }
}
