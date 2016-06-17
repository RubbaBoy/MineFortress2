package com.uddernetworks.tf2.guns;

public enum Clip {

    first_ONE((short) 1),
    first_TWO((short) 2),
    first_THREE((short) 3),
    first_FOUR((short) 4),
    first_FIVE((short) 5),
    first_SIX((short) 6),
    first_SEVEN((short) 7),
    first_EIGHT((short) 8),
    first_NINE((short) 9),
    first_ZERO((short) 10),

    second_ONE((short) 11),
    second_TWO((short) 12),
    second_THREE((short) 13),
    second_FOUR((short) 14),
    second_FIVE((short) 15),
    second_SIX((short) 16),
    second_SEVEN((short) 17),
    second_EIGHT((short) 18),
    second_NINE((short) 19),
    second_ZERO((short) 20),
    second_BLANK((short) 21);

    private short data;

    Clip(short data) {
        this.data = data;
    }

    public short getValue() {
        return data;
    }
}