/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hllsygn.ogrencigozlemdefterim.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ogrenci {

    private final IntegerProperty no; // primary key
    private StringProperty ad;
    private StringProperty soyad;
    private IntegerProperty sinif;
    private StringProperty sube;
    

    public Ogrenci(int no, String ad, String soyad, int sinif, String sube) {
        this.no = new SimpleIntegerProperty(no);
        this.ad = new SimpleStringProperty(ad);
        this.soyad = new SimpleStringProperty(soyad);
        this.sinif = new SimpleIntegerProperty(sinif);
        this.sube = new SimpleStringProperty(sube);
    }
    

    public int getNo() { return this.no.get();}
    public String getAd() {return this.ad.get();}
    public void setAd(String ad) {this.ad.set(ad);}
    public String getSoyad() {return this.soyad.get();}
    public void setSoyad(String soyad) {this.soyad.set(soyad);}
    public int getSinif() { return this.sinif.get();}
    public void setSinif(int sinif) {this.sinif.set(sinif);}
    public String getSube() { return this.sube.get();}
    public void setSube(String sube) {this.sube.set(sube);}
    
   
}
