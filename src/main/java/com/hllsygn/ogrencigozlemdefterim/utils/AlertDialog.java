package com.hllsygn.ogrencigozlemdefterim.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Uygulama genelinde kullanılan alert diyaloglarını yöneten merkezi sınıf
 */
public class AlertDialog {
    
    /**
     * Bilgi mesajı gösterir
     * @param alertMessage Gösterilecek mesaj enum değeri
     */
    public static void bilgi(AlertMessage alertMessage) {
        goster(alertMessage.getBaslik(), alertMessage.getMesaj(), Alert.AlertType.INFORMATION);
    }
    
    /**
     * Bilgi mesajı gösterir (özel mesaj ile)
     * @param baslik Alert başlığı
     * @param mesaj Alert mesajı
     */
    public static void bilgi(String baslik, String mesaj) {
        goster(baslik, mesaj, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Uyarı mesajı gösterir
     * @param alertMessage Gösterilecek mesaj enum değeri
     */
    public static void uyari(AlertMessage alertMessage) {
        goster(alertMessage.getBaslik(), alertMessage.getMesaj(), Alert.AlertType.WARNING);
    }
    
    /**
     * Uyarı mesajı gösterir (özel mesaj ile)
     * @param baslik Alert başlığı
     * @param mesaj Alert mesajı
     */
    public static void uyari(String baslik, String mesaj) {
        goster(baslik, mesaj, Alert.AlertType.WARNING);
    }
    
    /**
     * Hata mesajı gösterir
     * @param alertMessage Gösterilecek mesaj enum değeri
     */
    public static void hata(AlertMessage alertMessage) {
        goster(alertMessage.getBaslik(), alertMessage.getMesaj(), Alert.AlertType.ERROR);
    }
    
    /**
     * Hata mesajı gösterir (özel mesaj ile)
     * @param baslik Alert başlığı
     * @param mesaj Alert mesajı
     */
    public static void hata(String baslik, String mesaj) {
        goster(baslik, mesaj, Alert.AlertType.ERROR);
    }
    
    /**
     * Hata mesajı gösterir (ek detay ile)
     * @param alertMessage Gösterilecek mesaj enum değeri
     * @param detay Mesaja eklenecek detay bilgisi
     */
    public static void hataDetayli(AlertMessage alertMessage, String detay) {
        goster(alertMessage.getBaslik(), alertMessage.getMesajWithDetail(detay), Alert.AlertType.ERROR);
    }
    
    /**
     * Onay diyalogu gösterir
     * @param alertMessage Gösterilecek mesaj enum değeri
     * @return Kullanıcı OK'e tıkladıysa true, aksi halde false
     */
    public static boolean onay(AlertMessage alertMessage) {
        return onay(alertMessage.getBaslik(), alertMessage.getMesaj());
    }
    
    /**
     * Onay diyalogu gösterir (özel mesaj ile)
     * @param baslik Alert başlığı
     * @param mesaj Alert mesajı
     * @return Kullanıcı OK'e tıkladıysa true, aksi halde false
     */
    public static boolean onay(String baslik, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(null);
        alert.setContentText(mesaj);
        setAlertIcon(alert);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Onay diyalogu gösterir (özel header ile)
     * @param baslik Alert başlığı
     * @param header Alert header metni
     * @param mesaj Alert mesajı
     * @return Kullanıcı OK'e tıkladıysa true, aksi halde false
     */
    public static boolean onayHeader(String baslik, String header, String mesaj) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(baslik);
        alert.setHeaderText(header);
        alert.setContentText(mesaj);
        setAlertIcon(alert);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Temel alert gösterme metodu
     * @param baslik Alert başlığı
     * @param mesaj Alert mesajı
     * @param tip Alert tipi
     */
    private static void goster(String baslik, String mesaj, Alert.AlertType tip) {
        try {
            Alert alert = new Alert(tip);
            alert.setTitle(baslik);
            alert.setGraphic(null);
            alert.setHeaderText(null);
            alert.setContentText(mesaj);
            setAlertIcon(alert);
            alert.showAndWait();
        } catch (Exception e) {
            // Alert gösterilemezse konsola yaz ve logla
            System.err.println(baslik + ": " + mesaj);
            ErrorLogger.logError("Alert gösterilirken hata - Başlık: " + baslik, e);
        }
    }
    
    /**
     * Alert penceresine uygulama ikonunu ekler
     * @param alert İkon eklenecek alert
     */
    private static void setAlertIcon(Alert alert) {
        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image icon = new Image(AlertDialog.class.getResourceAsStream("/com/hllsygn/ogrencigozlemdefterim/app_icon.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            // İkon yüklenemezse sessizce devam et
            ErrorLogger.logError("Alert ikonu yüklenirken hata", e);
        }
    }
}
