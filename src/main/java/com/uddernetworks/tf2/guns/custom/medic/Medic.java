package com.uddernetworks.tf2.guns.custom.medic;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.custom.Controller;
import com.uddernetworks.tf2.guns.custom.spy.Watch;
import org.bukkit.entity.Player;

public class Medic extends Controller {

    public Medic(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
    }

    @Override
    public void sendStopNotify() {
        try {
            switch (getChildAbilities().get(0)) {
                case "medi":
                    new Medi(getGun(), getPlayer(), isHeald()).sendStopNotify();
                    break;
                default:
                    throw new Exception("Unknown medic child");
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void runAbility() {
        try {
            switch (getChildAbilities().get(0)) {
                case "medi":
                    new Medi(getGun(), getPlayer(), isHeald());
                    break;
                default:
                    throw new Exception("Unknown medic child");
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}