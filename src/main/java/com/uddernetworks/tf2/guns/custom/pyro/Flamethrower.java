package com.uddernetworks.tf2.guns.custom.pyro;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.entity.Player;

public class Flamethrower extends Pyro {

    PlayerGuns playerGuns = new PlayerGuns();
    static boolean stop = false;

    public Flamethrower(GunObject gun, Player player, boolean held) {
        super(gun, player, held);

        if (isHeald()) {
            Particles.spawnFlamethrowerParticles(player);
            playerGuns.setClip(player, playerGuns.getClip(player) - 1);
        }
    }

    @Override
    public void sendStopNotify() {
        stop = true;
    }
}
