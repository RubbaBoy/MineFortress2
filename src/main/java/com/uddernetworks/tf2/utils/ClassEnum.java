package com.uddernetworks.tf2.utils;

public enum ClassEnum {

    SCOUT(125, 133),
    SOLDIER(200, 80),
    PYRO(175, 100),
    DEMOMAN(175, 93),
    HEAVY(300, 77),
    ENGINEER(125, 100),
    MEDIC(150, 107),
    SNIPER(125, 100),
    SPY(125, 107);

    private int health;
    private int speed;

    ClassEnum(int health, int speed){
        this.health = health;
        this.speed = speed;
    }

    public int getHealth(){
        return health;
    }

    public int getSpeed() {
        return speed;
    }

}