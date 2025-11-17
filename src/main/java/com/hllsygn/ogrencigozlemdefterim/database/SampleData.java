package com.hllsygn.ogrencigozlemdefterim.database;

/**
 * Sample data for the student observation application
 */
public enum SampleData {
    // 5A Class Students
    STUDENT_1(1001, "Ahmet", "YILMAZ", 5, "A"),
    STUDENT_2(1002, "Mehmet", "KAYA", 5, "A"),
    STUDENT_3(1003, "Ayşe", "DEMİR", 5, "A"),
    STUDENT_4(1004, "Fatma", "ŞAHİN", 5, "A"),
    STUDENT_5(1005, "Ali", "YILDIZ", 5, "A"),
    
    // 6A Class Students
    STUDENT_6(2001, "Emre", "ÖZTÜRK", 6, "A"),
    STUDENT_7(2002, "Elif", "AKTÜRK", 6, "A"),
    STUDENT_8(2003, "Can", "KARADAĞ", 6, "A"),
    STUDENT_9(2004, "Ece", "GÜLER", 6, "A"),
    STUDENT_10(2005, "Barış", "KORKMAZ", 6, "A"),
    
    // 7A Class Students
    STUDENT_11(3001, "Deniz", "KAPLAN", 7, "A"),
    STUDENT_12(3002, "Zeynep", "ERDOĞAN", 7, "A"),
    STUDENT_13(3003, "Kaan", "IŞIK", 7, "A"),
    STUDENT_14(3004, "Selin", "TEKİN", 7, "A"),
    STUDENT_15(3005, "Umut", "AKIN", 7, "A"),
    
    // 8A Class Students
    STUDENT_16(4001, "Cem", "KILIÇ", 8, "A"),
    STUDENT_17(4002, "İrem", "KÖSE", 8, "A"),
    STUDENT_18(4003, "Kerem", "AYDIN", 8, "A"),
    STUDENT_19(4004, "Naz", "TÜRK", 8, "A"),
    STUDENT_20(4005, "Alp", "EREN", 8, "A");
    
    private final int no;
    private final String ad;
    private final String soyad;
    private final int sinif;
    private final String sube;
    
    SampleData(int no, String ad, String soyad, int sinif, String sube) {
        this.no = no;
        this.ad = ad;
        this.soyad = soyad;
        this.sinif = sinif;
        this.sube = sube;
    }
    
    public int getNo() {
        return no;
    }
    
    public String getAd() {
        return ad;
    }
    
    public String getSoyad() {
        return soyad;
    }
    
    public int getSinif() {
        return sinif;
    }
    
    public String getSube() {
        return sube;
    }
    
    public String getAdSoyad() {
        return ad + " " + soyad;
    }
    
    /**
     * Get all sample classes
     */
    public static String[] getSampleClasses() {
        return new String[]{"5A", "6A", "7A", "8A","9A","10A"};
    }
}