package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.main.Main;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.io.File;
import java.io.FileInputStream;

public class Gun {

    Main plugin;

    public Gun(Main main) {
        plugin = main;
    }

    public void loadGuns() throws Exception {

        File file = new File(plugin.getDataFolder(), "guns.xlsx");
        FileInputStream fs = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fs);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;


        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for(int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if(row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if(tmp > cols) cols = tmp;
            }
        }

        String name;
        String lore;
        Material item;
        Sound sound;
        double damage;

        boolean error = false;

        for(int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            if(row != null) {
                GunObject gunObject;
                if (cols != 5) {
                     throw new Exception("The amount of column is wrong in the spreadsheet. Must be 5");
                } else {

                    cell = row.getCell((short) 0);
                    if (cell != null) {
                        name = cell.toString();
                    } else {
                        System.out.println("Name cell was null!");
                        name = null;
                        error = true;
                    }

                    cell = row.getCell((short) 1);
                    if (cell != null) {
                        lore = cell.toString();
                    } else {
                        System.out.println("Lore cell was null!");
                        lore = null;
                        error = true;
                    }

                    cell = row.getCell((short) 2);
                    if (cell != null) {
                        item = Material.getMaterial(cell.toString());
                    } else {
                        System.out.println("Material cell was null!");
                        item = null;
                        error = true;
                    }

                    cell = row.getCell((short) 3);
                    if (cell != null) {
                        sound = Sound.valueOf(cell.toString());
                    } else {
                        System.out.println("Sound cell was null!");
                        sound = null;
                        error = true;
                    }

                    cell = row.getCell((short) 4);
                    if (cell != null) {
                        damage = Double.parseDouble(cell.toString());
                    } else {
                        System.out.println("Damage cell was null!");
                        damage = 0;
                        error = true;
                    }

                    if (!error) {
                        gunObject = new GunObject(name, lore, item, sound, damage);
                        GunList.registerGun(gunObject);
                        System.out.println("The gun has been created and registered!");
                    } else {
                        System.out.println("Stuff happened. The gun couldn't be created.");
                    }
                }
            }
        }

        System.out.println("All guns have been registered! Number: " + GunList.getGunlist().size());

    }

}
