package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.WeaponType;
import org.apache.poi.ss.usermodel.Cell;
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

        WeaponType type;
        String name;
        String lore;
        Material item;
        Sound sound;
        double power;
        double damage;
        int KZR;
        boolean scopeable;
        boolean NVscope;
        int clip;
        int ammo;
        int maxclip;
        int maxammo;
        int cooldown;
        int cooldown_reload;
        boolean tracer;
        boolean sniper;
        int accuracy;

        boolean error = false;

        for(int r = 1; r < rows; r++) {
            row = sheet.getRow(r);
            if(row != null) {
                GunObject gunObject;
                if (cols != 19) {
                     throw new Exception("The amount of column is wrong in the spreadsheet. Must be 18");
                } else {

                    cell = row.getCell((short) 0);
                    if (cell != null) {
                        type = WeaponType.valueOf(cell.toString());
                    } else {
                        System.out.println("Weapon type cell was null!");
                        type = null;
                        error = true;
                    }

                    cell = row.getCell((short) 1);
                    if (cell != null) {
                        name = cell.toString();
                    } else {
                        System.out.println("Name cell was null!");
                        name = null;
                        error = true;
                    }

                    cell = row.getCell((short) 2);
                    if (cell != null) {
                        lore = cell.toString();
                    } else {
                        System.out.println("Lore cell was null!");
                        lore = null;
                        error = true;
                    }

                    cell = row.getCell((short) 3);
                    if (cell != null) {
                        item = Material.getMaterial(cell.toString());
                    } else {
                        System.out.println("Material cell was null!");
                        item = null;
                        error = true;
                    }

                    cell = row.getCell((short) 4);
                    if (cell != null) {
                        sound = Sound.valueOf(cell.toString());
                    } else {
                        System.out.println("Sound cell was null!");
                        sound = null;
                        error = true;
                    }

                    cell = row.getCell((short) 5);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        power = cell.getNumericCellValue();
                    } else {
                        System.out.println("Power cell was null!");
                        power = 0;
                        error = true;
                    }

                    cell = row.getCell((short) 6);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        damage = cell.getNumericCellValue();
                    } else {
                        System.out.println("Damage cell was null!");
                        damage = 0;
                        error = true;
                    }

                    cell = row.getCell((short) 7);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        KZR = foo.intValue();
                    } else {
                        System.out.println("KZR cell was null!");
                        KZR = 0;
                        error = true;
                    }

                    cell = row.getCell((short) 8);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        scopeable = cell.getBooleanCellValue();
                    } else {
                        System.out.println("KZR cell was null!");
                        error = true;
                        scopeable = false;
                    }

                    cell = row.getCell((short) 9);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        NVscope = cell.getBooleanCellValue();
                    } else {
                        System.out.println("NVScope cell was null!");
                        error = true;
                        NVscope = false;
                    }

                    cell = row.getCell((short) 10);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        clip = foo.intValue();
                    } else {
                        System.out.println("Clip cell was null!");
                        error = true;
                        clip = 0;
                    }

                    cell = row.getCell((short) 11);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        ammo = foo.intValue();
                    } else {
                        System.out.println("Ammo cell was null!");
                        error = true;
                        ammo = 0;
                    }

                    cell = row.getCell((short) 12);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        maxclip = foo.intValue();
                    } else {
                        System.out.println("Max clip cell was null!");
                        error = true;
                        maxclip = 0;
                    }

                    cell = row.getCell((short) 13);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        maxammo = foo.intValue();
                    } else {
                        System.out.println("Max ammo cell was null!");
                        error = true;
                        maxammo = 0;
                    }

                    cell = row.getCell((short) 14);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        cooldown = foo.intValue();
                    } else {
                        System.out.println("Cooldown cell was null!");
                        error = true;
                        cooldown = 0;
                    }

                    cell = row.getCell((short) 15);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        cooldown_reload = foo.intValue();
                    } else {
                        System.out.println("Reload cooldown cell was null!");
                        error = true;
                        cooldown_reload = 0;
                    }

                    cell = row.getCell((short) 16);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        tracer = cell.getBooleanCellValue();
                    } else {
                        System.out.println("Tracer cell was null!");
                        error = true;
                        tracer = false;
                    }

                    cell = row.getCell((short) 17);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        sniper = cell.getBooleanCellValue();
                    } else {
                        System.out.println("Sniper cell was null!");
                        error = true;
                        sniper = false;
                    }

                    cell = row.getCell((short) 18);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        Double foo = cell.getNumericCellValue();
                        accuracy = foo.intValue();
                    } else {
                        System.out.println("Sniper cell was null!");
                        error = true;
                        accuracy = 0;
                    }

                    if (!error) {
                        gunObject = new GunObject(type, name, lore, item, sound, power, damage, KZR, scopeable, NVscope, clip, ammo, maxclip, maxammo, cooldown, cooldown_reload, tracer, sniper, accuracy);
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
