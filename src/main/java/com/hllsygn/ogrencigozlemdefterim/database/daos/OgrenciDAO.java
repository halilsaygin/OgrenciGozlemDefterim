package com.hllsygn.ogrencigozlemdefterim.database.daos;

import com.hllsygn.ogrencigozlemdefterim.database.DBConnect;
import com.hllsygn.ogrencigozlemdefterim.models.Ogrenci;
import com.hllsygn.ogrencigozlemdefterim.utils.ErrorLogger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OgrenciDAO {

    private final Statement statement;

    public OgrenciDAO() {
        try {
            this.statement = DBConnect.getInstance().getStatement();
        } catch (Exception e) {
            throw new RuntimeException("DAO oluşturulurken veritabanı bağlantısı kurulamadı.", e);
        }
    }

    public void deleteAllRecords() throws SQLException {
        // İlişkili verilerden dolayı önce GOZLEMLER tablosunu temizlemek daha güvenlidir.
        statement.executeUpdate("DELETE FROM GOZLEMLER");
        statement.executeUpdate("DELETE FROM OGRENCILER");
    }

    public void performYearTransition() throws SQLException {
        // 8. sınıflara ait gözlemleri sil
        statement.executeUpdate("DELETE FROM GOZLEMLER WHERE OGRENCI_NO IN (SELECT OGRENCI_NO FROM OGRENCILER WHERE OGRENCI_SINIF = 8)");
        // 8. sınıfları sil
        statement.executeUpdate("DELETE FROM OGRENCILER WHERE OGRENCI_SINIF = 8");
        // Kalan öğrencilerin sınıfını 1 artır
        statement.executeUpdate("UPDATE OGRENCILER SET OGRENCI_SINIF = OGRENCI_SINIF + 1");
    }

    public List<Ogrenci> findAllWithFilter(String sinif, String sube) {
        List<Ogrenci> ogrenciler = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM OGRENCILER WHERE 1=1");

        if (sinif != null && !sinif.equals("Tümü")) {
            sql.append(" AND OGRENCI_SINIF = ").append(sinif);
        }
        if (sube != null && !sube.equals("Tümü")) {
            sql.append(" AND OGRENCI_SUBE = '").append(sube).append("'");
        }
        sql.append(" ORDER BY OGRENCI_SINIF ASC, OGRENCI_SUBE ASC, OGRENCI_NO ASC");

        try (ResultSet rs = statement.executeQuery(sql.toString())) {
            while (rs.next()) {
                ogrenciler.add(new Ogrenci(
                        rs.getInt("OGRENCI_NO"),
                        rs.getString("OGRENCI_AD"),
                        rs.getString("OGRENCI_SOYAD"),
                        rs.getInt("OGRENCI_SINIF"),
                        rs.getString("OGRENCI_SUBE")
                ));
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Öğrenciler filtre ile sorgulanırken hata", e);
        }
        return ogrenciler;
    }
    
    public List<Ogrenci> findBySinifSube(String sinifSube) {
        List<Ogrenci> ogrenciler = new ArrayList<>();
        String sinif = sinifSube.replaceAll("[^0-9]", "");
        String sube = sinifSube.replaceAll("[0-9]", "");

        String sql = String.format("SELECT * FROM OGRENCILER WHERE OGRENCI_SINIF = %s AND OGRENCI_SUBE = '%s' ORDER BY OGRENCI_AD ASC, OGRENCI_SOYAD ASC", sinif, sube);

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                ogrenciler.add(new Ogrenci(
                        rs.getInt("OGRENCI_NO"),
                        rs.getString("OGRENCI_AD"),
                        rs.getString("OGRENCI_SOYAD"),
                        rs.getInt("OGRENCI_SINIF"),
                        rs.getString("OGRENCI_SUBE")
                ));
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Öğrenciler sınıf-şube ile sorgulanırken hata - Sınıf: " + sinifSube, e);
        }
        return ogrenciler;
    }

    public Ogrenci findById(int no) {
        String sql = "SELECT * FROM OGRENCILER WHERE OGRENCI_NO = " + no;
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return new Ogrenci(
                        rs.getInt("OGRENCI_NO"),
                        rs.getString("OGRENCI_AD"),
                        rs.getString("OGRENCI_SOYAD"),
                        rs.getInt("OGRENCI_SINIF"),
                        rs.getString("OGRENCI_SUBE")
                );
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Öğrenci ID ile sorgulanırken hata - No: " + no, e);
        }
        return null;
    }

    public void save(Ogrenci ogrenci) throws SQLException {
        String sql = String.format("INSERT INTO OGRENCILER(OGRENCI_NO, OGRENCI_AD, OGRENCI_SOYAD, OGRENCI_SINIF, OGRENCI_SUBE) VALUES (%d, '%s', '%s', %d, '%s')",
                ogrenci.getNo(),
                ogrenci.getAd(),
                ogrenci.getSoyad(),
                ogrenci.getSinif(),
                ogrenci.getSube());
        statement.executeUpdate(sql);
    }

    public void update(Ogrenci ogrenci) throws SQLException {
        String sql = String.format("UPDATE OGRENCILER SET OGRENCI_AD = '%s', OGRENCI_SOYAD = '%s', OGRENCI_SINIF = %d, OGRENCI_SUBE = '%s' WHERE OGRENCI_NO = %d",
                ogrenci.getAd(),
                ogrenci.getSoyad(),
                ogrenci.getSinif(),
                ogrenci.getSube(),
                ogrenci.getNo());
        statement.executeUpdate(sql);
    }

    public void delete(int no) throws SQLException {
        // Önce öğrenciye ait gözlemleri sil
        String deleteGozlemlerSql = "DELETE FROM GOZLEMLER WHERE OGRENCI_NO = " + no;
        statement.executeUpdate(deleteGozlemlerSql);
        
        // Sonra öğrenciyi sil
        String deleteOgrenciSql = "DELETE FROM OGRENCILER WHERE OGRENCI_NO = " + no;
        statement.executeUpdate(deleteOgrenciSql);
    }

    public List<String> findAllSiniflar() {
        List<String> siniflar = new ArrayList<>();
        String sql = "SELECT DISTINCT OGRENCI_SINIF FROM OGRENCILER ORDER BY OGRENCI_SINIF ASC";
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                siniflar.add(String.valueOf(rs.getInt("OGRENCI_SINIF")));
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Sınıflar sorgulanırken hata", e);
        }
        return siniflar;
    }

    public List<String> findAllSubeler() {
        List<String> subeler = new ArrayList<>();
        String sql = "SELECT DISTINCT OGRENCI_SUBE FROM OGRENCILER ORDER BY OGRENCI_SUBE ASC";
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                subeler.add(rs.getString("OGRENCI_SUBE"));
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Şubeler sorgulanırken hata", e);
        }
        return subeler;
    }

    public List<String> findAllSinifSube() {
        List<String> sinifSubeler = new ArrayList<>();
        String sql = "SELECT DISTINCT OGRENCI_SINIF || OGRENCI_SUBE AS SinifSube FROM OGRENCILER ORDER BY OGRENCI_SINIF ASC, OGRENCI_SUBE ASC";
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                sinifSubeler.add(rs.getString("SinifSube"));
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Sınıf-şube listesi sorgulanırken hata", e);
        }
        return sinifSubeler;
    }
    
    /**
     * Veritabanında kayıtlı öğrenci sayısını döndürür
     * @return Toplam öğrenci sayısı
     */
    public int countOgrenciler() {
        String sql = "SELECT COUNT(*) AS TOPLAM FROM OGRENCILER";
        try (ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("TOPLAM");
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Öğrenci sayısı sorgulanırken hata", e);
        }
        return 0;
    }
}
