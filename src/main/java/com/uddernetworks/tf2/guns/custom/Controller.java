package com.uddernetworks.tf2.guns.custom;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.guns.GunObject;
import com.uddernetworks.tf2.guns.custom.demoman.Demoman;
import com.uddernetworks.tf2.guns.custom.engineer.Engineer;
import com.uddernetworks.tf2.guns.custom.medic.Medic;
import com.uddernetworks.tf2.guns.custom.pyro.Pyro;
import com.uddernetworks.tf2.guns.custom.soldier.Soldier;
import com.uddernetworks.tf2.guns.custom.spy.Spy;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Controller {

    private GunObject gun;
    private Player player;
    private boolean held;
    private static HashMap<Player, Boolean> canproceed = new HashMap<>();

    public Controller(GunObject gun, Player player, boolean held) {
        this.gun = gun;
        this.player = player;
        this.held = held;
    }

    public void run() throws Exception {
        Controller controller = getParentAbility();
        controller.runAbility();
    }

    public void sendStopNotify() throws Exception {
        Controller controller = getParentAbility();
        controller.sendStopNotify();
    }

    public GunObject getGun() {
        return gun;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isHeald() {
        return held;
    }

    public Controller getParentAbility() throws Exception {
        if (gun.getCustom() == null) {
            return null;
        }
        String parent = gun.getCustom().split(Pattern.quote("."))[0];
        if (parent.equalsIgnoreCase("spy")) {
            return new Spy(gun, player, held);
        } else if (parent.equalsIgnoreCase("medic")) {
            return new Medic(gun, player, held);
        } else if (parent.equalsIgnoreCase("demoman")) {
            return new Demoman(gun, player, held);
        } else if (parent.equalsIgnoreCase("engineer")) {
            return new Engineer(gun, player, held);
        } else if (parent.equalsIgnoreCase("pyro")) {
            return new Pyro(gun, player, held);
        } else if (parent.equalsIgnoreCase("soldier")) {
            return new Soldier(gun, player, held);
        } else {
            System.out.println("Parent custom identifier not recognised");
            return null;
        }
    }

    public ArrayList<String> getChildAbilities() throws Exception {
        try {
            if (gun.getCustom() == null) {
                return null;
            }
            String[] children = gun.getCustom().split(Pattern.quote("."));
            if (children.length == 1) {
                System.out.println("No children attached to parent ability");
            }
            ArrayList<String> toReturn = new ArrayList<>();
            for (int i = 0; i < children.length; i++) {
                if (i != 0) {
                    toReturn.add(children[i]);
                }
            }
            return toReturn;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public boolean canProceed() {
        try {
            if (!Controller.canproceed.containsKey(player)) {
                return true;
            }
            return Controller.canproceed.get(player);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setCanProceed(boolean canproceed) {
        try {
            Controller.canproceed.put(player, canproceed);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public void runAbility() {}

}