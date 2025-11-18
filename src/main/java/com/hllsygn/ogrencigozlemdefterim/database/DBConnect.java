package com.hllsygn.ogrencigozlemdefterim.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {

    private static DBConnect instance;
    private Connection connection = null;
    private Statement statement = null;

    private DBConnect() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.sqlite.JDBC");
        File file_db = new File("OgrenciVeritabani.db");
        if (!file_db.exists()) {
            file_db.createNewFile();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + file_db.getAbsolutePath());
        statement = connection.createStatement();

        String ogrenciTablosuSorgu = "CREATE TABLE IF NOT EXISTS OGRENCILER ( "
                + "OGRENCI_NO INTEGER PRIMARY KEY, "
                + "OGRENCI_AD NVARCHAR(50) NOT NULL, "
                + "OGRENCI_SOYAD NVARCHAR(50) NOT NULL, "
                + "OGRENCI_SINIF INTEGER NOT NULL, "
                + "OGRENCI_SUBE NVARCHAR(10) NOT NULL "
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
    
    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
            System.out.println("Veritabanı bağlantısı kapatıldı.");
        }
    }
}
