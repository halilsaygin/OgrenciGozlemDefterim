package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.daos.GozlemDAO;
import com.hllsygn.ogrencigozlemdefterim.database.daos.OgrenciDAO;
import com.hllsygn.ogrencigozlemdefterim.models.Gozlem;
import com.hllsygn.ogrencigozlemdefterim.models.Ogrenci;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertDialog;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertMessage;
import com.hllsygn.ogrencigozlemdefterim.utils.ErrorLogger;
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
            AlertDialog.hata(AlertMessage.OGRENCI_SECILMEDI);
            return;
        }

        if (gozlemMetni == null || gozlemMetni.trim().isEmpty()) {
            AlertDialog.hata(AlertMessage.GOZLEM_BOS);
            return;
        }

        try {
            if (mevcutGozlem != null) {
                mevcutGozlem.setGozlemMetni(gozlemMetni);
                gozlemDAO.update(mevcutGozlem);
                AlertDialog.bilgi(AlertMessage.GOZLEM_GUNCELLENDI);
            } else {
                Gozlem yeniGozlem = new Gozlem();
                yeniGozlem.setOgrenciNo(seciliOgrenci.getNo());
                yeniGozlem.setGozlemMetni(gozlemMetni);
                gozlemDAO.save(yeniGozlem);
                mevcutGozlem = gozlemDAO.findByOgrenciNo(seciliOgrenci.getNo());
                AlertDialog.bilgi(AlertMessage.GOZLEM_KAYDEDILDI);
            }
        } catch (SQLException e) {
            ErrorLogger.logError("Gözlem kaydedilirken/güncellenirken hata", e);
            AlertDialog.hataDetayli(AlertMessage.VERITABANI_HATASI, e.getMessage());
        }
    }

    @FXML
    private void anaSahneAc(MouseEvent event) {
        try {
            SceneController.getInstance().anaSahneDon();
        } catch (IOException e) {
            ErrorLogger.logError("Gözlem ekranından ana sahneye dönülürken hata", e);
            AlertDialog.hata(AlertMessage.ANA_SAHNE_HATA);
        }
    }
}
