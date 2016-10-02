package com.uddernetworks.tf2.guns.custom.engineer;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.sentry.Sentries;
import com.uddernetworks.tf2.inv.ConstructionChooser;
import com.uddernetworks.tf2.inv.DestructionChooser;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.entity.Player;

public class Construction extends Engineer {

    public Construction(GunObject gun, Player player, boolean held) {
        super(gun, player, held);
        try {

            if (!isHeald()) {
                ConstructionChooser chooser = new ConstructionChooser(Main.getPlugin());
                chooser.openGUI(getPlayer());
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }
}