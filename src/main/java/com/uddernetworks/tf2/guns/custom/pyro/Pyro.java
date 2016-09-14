package com.uddernetworks.tf2.guns.custom.pyro;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.custom.Controller;
import com.uddernetworks.tf2.guns.custom.engineer.Construction;
import com.uddernetworks.tf2.guns.custom.engineer.Destruction;
import com.uddernetworks.tf2.guns.custom.medic.Medi;
import org.bukkit.entity.Player;

public class Pyro extends Controller {

    public Pyro(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
    }

    @Override
    public void sendStopNotify() {
        try {
            switch (getChildAbilities().get(0)) {
                case "flamethrower":
                    new Flamethrower(getGun(), getPlayer(), isHeald()).sendStopNotify();
                    break;
                default:
                    throw new Exception("Unknown pyro child");
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void runAbility() {
        try {
            switch (getChildAbilities().get(0)) {
                case "flamethrower":
                    new Flamethrower(getGun(), getPlayer(), isHeald());
                    break;
                default:
                    throw new Exception("Unknown pyro child");
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
