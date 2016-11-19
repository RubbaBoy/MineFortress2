package com.uddernetworks.tf2.guns.custom.engineer;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.custom.Controller;
import com.uddernetworks.tf2.guns.custom.medic.Medi;
import org.bukkit.entity.Player;

public class Engineer extends Controller {

    public Engineer(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
    }

    @Override
    public void sendStopNotify(Player player) {
        try {
            switch (getChildAbilities().get(0)) {
                case "construction":
                    new Construction(getGun(), getPlayer(), isHeald()).sendStopNotify(player);
                    break;
                case "destruction":
                    new Destruction(getGun(), getPlayer(), isHeald()).sendStopNotify(player);
                    break;
                case "wrench":
                    break;
                default:
                    System.out.println("Unknown engineer child");
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void runAbility() {
        try {
            switch (getChildAbilities().get(0)) {
                case "construction":
                    new Construction(getGun(), getPlayer(), isHeald());
                    break;
                case "destruction":
                    new Destruction(getGun(), getPlayer(), isHeald());
                    break;
                case "wrench":
                    break;
                default:
                    System.out.println("Unknown engineer child");
            }
        } catch (Exception ignored) {}
    }
}
