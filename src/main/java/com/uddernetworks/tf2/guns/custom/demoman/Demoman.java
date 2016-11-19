package com.uddernetworks.tf2.guns.custom.demoman;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.custom.Controller;
import org.bukkit.entity.Player;

public class Demoman extends Controller {

    public Demoman(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
    }

    @Override
    public void sendStopNotify(Player player) {
        try {
            switch (getChildAbilities().get(0)) {
                case "grenade_launcher":
                    new GrenadeLauncher(getGun(), getPlayer(), isHeald()).sendStopNotify(player);
                    break;
                case "stickybomb":
                    new StickyBomb(getGun(), getPlayer(), isHeald()).sendStopNotify(player);
                    break;
                default:
                    System.out.println("Unknown demoman child");
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void runAbility() {
        try {
            switch (getChildAbilities().get(0)) {
                case "grenade_launcher":
                    new GrenadeLauncher(getGun(), getPlayer(), isHeald());
                    break;
                case "stickybomb":
                    new StickyBomb(getGun(), getPlayer(), isHeald());
                    break;
                default:
                    System.out.println("Unknown demoman child");
            }
        } catch (Exception ignored) {}
    }
}