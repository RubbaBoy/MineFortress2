package com.uddernetworks.tf2.utils;

public class QueryResult {

    private String UUID;
    private String CLASS;
    private String TYPE;
    private int ID;
    private String NAME;

    public QueryResult(String UUID, String CLASS, String TYPE, int ID, String NAME) {
        this.UUID = UUID;
        this.CLASS = CLASS;
        this.TYPE = TYPE;
        this.ID = ID;
        this.NAME = NAME;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getCLASS() {
        return CLASS;
    }

    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
