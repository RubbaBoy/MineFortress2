package com.uddernetworks.tf2.guns;

public enum Ammo {

    third_BLANK((short) 22),
    third_ZERO((short) 23),
    third_ONE((short) 24),
    third_TWO((short) 25),
    third_THREE((short) 26),
    third_FOUR((short) 27),
    third_FIVE((short) 28),
    third_SIX((short) 29),
    third_SEVEN((short) 30),
    third_EIGHT((short) 31),
    third_NINE((short) 32),

    fourth_BLANK((short) 33),
    fourth_ZERO((short) 34),
    fourth_ONE((short) 35),
    fourth_TWO((short) 36),
    fourth_THREE((short) 37),
    fourth_FOUR((short) 38),
    fourth_FIVE((short) 39),
    fourth_SIX((short) 40),
    fourth_SEVEN((short) 41),
    fourth_EIGHT((short) 42),
    fourth_NINE((short) 43),

    fifth_BLANK((short) 44),
    fifth_ZERO((short) 45),
    fifth_ONE((short) 46),
    fifth_TWO((short) 47),
    fifth_THREE((short) 48),
    fifth_FOUR((short) 49),
    fifth_FIVE((short) 50),
    fifth_SIX((short) 51),
    fifth_SEVEN((short) 52),
    fifth_EIGHT((short) 53),
    fifth_NINE((short) 54);

    private short data;

    Ammo(short data) {
        this.data = data;
    }

    public short getValue() {
        return data;
    }
}