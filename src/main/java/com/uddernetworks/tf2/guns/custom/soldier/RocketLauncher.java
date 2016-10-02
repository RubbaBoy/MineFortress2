package com.uddernetworks.tf2.guns.custom.soldier;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.GunList;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.PlayerGuns;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public class RocketLauncher extends Soldier {

    PlayerGuns playerGuns = new PlayerGuns();

    public RocketLauncher(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
        try {
            if (!held) {
                playerGuns.setClip(player, playerGuns.getClip(player) - 1);
                if (gun.getSound() != null) {
                    player.playSound(player.getLocation(), gun.getSound(), 1, 1);
                }

                Fireball fireball = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);
                fireball.setBounce(false);
                fireball.setIsIncendiary(false);
                fireball.setShooter(getPlayer());
                fireball.setCustomName("fireball_" + GunList.getIndexOf(gun));
                fireball.setVelocity(player.getLocation().getDirection().normalize().multiply(2));
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

}