package com.hllsygn.ogrencigozlemdefterim.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ogrenci {

    private final IntegerProperty no;
    private final StringProperty ad;
    private final StringProperty soyad;
    private final IntegerProperty sinif;
    private final StringProperty sube;

    public Ogrenci(int no, String ad, String soyad, int sinif, String sube) {
        this.no = new SimpleIntegerProperty(no);
        this.ad = new SimpleStringProperty(ad);
        this.soyad = new SimpleStringProperty(soyad);
        this.sinif = new SimpleIntegerProperty(sinif);
        this.sube = new SimpleStringProperty(sube);
    }

    public int getNo() { return no.get(); }
    public String getAd() { return ad.get(); }
    public void setAd(String ad) { this.ad.set(ad); }
    public String getSoyad() { return soyad.get(); }
    public void setSoyad(String soyad) { this.soyad.set(soyad); }
    public int getSinif() { return sinif.get(); }
    public void setSinif(int sinif) { this.sinif.set(sinif); }
    public String getSube() { return sube.get(); }
    public void setSube(String sube) { this.sube.set(sube); }

    // Property getters for TableView
    public IntegerProperty noProperty() { return no; }
    public StringProperty adProperty() { return ad; }
    public StringProperty soyadProperty() { return soyad; }
    public IntegerProperty sinifProperty() { return sinif; }
    public StringProperty subeProperty() { return sube; }
    
    public String getAdSoyad() {
        return getAd() + " " + getSoyad();
    }
}
