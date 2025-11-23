package com.hllsygn.ogrencigozlemdefterim.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorLogger {
    
    private static final String LOG_FILE = "error_log.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Hatayı log dosyasına yazar
     */
    public static void logError(String message, Throwable throwable) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println("=".repeat(80));
            pw.println("HATA ZAMANI: " + LocalDateTime.now().format(DATE_FORMAT));
            pw.println("MESAJ: " + message);
            
            if (throwable != null) {
                pw.println("HATA TÜRÜ: " + throwable.getClass().getName());
                pw.println("HATA DETAYI: " + throwable.getMessage());
                pw.println("\nSTACK TRACE:");
                
                StringWriter sw = new StringWriter();
                throwable.printStackTrace(new PrintWriter(sw));
                pw.println(sw.toString());
            }
            
            pw.println("=".repeat(80));
            pw.println();
            
        } catch (IOException e) {
            System.err.println("Log dosyasına yazılamadı: " + e.getMessage());
            // printStackTrace kullanmıyoruz çünkü sonsuz döngüye girebilir
        }
    }
    
    /**
     * Sadece mesaj ile log yazar
     */
    public static void logError(String message) {
        logError(message, null);
    }
    
    /**
     * Sistem bilgilerini log dosyasına yazar
     */
    public static void logSystemInfo() {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println("=".repeat(80));
            pw.println("SİSTEM BİLGİLERİ - " + LocalDateTime.now().format(DATE_FORMAT));
            pw.println("Java Versiyonu: " + System.getProperty("java.version"));
            pw.println("JavaFX Versiyonu: " + System.getProperty("javafx.version"));
            pw.println("İşletim Sistemi: " + System.getProperty("os.name"));
            pw.println("OS Versiyonu: " + System.getProperty("os.version"));
            pw.println("Kullanıcı Dizini: " + System.getProperty("user.dir"));
            pw.println("=".repeat(80));
            pw.println();
            
        } catch (IOException e) {
            System.err.println("Sistem bilgileri yazılamadı: " + e.getMessage());
        }
    }
}
