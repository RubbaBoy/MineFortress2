package com.uddernetworks.tf2.guns.custom.engineer;

import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.inv.DestructionChooser;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.entity.Player;

public class Destruction extends Engineer {

    public Destruction(GunObject gun, Player player, boolean held) {
        super(gun, player, held);

        if (!held) {
            DestructionChooser chooser = new DestructionChooser(Main.getPlugin());
            chooser.openGUI(getPlayer());
        }
    }
}