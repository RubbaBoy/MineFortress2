package com.uddernetworks.tf2.gui;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.guns.Ammo;
import com.uddernetworks.tf2.guns.Clip;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BulletGUI {

    public BulletGUI(Player player) {
        Material material;
        if (PlayerTeams.getPlayer(player) == TeamEnum.RED) {
            material = Material.WOOD_HOE;
        } else {
            material = Material.WOOD_AXE;
        }

        int clip_to_use;
        PlayerGuns playerGuns = new PlayerGuns();
        if (playerGuns.getClip(player) >= 99) {
            clip_to_use = 99;
        } else {
            clip_to_use = playerGuns.getClip(player);
        }

        char clip[] = (String.valueOf(clip_to_use).toCharArray());

        if (clip.length == 1) {

            player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_ZERO.getValue()));

            switch (Character.getNumericValue(clip[0])) {
                case 0:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_ZERO.getValue()));
                    break;
            }
        } else {
            switch (Character.getNumericValue(clip[0])) {
                case 0:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(2, new ItemStack(material, 1, Clip.first_ZERO.getValue()));
                    break;

            }

            switch (Character.getNumericValue(clip[1])) {
                case 0:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(4, new ItemStack(material, 1, Clip.second_ZERO.getValue()));
                    break;
            }
        }

        int ammo_to_use;
        if (playerGuns.getAmmo(player) > 99) {
            ammo_to_use = 99;
        } else {
            ammo_to_use = playerGuns.getAmmo(player);
        }

        char ammo[] = (String.valueOf(ammo_to_use).toCharArray());

        if (ammo.length == 1) {

            player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_BLANK.getValue()));

            player.getInventory().setItem(8, new ItemStack(material, 1, Ammo.fifth_BLANK.getValue()));

            switch (Character.getNumericValue(ammo[0])) {
                case 0:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ZERO.getValue()));
                    break;
            }

        } else if (ammo.length == 2) {

            player.getInventory().setItem(8, new ItemStack(material, 1, Ammo.fifth_BLANK.getValue()));

            switch (Character.getNumericValue(ammo[0])) {
                case 0:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ZERO.getValue()));
                    break;
            }

            switch (Character.getNumericValue(ammo[1])) {
                case 0:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_ZERO.getValue()));
                    break;
            }

        } else if (ammo.length == 3) {

            switch (Character.getNumericValue(ammo[0])) {
                case 0:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(6, new ItemStack(material, 1, Ammo.third_ZERO.getValue()));
                    break;
            }

            switch (Character.getNumericValue(ammo[1])) {
                case 0:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(7, new ItemStack(material, 1, Ammo.fourth_ZERO.getValue()));
                    break;
            }

            switch (Character.getNumericValue(ammo[2])) {
                case 0:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_ZERO.getValue()));
                    break;
                case 1:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_ONE.getValue()));
                    break;
                case 2:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_TWO.getValue()));
                    break;
                case 3:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_THREE.getValue()));
                    break;
                case 4:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_FOUR.getValue()));
                    break;
                case 5:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_FIVE.getValue()));
                    break;
                case 6:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_SIX.getValue()));
                    break;
                case 7:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_SEVEN.getValue()));
                    break;
                case 8:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_EIGHT.getValue()));
                    break;
                case 9:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_NINE.getValue()));
                    break;
                default:
                    player.getInventory().setItem(8, new ItemStack(material,  1, Ammo.fifth_BLANK.getValue()));
                    break;

            }

        }
    }

}