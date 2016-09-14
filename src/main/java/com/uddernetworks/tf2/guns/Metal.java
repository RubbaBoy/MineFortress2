package com.uddernetworks.tf2.guns;

public enum Metal {

    Metal_0((short) 1),
    Metal_25((short) 2),
    Metal_50((short) 3),
    Metal_75((short) 4),
    Metal_100((short) 5),
    Metal_125((short) 6),
    Metal_150((short) 7),
    Metal_175((short) 8),
    Metal_200((short) 9);

    private short data;

    Metal(short data) {
        this.data = data;
    }

    public short getValue() {
        return data;
    }

}
