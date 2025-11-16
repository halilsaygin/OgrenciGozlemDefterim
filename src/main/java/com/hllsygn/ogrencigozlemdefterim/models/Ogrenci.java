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
    private StringProperty adSoyad;
    private IntegerProperty sinif;
    private StringProperty sube;
    private IntegerProperty artiSayisi;
    private IntegerProperty eksiSayisi;
    

    public Ogrenci(int no, String adSoyad, int sinif, String sube) {
        this.no = new SimpleIntegerProperty(no);
        this.adSoyad = new SimpleStringProperty(adSoyad);
        this.sinif = new SimpleIntegerProperty(sinif);
        this.sube = new SimpleStringProperty(sube);
        this.artiSayisi = new SimpleIntegerProperty(0); // varsayılan olarak 0
        this.eksiSayisi = new SimpleIntegerProperty(0); // varsayılan olarak 0
    }
    
     public Ogrenci(int no, String adSoyad, int sinif, String sube,int artiSayisi, int eksiSayisi) {
        this.no = new SimpleIntegerProperty(no);
        this.adSoyad = new SimpleStringProperty(adSoyad);
        this.sinif = new SimpleIntegerProperty(sinif);
        this.sube = new SimpleStringProperty(sube);
        this.artiSayisi = new SimpleIntegerProperty(artiSayisi); 
        this.eksiSayisi = new SimpleIntegerProperty(eksiSayisi); 
    }

    public int getNo() { return this.no.get();}
    public String getAdSoyad() {return this.adSoyad.get();}
    public void setAdSoyad(String adSoyad) {this.adSoyad = new SimpleStringProperty(adSoyad);}
    public int getSinif() { return this.sinif.get();}
    public void setSinif(int sinif) {this.sinif = new SimpleIntegerProperty(sinif);}
    public String getSube() { return this.sube.get();}
    public void setSube(String sube) {this.sube = new SimpleStringProperty(sube);}
    
    public void setArtiSayisi(int artiSayisi) {
        this.artiSayisi = new SimpleIntegerProperty(artiSayisi);
    }
    
    public void artiEkle(int artiSayisi){
        this.artiSayisi = new SimpleIntegerProperty(artiSayisi+1);
    }
    
    public void eksiEkle(int eksiSayisi){
         this.eksiSayisi = new SimpleIntegerProperty(eksiSayisi+1);
    }
    
    public int getArtiSayisi(){
        return this.artiSayisi.get();
    }

    public void setEksiSayisi( int eksiSayisi) {
        this.eksiSayisi = new SimpleIntegerProperty(eksiSayisi);
    }
    
    public int getEksiSayisi(){
        return this.eksiSayisi.get();
    }
   
}
