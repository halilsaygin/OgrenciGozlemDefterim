package com.hllsygn.ogrencigozlemdefterim.utils;

/**
 * Uygulama genelinde kullanılan alert mesajlarını tanımlayan enum sınıfı
 */
public enum AlertMessage {
    // Başarı Mesajları
    GOZLEM_KAYDEDILDI("Başarılı", "Gözlem başarıyla kaydedildi."),
    GOZLEM_GUNCELLENDI("Başarılı", "Gözlem başarıyla güncellendi."),
    OGRENCI_GUNCELLENDI("Başarılı", "Öğrenci bilgileri güncellendi."),
    OGRENCI_SILINDI("Başarılı", "Öğrenci başarıyla silindi."),
    TUM_KAYITLAR_SILINDI("İşlem Başarılı", "Tüm kayıtlar başarıyla silindi."),
    YENI_YIL_TAMAMLANDI("İşlem Başarılı", "Yeni yıl geçişi başarıyla tamamlandı."),
    
    // Hata Mesajları
    OGRENCI_SECILMEDI("Hata", "Lütfen bir öğrenci seçin."),
    GOZLEM_BOS("Hata", "Gözlem metni boş olamaz."),
    TUM_ALANLAR_DOLU("Hata", "Tüm alanlar doldurulmalıdır."),
    OGRENCI_MEVCUT("Hata", "Bu numaraya sahip bir öğrenci zaten mevcut."),
    OGRENCI_SECILMEDI_GUNCELLEME("Hata", "Lütfen güncellenecek bir öğrenci seçin."),
    OGRENCI_SECILMEDI_SILME("Hata", "Lütfen silinecek bir öğrenci seçin."),
    ANA_SAHNE_HATA("Hata", "Ana sahneye dönülürken bir hata oluştu."),
    UYGULAMA_BASLAMA_HATASI("Uygulama Başlatma Hatası", "Uygulama başlatılırken bir hata oluştu.\nDetaylar için error_log.txt dosyasını kontrol edin."),
    OGRENCI_NO_DEGISTIRILDI("Uyarı", """
            Öğrenci seçili iken öğrenci numarası değiştirdiniz.
            
            Bu işlem mevcut öğrenciyi güncellemez, yeni kayıt oluşturur.
            
            Eğer mevcut öğrencinin öğrenci numarası hatalı ise:
            - Önce öğrenciyi silip tekrar giriniz
            
            Diğer alanları değiştirmek için:
            - Düzenleme yapıp Güncelle butonunu kullanabilirsiniz
            - Bu mevcut öğrencinin bilgilerini güncelleyecektir"""),
    
    // Veritabanı Hataları
    VERITABANI_HATASI("Veritabanı Hatası", "İşlem sırasında bir hata oluştu"),
    OGRENCI_EKLEME_HATASI("Veritabanı Hatası", "Öğrenci eklenirken bir hata oluştu"),
    OGRENCI_GUNCELLEME_HATASI("Veritabanı Hatası", "Öğrenci güncellenirken bir hata oluştu"),
    OGRENCI_SILME_HATASI("Veritabanı Hatası", "Öğrenci silinirken bir hata oluştu"),
    KAYIT_SILME_HATASI("Veritabanı Hatası", "Kayıtlar silinirken bir hata oluştu"),
    YIL_GECIS_HATASI("Veritabanı Hatası", "Yıl geçişi sırasında bir hata oluştu"),
    
    // Onay Mesajları
    TUM_KAYITLARI_SIL_ONAY("Tüm Kayıtları Sil", "Tüm öğrenci ve gözlem kayıtları kalıcı olarak silinecektir.\nBu işlem geri alınamaz. Emin misiniz?"),
    YENI_YIL_ONAY("Yeni Yıl Geçişi", "8. sınıflar silinecek ve diğer sınıflar bir üst sınıfa geçirilecektir.\n5. sınıf kayıtlarını daha sonra manuel olarak girmeniz gerekecektir. Onaylıyor musunuz?"),
    OGRENCI_SIL_ONAY("Öğrenci Sil", "Bu işlem geri alınamaz."),
    
    // Bilgilendirme Mesajları
    ILK_ACILIS_OGRENCI_KAYIT("Hoş Geldiniz", """
            Programı kullanmak için önce dersine girdiğiniz tüm öğrencileri kaydetmelisiniz.
            
            Öğrenci eklemek için:
            - Üstteki menüden 'Liste -> Listeleri Ayarla' yolunu takip edin
            
            Öğrenci listesini kaydettikten sonra:
            - Sınıf adları burada gözükecek
            - Sınıfa tıklayarak gözlem girebileceksiniz"""),
    DATABASE_ILK_ACILIS("Öğrenci Ekleme", """
            Buraya dersine girdiğiniz tüm öğrencileri teker teker ekleyiniz.
            
            Sınıf alanı:
            - Kaçıncı sınıf olduğunu belirtir (örn: 5, 6, 7, 8)
            
            Şube alanı:
            - Şube harfini belirtir (örn: A, B, C, Ç, D)"""),
    
    // Validasyon Hata Mesajları
    GECERSIZ_AD_SOYAD("Geçersiz Ad Soyad", """
            Ad Soyad formatı hatalı!
            
            Doğru format:
            - Ad(lar)ın ilk harfi büyük, diğerleri küçük olmalı
            - Soyadın tüm harfleri büyük olmalı
            - Bir veya iki isim kullanılabilir
            
            Örnekler:
            ✓ Ali KARAN
            ✓ Kerem Ali KARAN
            ✗ ali KARAN
            ✗ Ali Karan"""),
    
    GECERSIZ_SINIF("Geçersiz Sınıf", """
            Sınıf formatı hatalı!
            
            Sınıf 1 ile 12 arasında bir sayı olmalıdır."""),
    
    GECERSIZ_SUBE("Geçersiz Şube", """
            Şube formatı hatalı!
            
            Şube Türkçe alfabeden A-N arası tek bir büyük harf olmalıdır (Ğ hariç).
            Geçerli harfler: A, B, C, Ç, D, E, F, G, H, I, İ, J, K, L, M, N """),
    
    GECERSIZ_OGRENCI_NO("Geçersiz Öğrenci Numarası", """
            Öğrenci numarası formatı hatalı!
            
            Öğrenci numarası sayılardan oluşmalıdır.""");
    
    private final String baslik;
    private final String mesaj;
    
    AlertMessage(String baslik, String mesaj) {
        this.baslik = baslik;
        this.mesaj = mesaj;
    }
    
    public String getBaslik() {
        return baslik;
    }
    
    public String getMesaj() {
        return mesaj;
    }
    
    /**
     * Mesaja ek bilgi eklemek için kullanılır
     * @param ekBilgi Mesaja eklenecek bilgi
     * @return Tam mesaj
     */
    public String getMesajWithDetail(String ekBilgi) {
        return mesaj + ": " + ekBilgi;
    }
}
