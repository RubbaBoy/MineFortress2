package com.uddernetworks.tf2.guns;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.ClassEnum;
import com.uddernetworks.tf2.utils.WeaponType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Gun {

    Main plugin;

    public Gun(Main main) {
        plugin = main;
    }

    public void loadGuns() throws Exception {
        try {
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
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) cols = tmp;
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
            int maxclip;
            int maxammo;
            int cooldown;
            int cooldown_reload;
            boolean tracer;
            boolean sniper;
            int accuracy;
            boolean shotgun;
            int shotgun_bullet;
            ClassEnum classtype;
            boolean classDefault;
            String custom;
            boolean showGUI;
            boolean leftclick;

            boolean error = false;

            HashMap<Boolean, Integer> finished = new HashMap<>();

            for (int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    GunObject gunObject;
                    if (cols != 24) {
                        throw new Exception("The amount of column is wrong in the spreadsheet. Must be 23, it is: " + cols);
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
                            for (GunObject gun : GunList.getGunlist()) {
                                if (cell.toString().equalsIgnoreCase(gun.getName())) {
                                    System.out.println("The name of the gun was the same as another!");
                                    error = true;
                                }
                            }
                            if (!error) {
                                name = cell.toString();
                            } else {
                                name = null;
                            }
                        } else {
                            System.out.println("Name cell was null!");
                            name = null;
                            error = true;
                        }

                        cell = row.getCell((short) 2);
                        if (cell != null) {
                            lore = cell.toString();
                        } else {
                            lore = "";
                        }

                        cell = row.getCell((short) 3);
                        if (cell != null) {
                            if (isInEnum(cell.toString(), Material.class)) {
                                item = Material.getMaterial(cell.toString());
                            } else {
                                System.out.println("Invalid material: " + cell.toString());
                                item = null;
                                error = true;
                            }
                        } else {
                            System.out.println("Material cell was null!");
                            item = null;
                            error = true;
                        }

                        cell = row.getCell((short) 4);
                        if (cell != null) {
                            if (cell.toString().equalsIgnoreCase("NULL")) {
                                sound = null;
                            } else {
                                sound = Sound.valueOf(cell.toString());
                            }
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
                            System.out.println("Scopeable cell was null!");
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
                            maxclip = foo.intValue();
                        } else {
                            System.out.println("Max clip cell was null!");
                            error = true;
                            maxclip = 0;
                        }

                        cell = row.getCell((short) 11);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            Double foo = cell.getNumericCellValue();
                            maxammo = foo.intValue();
                        } else {
                            System.out.println("Max ammo cell was null!");
                            error = true;
                            maxammo = 0;
                        }

                        cell = row.getCell((short) 12);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            Double foo = cell.getNumericCellValue();
                            cooldown = foo.intValue();
                        } else {
                            System.out.println("Cooldown cell was null!");
                            error = true;
                            cooldown = 0;
                        }

                        cell = row.getCell((short) 13);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            Double foo = cell.getNumericCellValue();
                            cooldown_reload = foo.intValue();
                        } else {
                            System.out.println("Reload cooldown cell was null!");
                            error = true;
                            cooldown_reload = 0;
                        }

                        cell = row.getCell((short) 14);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                            tracer = cell.getBooleanCellValue();
                        } else {
                            System.out.println("Tracer cell was null!");
                            error = true;
                            tracer = false;
                        }

                        cell = row.getCell((short) 15);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                            sniper = cell.getBooleanCellValue();
                        } else {
                            System.out.println("Sniper cell was null!");
                            error = true;
                            sniper = false;
                        }

                        cell = row.getCell((short) 16);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            Double foo = cell.getNumericCellValue();
                            accuracy = foo.intValue();
                        } else {
                            System.out.println("Sniper cell was null!");
                            error = true;
                            accuracy = 0;
                        }

                        cell = row.getCell((short) 17);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                            shotgun = cell.getBooleanCellValue();
                        } else {
                            System.out.println("Shotgun cell was null!");
                            error = true;
                            shotgun = false;
                        }

                        cell = row.getCell((short) 18);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            Double foo = cell.getNumericCellValue();
                            shotgun_bullet = foo.intValue();
                        } else {
                            System.out.println("Shotgun bullet cell was null!");
                            error = true;
                            shotgun_bullet = 0;
                        }

                        cell = row.getCell((short) 19);
                        if (cell != null) {
                            classtype = ClassEnum.valueOf(cell.getStringCellValue());
                        } else {
                            System.out.println("Class type cell was null!");
                            error = true;
                            classtype = null;
                        }

                        cell = row.getCell((short) 20);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                            classDefault = cell.getBooleanCellValue();
                        } else {
                            System.out.println("Class default type cell was null!");
                            error = true;
                            classDefault = false;
                        }

                        cell = row.getCell((short) 21);
                        if (cell != null) {
                            if (cell.toString().equalsIgnoreCase("NULL")) {
                                custom = null;
                            } else {
                                custom = cell.toString();
                            }
                        } else {
                            System.out.println("Custom identifier cell was null!");
                            custom = null;
                            error = true;
                        }

                        cell = row.getCell((short) 22);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                            showGUI = cell.getBooleanCellValue();
                        } else {
                            System.out.println("Show GUI cell was null!");
                            showGUI = false;
                            error = true;
                        }

                        cell = row.getCell((short) 23);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                            leftclick = cell.getBooleanCellValue();
                        } else {
                            System.out.println("Left click cell was null!");
                            leftclick = false;
                            error = true;
                        }

                        if (!error) {
                            if (finished.containsKey(true)) {
                                finished.put(true, finished.get(true) + 1);
                            } else {
                                finished.put(true, 1);
                            }
                            gunObject = new GunObject(type, name, lore, item, sound, power, damage, KZR, scopeable, NVscope, maxclip, maxammo, cooldown, cooldown_reload, tracer, sniper, accuracy, shotgun, shotgun_bullet, classtype, classDefault, custom, showGUI, leftclick);
                            GunList.registerGun(gunObject);
                            System.out.println("The gun has been created and registered!");
                        } else {
                            if (finished.containsKey(false)) {
                                finished.put(false, finished.get(false) + 1);
                            } else {
                                finished.put(false, 1);
                            }
                            System.out.println("The gun couldn't be created.");
                        }
                    }
                }
            }

            if (finished.containsKey(false)) {
                if (finished.containsKey(true)) {
                    System.out.println(finished.get(true) + "/" + (finished.get(true) + finished.get(false)) + " Guns have been registered!");
                }
            } else {
                if (finished.containsKey(true)) {
                    System.out.println(finished.get(true) + "/" + finished.get(true) + " Guns have been registered!");
                }
            }
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if(e.name().equals(value)) { return true; }
        }
        return false;
    }
}
