package com.uddernetworks.tf2.guns.custom.pyro;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.DeadMode;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.utils.particles.Particles;
import org.bukkit.entity.Player;

public class Flamethrower extends Pyro {

    PlayerGuns playerGuns = new PlayerGuns();

    public Flamethrower(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
        try {
            if (isHeald()) {
                if (!DeadMode.isInDeadMode(player)) {
                    Particles.spawnFlamethrowerParticles(player, gun);
                    playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}