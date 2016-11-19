package com.uddernetworks.tf2.guns.custom.soldier;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.custom.Controller;
import org.bukkit.entity.Player;

public class Soldier extends Controller {

    public Soldier(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
    }

    @Override
    public void sendStopNotify(Player player) {
        try {
            switch (getChildAbilities().get(0)) {
                case "rocket_launcher":
                    new RocketLauncher(getGun(), getPlayer(), isHeald()).sendStopNotify(player);
                    break;
                default:
                    System.out.println("Unknown soldier child");
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void runAbility() {
        try {
            switch (getChildAbilities().get(0)) {
                case "rocket_launcher":
                    new RocketLauncher(getGun(), getPlayer(), isHeald());
                    break;
                default:
                    System.out.println("Unknown soldier child");
            }
        } catch (Exception ignored) {}
    }

}
