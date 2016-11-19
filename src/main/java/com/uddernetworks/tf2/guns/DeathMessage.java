package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.guns.sentry.Sentry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeathMessage {

    private Player victim;
    private Player killer;

    private GunObject gun;
    private Sentry sentry;
    private boolean usesGun;

    public DeathMessage(Player victim, Player killer, GunObject gun) {
        this.victim = victim;
        this.killer = killer;
        this.gun = gun;

        this.usesGun = true;
    }

    public DeathMessage(Player victim, Player killer, Sentry sentry) {
        this.victim = victim;
        this.killer = killer;
        this.sentry = sentry;

        this.usesGun = false;
    }

    public void sendMessage() {
        if (usesGun) {
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + victim.getName() + ChatColor.RESET + ChatColor.GRAY + " was killed by " + ChatColor.RESET + ChatColor.DARK_GRAY + ChatColor.BOLD + killer.getName() + ChatColor.RESET + ChatColor.GRAY + ChatColor.BOLD + " with a " + gun.getName());
        } else {
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + victim.getName() + ChatColor.RESET + ChatColor.GRAY + " was killed by " + ChatColor.RESET + ChatColor.DARK_GRAY + ChatColor.BOLD + killer.getName() + "'s sentry");
        }
    }

}
