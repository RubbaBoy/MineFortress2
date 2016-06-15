package com.uddernetworks.tf2.guns;

import org.bukkit.Material;
import org.bukkit.Sound;

public class Gun {

    public void loadGuns() {
        GunObject gun = new GunObject("Cow Mangler 5000", "poop", Material.STONE, Sound.BLOCK_ANVIL_USE, 5);
        gun.registerGun()
    }

}
