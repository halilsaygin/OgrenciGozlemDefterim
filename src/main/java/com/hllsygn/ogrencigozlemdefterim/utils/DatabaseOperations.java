/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hllsygn.ogrencigozlemdefterim.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hllsygn.ogrencigozlemdefterim.models.Ogrenci;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author C-MONSTER
 */
public class DatabaseOperations {

    private static DatabaseOperations instance;
    private static Connection connection = null;
    private static Statement statement;


    public DatabaseOperations() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.sqlite.JDBC");
        File file_db  = new File("OgrenciVeritabani.db");
        if (!file_db.exists()){
            file_db.createNewFile();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:"+file_db);
        statement = connection.createStatement();
        String ogrenciTablosuSorgu = "CREATE TABLE IF NOT EXISTS OGRENCILER ( "
                + "OGRENCI_NO INTEGER PRIMARY KEY, "
                + "OGRENCI_AD_SOYAD NVARCHAR(50) NOT NULL, "
                + "OGRENCI_SINIF INTEGER NOT NULL, "
                + "OGRENCI_SUBE NVARCHAR(1) NOT NULL "
                + ");";
        statement.executeUpdate(ogrenciTablosuSorgu);

        String ogrenciNotTablosuSorgu = "CREATE TABLE IF NOT EXISTS ARTI_EKSI_NOTLAR ( "
                + "NOT_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "OGRENCI_NO INTEGER NOT NULL, "
                + "ARTI_SAYISI INTEGER NOT NULL DEFAULT 0, "
                + "EKSI_SAYISI INTEGER NOT NULL DEFAULT 0, "
                + "FOREIGN KEY(OGRENCI_NO) REFERENCES OGRENCILER(OGRENCI_NO)"
                + ");";

        statement.executeUpdate(ogrenciNotTablosuSorgu);

        System.out.println("Bağlantı ve tablo başarı ile oluşturuldu");
    }

    public static DatabaseOperations getInstance() throws ClassNotFoundException, SQLException, IOException {
        if (instance == null) {
            instance = new DatabaseOperations();
        }
        return instance;
    }

    public ObservableList<Ogrenci> ogrenciListesiGetir() throws SQLException {
        // ObservableList tipinde bir liste oluştur
        ObservableList<Ogrenci> ogrenciListesi = FXCollections.observableArrayList();

        // Veritabanından tüm kayıtları seç
        String query = "SELECT * FROM OGRENCILER ORDER BY OGRENCI_SINIF ASC, OGRENCI_SUBE ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        // ResultSet'teki her satır için bir Ogrenci nesnesi oluştur ve listeye ekle
        while (resultSet.next()) {
            int ogrenci_no = resultSet.getInt("OGRENCI_NO");
            String adSoyad = resultSet.getString("OGRENCI_AD_SOYAD");
            int sinif = resultSet.getInt("OGRENCI_SINIF");
            String sube = resultSet.getString("OGRENCI_SUBE");
            Ogrenci ogrenci = new Ogrenci(ogrenci_no, adSoyad, sinif, sube);
            ogrenciListesi.add(ogrenci);
        }

        // Listeyi döndür
        return ogrenciListesi;
    }

    public ObservableList<Ogrenci> ogrenciListesiGetir(int sinifNo, String subeAd) throws SQLException {
        // ObservableList tipinde bir liste oluştur
        ObservableList<Ogrenci> ogrenciListesi = FXCollections.observableArrayList();

        String query = " SELECT\n" +
                                        "  OGRENCILER.OGRENCI_NO,\n" +
                                        "  OGRENCILER.OGRENCI_AD_SOYAD,\n" +
                                        "  OGRENCILER.OGRENCI_SINIF,\n" +
                                        "  OGRENCILER.OGRENCI_SUBE,\n" +
                                        "  ARTI_EKSI_NOTLAR.NOT_ID,\n" +
                                        "  ARTI_EKSI_NOTLAR.ARTI_SAYISI,\n" +
                                        "  ARTI_EKSI_NOTLAR.EKSI_SAYISI\n" +
                                        "FROM OGRENCILER\n" +
                                        "INNER JOIN ARTI_EKSI_NOTLAR\n" +
                                        "  ON OGRENCILER.OGRENCI_NO = ARTI_EKSI_NOTLAR.OGRENCI_NO\n" +
                                        "WHERE\n" +
                                        "  OGRENCILER.OGRENCI_SINIF = ? AND\n" +
                                        "  OGRENCILER.OGRENCI_SUBE = ?\n" +
                                        "ORDER BY\n" +
                                        "  OGRENCILER.OGRENCI_SINIF ASC,\n" +
                                        "  OGRENCILER.OGRENCI_SUBE ASC;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, sinifNo);
        preparedStatement.setString(2, subeAd);
        ResultSet resultSet = preparedStatement.executeQuery();

        // ResultSet'teki her satır için bir Ogrenci nesnesi oluştur ve listeye ekle
        while (resultSet.next()) {
            int ogrenci_no = resultSet.getInt("OGRENCI_NO");
            String adSoyad = resultSet.getString("OGRENCI_AD_SOYAD");
            int sinif = resultSet.getInt("OGRENCI_SINIF");
            String sube = resultSet.getString("OGRENCI_SUBE");
            int artiSayisi = resultSet.getInt("ARTI_SAYISI");
            int eksiSayisi =  resultSet.getInt("EKSI_SAYISI");

            Ogrenci ogrenci = new Ogrenci(ogrenci_no, adSoyad, sinif, sube,artiSayisi,eksiSayisi);
            ogrenciListesi.add(ogrenci);
        }

        // Listeyi döndür
        return ogrenciListesi;
    }

    public ObservableList<Ogrenci> ogrenciListesiGetir(int sinifNo) throws SQLException {
        String query = "SELECT * FROM OGRENCILER WHERE OGRENCI_SINIF = ? ORDER BY OGRENCI_SINIF ASC, OGRENCI_SUBE ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, sinifNo);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Ogrenci> ogrenciSinifList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int ogrenci_no = resultSet.getInt("OGRENCI_NO");
            String adSoyad = resultSet.getString("OGRENCI_AD_SOYAD");
            int sinif = resultSet.getInt("OGRENCI_SINIF");
            String sube = resultSet.getString("OGRENCI_SUBE");

            Ogrenci ogrenci = new Ogrenci(ogrenci_no, adSoyad, sinif, sube);
            ogrenciSinifList.add(ogrenci);
        }
        return ogrenciSinifList;
    }

    public ObservableList<Ogrenci> ogrenciListesiGetir(String subeAd) throws SQLException {
        String query = "SELECT * FROM OGRENCILER WHERE OGRENCI_SUBE = ? ORDER BY OGRENCI_SINIF ASC, OGRENCI_SUBE ASC";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, subeAd);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Ogrenci> ogrenciSinifList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            int ogrenci_no = resultSet.getInt("OGRENCI_NO");
            String adSoyad = resultSet.getString("OGRENCI_AD_SOYAD");
            int sinif = resultSet.getInt("OGRENCI_SINIF");
            String sube = resultSet.getString("OGRENCI_SUBE");

            Ogrenci ogrenci = new Ogrenci(ogrenci_no, adSoyad, sinif, sube);
            ogrenciSinifList.add(ogrenci);
        }
        return ogrenciSinifList;
    }
    
    public ObservableList<String> subeListesiGetir(int sinif) throws SQLException{
        ObservableList<String> subeList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT OGRENCI_SUBE FROM OGRENCILER WHERE OGRENCI_SINIF = ? ORDER BY OGRENCI_SUBE";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
         preparedStatement.setInt(1,sinif);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
            String sube = resultSet.getString("OGRENCI_SUBE");
            subeList.add(sube);
        } 
        return subeList;
    }

    // Öğrenci ekleme
    public void ogrenciEkle(Ogrenci ogrenci) throws SQLException {
        String sql_ogrenci = "INSERT INTO OGRENCILER (OGRENCI_NO, OGRENCI_AD_SOYAD, OGRENCI_SINIF, OGRENCI_SUBE) VALUES (?, ?, ?, ?); ";

        PreparedStatement preparedStatement = connection.prepareStatement(sql_ogrenci);
        preparedStatement.setInt(1, ogrenci.getNo());
        preparedStatement.setString(2, ogrenci.getAdSoyad());
        preparedStatement.setInt(3, ogrenci.getSinif());
        preparedStatement.setString(4, ogrenci.getSube());

        preparedStatement.executeUpdate();

        String sql_notlar = "INSERT INTO ARTI_EKSI_NOTLAR (OGRENCI_NO, ARTI_SAYISI, EKSI_SAYISI) VALUES (?, ?, ?)";
        preparedStatement = connection.prepareStatement(sql_notlar);
        preparedStatement.setInt(1, ogrenci.getNo());
        preparedStatement.setInt(2, 0);
        preparedStatement.setInt(3, 0);

        preparedStatement.executeUpdate(); // Execute prepared statement

        System.out.println("Öğrenci başarıyla eklendi.");
    }

    // Öğrenci güncelleme
    public void ogrenciGuncelle(Ogrenci ogrenci) throws SQLException {
        String sql = "UPDATE OGRENCILER SET OGRENCI_AD_SOYAD = ? , OGRENCI_SINIF = ? ,OGRENCI_SUBE = ? WHERE OGRENCI_NO = ?  ";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, ogrenci.getAdSoyad());
        preparedStatement.setInt(2, ogrenci.getSinif());
        preparedStatement.setString(3, ogrenci.getSube());
        preparedStatement.setInt(4, ogrenci.getNo());

        preparedStatement.executeUpdate(); // Execute prepared statement

        System.out.println("Öğrenci başarıyla güncellendi.");
    }

    // Öğrenci silme
    public void ogrenciSil(Ogrenci ogrenci) throws SQLException {
        // OGRENCILER tablosu ilgili kaydı sil 
        String sql_delete_ogrenci = "DELETE FROM OGRENCILER WHERE OGRENCI_NO = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql_delete_ogrenci);
        preparedStatement.setInt(1, ogrenci.getNo());
        preparedStatement.executeUpdate();

        // ARTI_EKSI_NOTLAR tablosu ilgli kaydı sil
        String sql_delete_notlar = "DELETE FROM ARTI_EKSI_NOTLAR WHERE OGRENCI_NO = ?";
        preparedStatement = connection.prepareStatement(sql_delete_notlar);
        preparedStatement.setInt(1, ogrenci.getNo());
        preparedStatement.executeUpdate();
    }

    public Ogrenci ogrenciGetir(int ogrenciNo) throws SQLException {
        String query = "SELECT * FROM OGRENCILER WHERE OGRENCI_NO = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ogrenciNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        Ogrenci ogrenci = null;
        if (resultSet.next()) {
            int ogrenci_no = resultSet.getInt("OGRENCI_NO");
            String adSoyad = resultSet.getString("OGRENCI_AD_SOYAD");
            int sinif = resultSet.getInt("OGRENCI_SINIF");
            String sube = resultSet.getString("OGRENCI_SUBE");
            ogrenci = new Ogrenci(ogrenci_no, adSoyad, sinif, sube);
        }
        return ogrenci;
    }

    public void tumOgrenciKayitlariniSil() throws SQLException {
        String sql = "DELETE FROM ARTI_EKSI_NOTLAR; DELETE FROM OGRENCILER";
        statement.executeUpdate(sql);
        System.out.println("Tüm öğrenci kayıtları silindi!");
    }

    public void tumArtiEksiNotlariniSil() throws SQLException {
        String sql = "DELETE FROM ARTI_EKSI_NOTLAR";
        statement.executeUpdate(sql);
        System.out.println("Tüm artı-eksi notlar silindi!");
    }

    public void ogrenciArtiEksiSayisiGuncelle(Ogrenci ogrenci) throws SQLException {
        String query = "UPDATE ARTI_EKSI_NOTLAR SET ARTI_SAYISI = ?, EKSI_SAYISI = ? WHERE OGRENCI_NO = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ogrenci.getArtiSayisi());
        preparedStatement.setInt(2, ogrenci.getEksiSayisi());
        preparedStatement.setInt(3, ogrenci.getNo());
        preparedStatement.executeUpdate();
    }

    public int ogrenciEksiSayisiGetir(Ogrenci ogrenci) throws SQLException {
        String query = "SELECT EKSI_SAYISI FROM ARTI_EKSI_NOTLAR WHERE OGRENCI_NO = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ogrenci.getNo());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("EKSI_SAYISI");
        } else {
            return 0;
        }
    }

    public int ogrenciArtiSayisiGetir(Ogrenci ogrenci) throws SQLException {
        String query = "SELECT ARTI_SAYISI FROM ARTI_EKSI_NOTLAR WHERE OGRENCI_NO = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ogrenci.getNo());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("ARTI_SAYISI");
        } else {
            return 0;
        }
    }

    public int ogrenciNoBul(String adSoyad) throws SQLException {
        String query = "SELECT OGRENCI_NO FROM OGRENCILER WHERE OGRENCI_AD_SOYAD = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, adSoyad);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("OGRENCI_NO");
        } else {
            return -1;
        }
    }

    public Ogrenci ogrenciAdSoyadileBul(String adSoyad) throws SQLException{
         String query = "SELECT * FROM OGRENCILER WHERE OGRENCI_AD_SOYAD = ?";
         PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, adSoyad);
        ResultSet resultSet = preparedStatement.executeQuery();
        
         if (resultSet.next()) {
            Ogrenci ogrenci = new Ogrenci(resultSet.getInt("OGRENCI_NO"),
                                                                  resultSet.getString("OGRENCI_AD_SOYAD"),
                                                                  resultSet.getInt("OGRENCI_SINIF"),
                                                                  resultSet.getString("OGRENCI_SUBE"));
            return ogrenci;
        } else {
            return null;
        }
    }

    public String[] ogrenciSinifSubeListesiGetir() throws SQLException {
        String query = "SELECT DISTINCT OGRENCI_SINIF, OGRENCI_SUBE FROM OGRENCILER ORDER BY OGRENCI_SINIF, OGRENCI_SUBE";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Sonucu String dizisine aktar
        ArrayList<String> sinifSubeListesi = new ArrayList<>();
        while (resultSet.next()) {
            int sinif = resultSet.getInt("OGRENCI_SINIF");
            String sube = resultSet.getString("OGRENCI_SUBE");
            sinifSubeListesi.add(sinif + sube);
        }
      
        // ArrayList'i String dizisine dönüştür ve döndür
        String[] sinifSubeArray = new String[sinifSubeListesi.size()];
        for (int i = 0; i < sinifSubeArray.length; i++) {
            sinifSubeArray[i] = sinifSubeListesi.get(i);
        }
        return sinifSubeArray;
    }

    public String[] ogrenciAdSoyadListesiGetir(int sinif, String sube) throws SQLException {
        String query = "SELECT DISTINCT OGRENCI_AD_SOYAD FROM OGRENCILER WHERE OGRENCI_SINIF = ? AND OGRENCI_SUBE = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, sinif);
        preparedStatement.setString(2, sube);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Sonucu String dizisine aktar
        ArrayList<String> adSoyadListesi = new ArrayList<>();
        while (resultSet.next()) {
            String adSoyad = resultSet.getString("OGRENCI_AD_SOYAD");
            adSoyadListesi.add(adSoyad);
        }

        // ArrayList'i String dizisine dönüştür ve döndür
        String[] adSoyadArray = new String[adSoyadListesi.size()];
        for (int i = 0; i < adSoyadArray.length; i++) {
            adSoyadArray[i] = adSoyadListesi.get(i);
        }
        return adSoyadArray;
    }

    public int ogrenciSayisiGetir() throws SQLException {
        String query = "SELECT COUNT(*) FROM OGRENCILER;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            return 0;
        }
    }

    public void yeniYilaGecir() throws SQLException {
        // metod OGRENCILER tablosunda 8. sınıfları siler sonra 5,6 ve 7. sınıfları bir üst sınıfa geçirir.
        // 8. sınıfları siler
        String silSorgusu = "DELETE FROM OGRENCILER WHERE OGRENCI_SINIF = 8;";
        statement.executeUpdate(silSorgusu);

        // 5., 6. ve 7. sınıfları bir üst sınıfa geçirir
        String guncelleSorgusu = "UPDATE OGRENCILER SET OGRENCI_SINIF = CASE "
                + "WHEN OGRENCI_SINIF = 5 THEN 6 "
                + "WHEN OGRENCI_SINIF = 6 THEN 7 "
                + "WHEN OGRENCI_SINIF = 7 THEN 8 "
                + "ELSE OGRENCI_SINIF END "
                + "WHERE OGRENCI_SINIF IN (5, 6, 7);";
        statement.executeUpdate(guncelleSorgusu);
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
