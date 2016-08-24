package com.uddernetworks.tf2.utils;

public enum ClassEnum {

    SCOUT(125),
    SOLDIER(200),
    PYRO(175),
    DEMOMAN(175),
    HEAVY(300),
    ENGINEER(125),
    MEDIC(150),
    SNIPER(125),
    SPY(125);

    private int health;

    ClassEnum(int health){
        this.health = health;
    }

    public int getHealth(){
        return health;
    }

}