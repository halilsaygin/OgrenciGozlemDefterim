package com.hllsygn.ogrencigozlemdefterim;

import com.hllsygn.ogrencigozlemdefterim.database.DBConnect;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertDialog;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertMessage;
import com.hllsygn.ogrencigozlemdefterim.utils.ErrorLogger;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage _primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Sistem bilgilerini logla
            ErrorLogger.logSystemInfo();
            
            // Veritabanı bağlantısını başlat
            DBConnect.getInstance();

            _primaryStage = primaryStage;

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/fxmlfiles/Main.fxml")));
            Scene scene = new Scene(root);

            // ayarla uygualama pencere ikonu
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/hllsygn/ogrencigozlemdefterim/app_icon.png")));
                _primaryStage.getIcons().add(icon);
            } catch (Exception e) {
                ErrorLogger.logError("Icon yüklenemedi", e);
                System.err.println("Icon yüklenemedi: " + e.getMessage());
            }

            _primaryStage.setResizable(false);
            _primaryStage.setTitle("Öğrenci Gözlem Defterim");
            _primaryStage.setScene(scene);
            _primaryStage.show();
            
        } catch (Exception e) {
            ErrorLogger.logError("Uygulama başlatılırken kritik hata oluştu", e);
            AlertDialog.hataDetayli(AlertMessage.UYGULAMA_BASLAMA_HATASI, "\n\nHata: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void stop() throws Exception {
        // Uygulama kapanırken veritabanı bağlantısını kapat
        try {
            DBConnect.getInstance().closeConnection();
        } catch (Exception e) {
            ErrorLogger.logError("Veritabanı bağlantısı kapatılırken hata", e);
            System.err.println("Veritabanı bağlantısı kapatılırken hata: " + e.getMessage());
        }
        super.stop();
    }
    


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Global exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ErrorLogger.logError("Yakalanmamış hata - Thread: " + thread.getName(), throwable);
            System.err.println("Kritik hata oluştu. Detaylar error_log.txt dosyasına kaydedildi.");
        });
        
        try {
            // JavaFX uygulamasını başlat
            launch(args);
        } catch (Exception e) {
            ErrorLogger.logError("Main metodunda kritik hata", e);
            System.err.println("Uygulama başlatılamadı. Detaylar error_log.txt dosyasına kaydedildi.");
            System.exit(1);
        }
    }

}