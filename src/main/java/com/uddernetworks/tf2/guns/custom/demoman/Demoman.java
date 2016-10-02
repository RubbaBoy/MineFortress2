package com.uddernetworks.tf2.guns.custom.demoman;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.custom.Controller;
import org.bukkit.entity.Player;

public class Demoman extends Controller {

    public Demoman(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
    }

    @Override
    public void sendStopNotify() {
        try {
            switch (getChildAbilities().get(0)) {
                case "grenade_launcher":
                    new GrenadeLauncher(getGun(), getPlayer(), isHeald()).sendStopNotify();
                    break;
                case "stickybomb":
                    new StickyBomb(getGun(), getPlayer(), isHeald()).sendStopNotify();
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