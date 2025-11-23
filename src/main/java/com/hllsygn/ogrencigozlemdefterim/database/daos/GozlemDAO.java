package com.hllsygn.ogrencigozlemdefterim.database.daos;

import com.hllsygn.ogrencigozlemdefterim.database.DBConnect;
import com.hllsygn.ogrencigozlemdefterim.models.Gozlem;
import com.hllsygn.ogrencigozlemdefterim.utils.ErrorLogger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GozlemDAO {

    private final Statement statement;

    public GozlemDAO() {
        try {
            this.statement = DBConnect.getInstance().getStatement();
        } catch (Exception e) {
            throw new RuntimeException("DAO oluşturulurken veritabanı bağlantısı kurulamadı.", e);
        }
    }

    public Gozlem findByOgrenciNo(int ogrenciNo) {
        String sql = "SELECT * FROM GOZLEMLER WHERE OGRENCI_NO = " + ogrenciNo;
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return new Gozlem(
                        rs.getInt("GOZLEM_ID"),
                        rs.getInt("OGRENCI_NO"),
                        rs.getString("GOZLEM_METNI")
                );
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Gözlem öğrenci no ile sorgulanırken hata - No: " + ogrenciNo, e);
        }
        return null;
    }

    public void save(Gozlem gozlem) throws SQLException {
        // SQL Injection'a karşı savunmasız. Sadece proje bağlamı için.
        String gozlemMetni = gozlem.getGozlemMetni().replace("'", "''");
        String sql = String.format("INSERT INTO GOZLEMLER (OGRENCI_NO, GOZLEM_METNI) VALUES (%d, '%s')",
                gozlem.getOgrenciNo(),
                gozlemMetni);
        statement.executeUpdate(sql);
    }

    public void update(Gozlem gozlem) throws SQLException {
        // SQL Injection'a karşı savunmasız. Sadece proje bağlamı için.
        String gozlemMetni = gozlem.getGozlemMetni().replace("'", "''");
        String sql = String.format("UPDATE GOZLEMLER SET GOZLEM_METNI = '%s' WHERE GOZLEM_ID = %d",
                gozlemMetni,
                gozlem.getGozlemId());
        statement.executeUpdate(sql);
    }
}
