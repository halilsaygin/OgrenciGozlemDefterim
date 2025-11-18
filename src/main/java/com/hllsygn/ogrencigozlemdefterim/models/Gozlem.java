package com.hllsygn.ogrencigozlemdefterim.models;

public class Gozlem {

    private int gozlemId;
    private int ogrenciNo;
    private String gozlemMetni;

    public Gozlem() {
    }

    public Gozlem(int gozlemId, int ogrenciNo, String gozlemMetni) {
        this.gozlemId = gozlemId;
        this.ogrenciNo = ogrenciNo;
        this.gozlemMetni = gozlemMetni;
    }

    public int getGozlemId() {
        return gozlemId;
    }

    public void setGozlemId(int gozlemId) {
        this.gozlemId = gozlemId;
    }

    public int getOgrenciNo() {
        return ogrenciNo;
    }

    public void setOgrenciNo(int ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
    }

    public String getGozlemMetni() {
        return gozlemMetni;
    }

    public void setGozlemMetni(String gozlemMetni) {
        this.gozlemMetni = gozlemMetni;
    }
}
