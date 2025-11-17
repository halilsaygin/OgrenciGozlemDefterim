package com.hllsygn.ogrencigozlemdefterim.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {

    private static DBConnect instance;
    private static Connection connection = null;
    private static Statement statement;


    public DBConnect() throws ClassNotFoundException, SQLException, IOException {
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
                + "OGRENCI_SINIF NVARCHAR(1) NOT NULL, "
                + "OGRENCI_SUBE NVARCHAR(1) NOT NULL "
                + ");";
        statement.executeUpdate(ogrenciTablosuSorgu);

        String gozlemTablosuSorgu = "CREATE TABLE IF NOT EXISTS GOZLEMLER ( "
                + "GOZLEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "OGRENCI_NO INTEGER NOT NULL, "
                + "GOZLEM_METNI TEXT NOT NULL, "
                + "FOREIGN KEY(OGRENCI_NO) REFERENCES OGRENCILER(OGRENCI_NO)"
                + ");";

        statement.executeUpdate(gozlemTablosuSorgu);

        System.out.println("Bağlantı başarıyla kuruldu.");
    }

    public static DBConnect getInstance() throws ClassNotFoundException, SQLException, IOException {
        if (instance == null) {
            instance = new DBConnect();
        }
        return instance;
    }

    
}
