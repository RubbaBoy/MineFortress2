package com.uddernetworks.tf2.guns.custom.medic;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.game.Game;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.guns.PlayerHealth;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.entity.Player;

public class Medi extends Medic {

    Game game = new Game(Main.getPlugin());
    PlayerGuns playerGuns = new PlayerGuns();
    PlayerHealth playerHealth = new PlayerHealth();
    Player newPlayer;
    static boolean stop = false;

    public Medi(GunObject gun, Player player, boolean held) {
        super(gun, player, held);

        if (isHeald()) {
            game.getWorld().getEntities().stream().filter(entity -> player.hasLineOfSight(entity) && entity.getLocation().distance(player.getLocation()) <= 5 && entity instanceof Player).forEach(entity -> {
                Player tempplayer = (Player) entity;
                if (tempplayer.getUniqueId() != player.getUniqueId()) {
                    if (this.newPlayer == null) {
                        this.newPlayer = (Player) entity;
                    } else if (entity.getLocation().distance(player.getLocation()) < this.newPlayer.getLocation().distance(player.getLocation())) {
                        this.newPlayer = (Player) entity;
                    }
                }
            });
            if (this.newPlayer != null) {
                Particles.spawnHealthParticles(this.newPlayer.getLocation().add(0, this.newPlayer.getEyeHeight(), 0), player.getLocation().add(0, player.getEyeHeight(), 0), PlayerTeams.getPlayer(player));
                if (playerHealth.getHealth(this.newPlayer) + 6 > playerHealth.getMaxHealth(this.newPlayer)) {
                    playerHealth.addHealth(this.newPlayer, playerHealth.getMaxHealth(this.newPlayer));
                } else {
                    playerHealth.addHealth(this.newPlayer, playerHealth.getHealth(this.newPlayer) + 6);
                }
                playerGuns.setClip(player, playerGuns.getClip(player) - 1);
            }
        }
    }

    @Override
    public void sendStopNotify() {
        stop = true;
    }
}
