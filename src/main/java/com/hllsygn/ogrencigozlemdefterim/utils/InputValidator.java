package com.hllsygn.ogrencigozlemdefterim.utils;

import java.util.regex.Pattern;

/**
 * Öğrenci kayıt girdilerini doğrulayan validator sınıfı
 */
public class InputValidator {
    
    // Regex pattern'leri
    // Ad: İlk harf büyük, sonraki harfler küçük (Türkçe karakterler dahil)
    // Soyad: Tüm harfler büyük (Türkçe karakterler dahil: A-Z, Ç, Ğ, İ, Ö, Ş, Ü)
    private static final Pattern AD_SOYAD_PATTERN = Pattern.compile("^([A-ZÇĞİÖŞÜ][a-zçğıöşü]+)(\\s[A-ZÇĞİÖŞÜ][a-zçğıöşü]+)?\\s[A-ZÇĞİÖŞÜ]+$");
    // Şube: Türkçe alfabe A-N arası (Ğ hariç): A,B,C,Ç,D,E,F,G,H,I,İ,J,K,L,M,N
    private static final Pattern SUBE_PATTERN = Pattern.compile("^[A-CÇDEFGHİİJKLMN]$");
    private static final Pattern SAYI_PATTERN = Pattern.compile("^\\d+$");
    
    /**
     * Öğrenci ad soyad formatını kontrol eder
     * Format: İlk harfi büyük ad(lar) + Tamamı büyük soyad
     * Örnek: Ali KARAN, Kerem Ali GÜNDÜZ
     * 
     * @param adSoyad Kontrol edilecek ad soyad
     * @return Geçerli ise true
     */
    public static boolean validateAdSoyad(String adSoyad) {
        if (adSoyad == null || adSoyad.trim().isEmpty()) {
            return false;
        }
        return AD_SOYAD_PATTERN.matcher(adSoyad.trim()).matches();
    }
    
    /**
     * Sınıf numarasını kontrol eder (1-12 arası)
     * 
     * @param sinif Kontrol edilecek sınıf
     * @return Geçerli ise true
     */
    public static boolean validateSinif(String sinif) {
        if (sinif == null || sinif.trim().isEmpty()) {
            return false;
        }
        
        if (!SAYI_PATTERN.matcher(sinif.trim()).matches()) {
            return false;
        }
        
        try {
            int sinifNo = Integer.parseInt(sinif.trim());
            return sinifNo >= 1 && sinifNo <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Şube harfini kontrol eder
     * Türkçe alfabe A-N arası (Ğ hariç): A,B,C,Ç,D,E,F,G,H,I,İ,J,K,L,M,N
     * 
     * @param sube Kontrol edilecek şube
     * @return Geçerli ise true
     */
    public static boolean validateSube(String sube) {
        if (sube == null || sube.trim().isEmpty()) {
            return false;
        }
        return SUBE_PATTERN.matcher(sube.trim()).matches();
    }
    
    /**
     * Öğrenci numarasını kontrol eder (Sayı olmalı)
     * 
     * @param ogrenciNo Kontrol edilecek öğrenci numarası
     * @return Geçerli ise true
     */
    public static boolean validateOgrenciNo(String ogrenciNo) {
        if (ogrenciNo == null || ogrenciNo.trim().isEmpty()) {
            return false;
        }
        return SAYI_PATTERN.matcher(ogrenciNo.trim()).matches();
    }
    
}
