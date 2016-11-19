package com.uddernetworks.tf2.gui;

import com.uddernetworks.tf2.arena.PlayerTeams;
import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.Ammo;
import com.uddernetworks.tf2.guns.Clip;
import com.uddernetworks.tf2.guns.PlayerGuns;
import com.uddernetworks.tf2.utils.TeamEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BulletGUI {

    public BulletGUI(Player player) {
        try {
            
            ItemStack first = new ItemStack(Material.AIR);
            ItemStack second = new ItemStack(Material.AIR);
            ItemStack third = new ItemStack(Material.AIR);
            ItemStack fourth = new ItemStack(Material.AIR);
            ItemStack fifth = new ItemStack(Material.AIR);
            
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

                first = new ItemStack(material, 1, Clip.first_ZERO.getValue());

                switch (Character.getNumericValue(clip[0])) {
                    case 0:
                        second = new ItemStack(material, 1, Clip.second_ZERO.getValue());
                        break;
                    case 1:
                        second = new ItemStack(material, 1, Clip.second_ONE.getValue());
                        break;
                    case 2:
                        second = new ItemStack(material, 1, Clip.second_TWO.getValue());
                        break;
                    case 3:
                        second = new ItemStack(material, 1, Clip.second_THREE.getValue());
                        break;
                    case 4:
                        second = new ItemStack(material, 1, Clip.second_FOUR.getValue());
                        break;
                    case 5:
                        second = new ItemStack(material, 1, Clip.second_FIVE.getValue());
                        break;
                    case 6:
                        second = new ItemStack(material, 1, Clip.second_SIX.getValue());
                        break;
                    case 7:
                        second = new ItemStack(material, 1, Clip.second_SEVEN.getValue());
                        break;
                    case 8:
                        second = new ItemStack(material, 1, Clip.second_EIGHT.getValue());
                        break;
                    case 9:
                        second = new ItemStack(material, 1, Clip.second_NINE.getValue());
                        break;
                    default:
                        second = new ItemStack(material, 1, Clip.second_ZERO.getValue());
                        break;
                }
            } else {
                switch (Character.getNumericValue(clip[0])) {
                    case 0:
                        first = new ItemStack(material, 1, Clip.first_ZERO.getValue());
                        break;
                    case 1:
                        first = new ItemStack(material, 1, Clip.first_ONE.getValue());
                        break;
                    case 2:
                        first = new ItemStack(material, 1, Clip.first_TWO.getValue());
                        break;
                    case 3:
                        first = new ItemStack(material, 1, Clip.first_THREE.getValue());
                        break;
                    case 4:
                        first = new ItemStack(material, 1, Clip.first_FOUR.getValue());
                        break;
                    case 5:
                        first = new ItemStack(material, 1, Clip.first_FIVE.getValue());
                        break;
                    case 6:
                        first = new ItemStack(material, 1, Clip.first_SIX.getValue());
                        break;
                    case 7:
                        first = new ItemStack(material, 1, Clip.first_SEVEN.getValue());
                        break;
                    case 8:
                        first = new ItemStack(material, 1, Clip.first_EIGHT.getValue());
                        break;
                    case 9:
                        first = new ItemStack(material, 1, Clip.first_NINE.getValue());
                        break;
                    default:
                        first = new ItemStack(material, 1, Clip.first_ZERO.getValue());
                        break;

                }

                switch (Character.getNumericValue(clip[1])) {
                    case 0:
                        second = new ItemStack(material, 1, Clip.second_ZERO.getValue());
                        break;
                    case 1:
                        second = new ItemStack(material, 1, Clip.second_ONE.getValue());
                        break;
                    case 2:
                        second = new ItemStack(material, 1, Clip.second_TWO.getValue());
                        break;
                    case 3:
                        second = new ItemStack(material, 1, Clip.second_THREE.getValue());
                        break;
                    case 4:
                        second = new ItemStack(material, 1, Clip.second_FOUR.getValue());
                        break;
                    case 5:
                        second = new ItemStack(material, 1, Clip.second_FIVE.getValue());
                        break;
                    case 6:
                        second = new ItemStack(material, 1, Clip.second_SIX.getValue());
                        break;
                    case 7:
                        second = new ItemStack(material, 1, Clip.second_SEVEN.getValue());
                        break;
                    case 8:
                        second = new ItemStack(material, 1, Clip.second_EIGHT.getValue());
                        break;
                    case 9:
                        second = new ItemStack(material, 1, Clip.second_NINE.getValue());
                        break;
                    default:
                        second = new ItemStack(material, 1, Clip.second_ZERO.getValue());
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

                fourth = new ItemStack(material, 1, Ammo.fourth_BLANK.getValue());

                fifth = new ItemStack(material, 1, Ammo.fifth_BLANK.getValue());

                switch (Character.getNumericValue(ammo[0])) {
                    case 0:
                        third = new ItemStack(material, 1, Ammo.third_ZERO.getValue());
                        break;
                    case 1:
                        third = new ItemStack(material, 1, Ammo.third_ONE.getValue());
                        break;
                    case 2:
                        third = new ItemStack(material, 1, Ammo.third_TWO.getValue());
                        break;
                    case 3:
                        third = new ItemStack(material, 1, Ammo.third_THREE.getValue());
                        break;
                    case 4:
                        third = new ItemStack(material, 1, Ammo.third_FOUR.getValue());
                        break;
                    case 5:
                        third = new ItemStack(material, 1, Ammo.third_FIVE.getValue());
                        break;
                    case 6:
                        third = new ItemStack(material, 1, Ammo.third_SIX.getValue());
                        break;
                    case 7:
                        third = new ItemStack(material, 1, Ammo.third_SEVEN.getValue());
                        break;
                    case 8:
                        third = new ItemStack(material, 1, Ammo.third_EIGHT.getValue());
                        break;
                    case 9:
                        third = new ItemStack(material, 1, Ammo.third_NINE.getValue());
                        break;
                    default:
                        third = new ItemStack(material, 1, Ammo.third_ZERO.getValue());
                        break;
                }

            } else if (ammo.length == 2) {

                fifth = new ItemStack(material, 1, Ammo.fifth_BLANK.getValue());

                switch (Character.getNumericValue(ammo[0])) {
                    case 0:
                        third = new ItemStack(material, 1, Ammo.third_ZERO.getValue());
                        break;
                    case 1:
                        third = new ItemStack(material, 1, Ammo.third_ONE.getValue());
                        break;
                    case 2:
                        third = new ItemStack(material, 1, Ammo.third_TWO.getValue());
                        break;
                    case 3:
                        third = new ItemStack(material, 1, Ammo.third_THREE.getValue());
                        break;
                    case 4:
                        third = new ItemStack(material, 1, Ammo.third_FOUR.getValue());
                        break;
                    case 5:
                        third = new ItemStack(material, 1, Ammo.third_FIVE.getValue());
                        break;
                    case 6:
                        third = new ItemStack(material, 1, Ammo.third_SIX.getValue());
                        break;
                    case 7:
                        third = new ItemStack(material, 1, Ammo.third_SEVEN.getValue());
                        break;
                    case 8:
                        third = new ItemStack(material, 1, Ammo.third_EIGHT.getValue());
                        break;
                    case 9:
                        third = new ItemStack(material, 1, Ammo.third_NINE.getValue());
                        break;
                    default:
                        third = new ItemStack(material, 1, Ammo.third_ZERO.getValue());
                        break;
                }

                switch (Character.getNumericValue(ammo[1])) {
                    case 0:
                        fourth = new ItemStack(material, 1, Ammo.fourth_ZERO.getValue());
                        break;
                    case 1:
                        System.out.println("fourth is 1?? ammo is: " + Arrays.toString(ammo) + " in string: " + ammo_to_use);
                        fourth = new ItemStack(material, 1, Ammo.fourth_ONE.getValue());
                        break;
                    case 2:
                        fourth = new ItemStack(material, 1, Ammo.fourth_TWO.getValue());
                        break;
                    case 3:
                        fourth = new ItemStack(material, 1, Ammo.fourth_THREE.getValue());
                        break;
                    case 4:
                        fourth = new ItemStack(material, 1, Ammo.fourth_FOUR.getValue());
                        break;
                    case 5:
                        fourth = new ItemStack(material, 1, Ammo.fourth_FIVE.getValue());
                        break;
                    case 6:
                        fourth = new ItemStack(material, 1, Ammo.fourth_SIX.getValue());
                        break;
                    case 7:
                        fourth = new ItemStack(material, 1, Ammo.fourth_SEVEN.getValue());
                        break;
                    case 8:
                        fourth = new ItemStack(material, 1, Ammo.fourth_EIGHT.getValue());
                        break;
                    case 9:
                        fourth = new ItemStack(material, 1, Ammo.fourth_NINE.getValue());
                        break;
                    default:
                        fourth = new ItemStack(material, 1, Ammo.fourth_ZERO.getValue());
                        break;
                }

            } else if (ammo.length == 3) {

                switch (Character.getNumericValue(ammo[0])) {
                    case 0:
                        third = new ItemStack(material, 1, Ammo.third_ZERO.getValue());
                        break;
                    case 1:
                        third = new ItemStack(material, 1, Ammo.third_ONE.getValue());
                        break;
                    case 2:
                        third = new ItemStack(material, 1, Ammo.third_TWO.getValue());
                        break;
                    case 3:
                        third = new ItemStack(material, 1, Ammo.third_THREE.getValue());
                        break;
                    case 4:
                        third = new ItemStack(material, 1, Ammo.third_FOUR.getValue());
                        break;
                    case 5:
                        third = new ItemStack(material, 1, Ammo.third_FIVE.getValue());
                        break;
                    case 6:
                        third = new ItemStack(material, 1, Ammo.third_SIX.getValue());
                        break;
                    case 7:
                        third = new ItemStack(material, 1, Ammo.third_SEVEN.getValue());
                        break;
                    case 8:
                        third = new ItemStack(material, 1, Ammo.third_EIGHT.getValue());
                        break;
                    case 9:
                        third = new ItemStack(material, 1, Ammo.third_NINE.getValue());
                        break;
                    default:
                        third = new ItemStack(material, 1, Ammo.third_ZERO.getValue());
                        break;
                }

                switch (Character.getNumericValue(ammo[1])) {
                    case 0:
                        fourth = new ItemStack(material, 1, Ammo.fourth_ZERO.getValue());
                        break;
                    case 1:
                        fourth = new ItemStack(material, 1, Ammo.fourth_ONE.getValue());
                        break;
                    case 2:
                        fourth = new ItemStack(material, 1, Ammo.fourth_TWO.getValue());
                        break;
                    case 3:
                        fourth = new ItemStack(material, 1, Ammo.fourth_THREE.getValue());
                        break;
                    case 4:
                        fourth = new ItemStack(material, 1, Ammo.fourth_FOUR.getValue());
                        break;
                    case 5:
                        fourth = new ItemStack(material, 1, Ammo.fourth_FIVE.getValue());
                        break;
                    case 6:
                        fourth = new ItemStack(material, 1, Ammo.fourth_SIX.getValue());
                        break;
                    case 7:
                        fourth = new ItemStack(material, 1, Ammo.fourth_SEVEN.getValue());
                        break;
                    case 8:
                        fourth = new ItemStack(material, 1, Ammo.fourth_EIGHT.getValue());
                        break;
                    case 9:
                        fourth = new ItemStack(material, 1, Ammo.fourth_NINE.getValue());
                        break;
                    default:
                        fourth = new ItemStack(material, 1, Ammo.fourth_ZERO.getValue());
                        break;
                }

                switch (Character.getNumericValue(ammo[2])) {
                    case 0:
                        fifth = new ItemStack(material, 1, Ammo.fifth_ZERO.getValue());
                        break;
                    case 1:
                        fifth = new ItemStack(material, 1, Ammo.fifth_ONE.getValue());
                        break;
                    case 2:
                        fifth = new ItemStack(material, 1, Ammo.fifth_TWO.getValue());
                        break;
                    case 3:
                        fifth = new ItemStack(material, 1, Ammo.fifth_THREE.getValue());
                        break;
                    case 4:
                        fifth = new ItemStack(material, 1, Ammo.fifth_FOUR.getValue());
                        break;
                    case 5:
                        fifth = new ItemStack(material, 1, Ammo.fifth_FIVE.getValue());
                        break;
                    case 6:
                        fifth = new ItemStack(material, 1, Ammo.fifth_SIX.getValue());
                        break;
                    case 7:
                        fifth = new ItemStack(material, 1, Ammo.fifth_SEVEN.getValue());
                        break;
                    case 8:
                        fifth = new ItemStack(material, 1, Ammo.fifth_EIGHT.getValue());
                        break;
                    case 9:
                        fifth = new ItemStack(material, 1, Ammo.fifth_NINE.getValue());
                        break;
                    default:
                        fifth = new ItemStack(material, 1, Ammo.fifth_BLANK.getValue());
                        break;

                }

            }

            first = setUnbreakable(first.clone());
            second = setUnbreakable(second.clone());
            third = setUnbreakable(third.clone());
            fourth = setUnbreakable(fourth.clone());
            fifth = setUnbreakable(fifth.clone());

            player.getInventory().setItem(2, first);
            player.getInventory().setItem(4, second);
            player.getInventory().setItem(6, third);
            player.getInventory().setItem(7, fourth);
            player.getInventory().setItem(8, fifth);
            
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    private ItemStack setUnbreakable(ItemStack item) {
        ItemStack toreturn;
        net.minecraft.server.v1_10_R1.ItemStack stack = org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack.asNMSCopy(item.clone());
        net.minecraft.server.v1_10_R1.NBTTagCompound tag = new net.minecraft.server.v1_10_R1.NBTTagCompound();
        tag.setBoolean("Unbreakable", true);
        stack.setTag(tag);

        toreturn = org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack.asCraftMirror(stack);

        ItemMeta meta = toreturn.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        toreturn.setItemMeta(meta);

        return toreturn;
    }

}