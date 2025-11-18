package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.daos.GozlemDAO;
import com.hllsygn.ogrencigozlemdefterim.database.daos.OgrenciDAO;
import com.hllsygn.ogrencigozlemdefterim.models.Gozlem;
import com.hllsygn.ogrencigozlemdefterim.models.Ogrenci;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class GozlemEkraniController implements Initializable {

    @FXML
    private Label lbl_sinif_adi;
    @FXML
    private ComboBox<Ogrenci> combx_ogrenciler;
    @FXML
    private TextArea txt_gozlem;
    @FXML
    private Button btn_kaydet;

    private final OgrenciDAO ogrenciDAO = new OgrenciDAO();
    private final GozlemDAO gozlemDAO = new GozlemDAO();
    
    private Gozlem mevcutGozlem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ComboBox'ta öğrencinin adının ve soyadının nasıl gösterileceğini ayarla
        combx_ogrenciler.setConverter(new StringConverter<Ogrenci>() {
            @Override
            public String toString(Ogrenci ogrenci) {
                return ogrenci == null ? null : ogrenci.getAdSoyad();
            }

            @Override
            public Ogrenci fromString(String string) {
                return null; // Bu senaryoda gerekli değil
            }
        });

        // Bir öğrenci seçildiğinde gözlem metnini yükle
        combx_ogrenciler.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadGozlemForOgrenci(newSelection);
            } else {
                txt_gozlem.clear();
                mevcutGozlem = null;
            }
        });
    }

    public void setSinifAdi(String sinifAdi) {
        lbl_sinif_adi.setText(sinifAdi);
        loadOgrenciler(sinifAdi);
    }

    private void loadOgrenciler(String sinifAdi) {
        List<Ogrenci> ogrenciler = ogrenciDAO.findBySinifSube(sinifAdi);
        ObservableList<Ogrenci> ogrenciList = FXCollections.observableArrayList(ogrenciler);
        combx_ogrenciler.setItems(ogrenciList);
    }

    private void loadGozlemForOgrenci(Ogrenci ogrenci) {
        mevcutGozlem = gozlemDAO.findByOgrenciNo(ogrenci.getNo());
        if (mevcutGozlem != null) {
            txt_gozlem.setText(mevcutGozlem.getGozlemMetni());
        } else {
            txt_gozlem.clear();
        }
    }

    @FXML
    private void kaydet(ActionEvent event) {
        Ogrenci seciliOgrenci = combx_ogrenciler.getSelectionModel().getSelectedItem();
        String gozlemMetni = txt_gozlem.getText();

        if (seciliOgrenci == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir öğrenci seçin.");
            return;
        }

        if (gozlemMetni == null || gozlemMetni.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Gözlem metni boş olamaz.");
            return;
        }

        try {
            if (mevcutGozlem != null) {
                // Güncelleme
                mevcutGozlem.setGozlemMetni(gozlemMetni);
                gozlemDAO.update(mevcutGozlem);
                showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Gözlem başarıyla güncellendi.");
            } else {
                // Yeni Kayıt
                Gozlem yeniGozlem = new Gozlem();
                yeniGozlem.setOgrenciNo(seciliOgrenci.getNo());
                yeniGozlem.setGozlemMetni(gozlemMetni);
                gozlemDAO.save(yeniGozlem);
                // Yeni eklenen kaydı bir sonraki işlem için mevcut olarak ayarla
                mevcutGozlem = gozlemDAO.findByOgrenciNo(seciliOgrenci.getNo());
                showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Gözlem başarıyla kaydedildi.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Veritabanı Hatası", "İşlem sırasında bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void anaSahneAc(MouseEvent event) {
        try {
            SceneController.getInstance().anaSahneDon();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Ana sahneye dönülürken bir hata oluştu.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
