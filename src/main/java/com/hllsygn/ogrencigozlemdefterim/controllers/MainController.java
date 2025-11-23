package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.daos.OgrenciDAO;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertDialog;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertMessage;
import com.hllsygn.ogrencigozlemdefterim.utils.ErrorLogger;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private GridPane gridP_siniflar;

    private final OgrenciDAO ogrenciDAO = new OgrenciDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateClassGrid();
        
        // İlk açılışta öğrenci kontrolü yap - UI tam yüklendikten sonra
        javafx.application.Platform.runLater(this::checkFirstLaunch);
    }
    
    /**
     * İlk açılışta öğrenci kaydı olup olmadığını kontrol eder
     * Eğer kayıt yoksa kullanıcıyı bilgilendirir
     */
    private void checkFirstLaunch() {
        int ogrenciSayisi = ogrenciDAO.countOgrenciler();
        if (ogrenciSayisi == 0) {
            AlertDialog.bilgi(AlertMessage.ILK_ACILIS_OGRENCI_KAYIT);
        }
    }

    private void populateClassGrid() {
        gridP_siniflar.getChildren().clear();
        List<String> classes = ogrenciDAO.findAllSinifSube();
        
        int row = 0;
        int col = 0;

        for (String className : classes) {
            StackPane cellPane = new StackPane();
            cellPane.getStyleClass().add("class-cell");
            cellPane.setUserData(className);

            Label classLabel = new Label(className);
            classLabel.getStyleClass().add("class-label");
            classLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            classLabel.setAlignment(javafx.geometry.Pos.CENTER);

            cellPane.getChildren().add(classLabel);
            gridP_siniflar.add(cellPane, col, row);

            col++;
            if (col >= gridP_siniflar.getColumnCount()) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void databaseAc(ActionEvent event) {
        try {
            SceneController.getInstance().databaseSahneAc();
        } catch (IOException ex) {
            ErrorLogger.logError("Database ekranı açılırken hata", ex);
        }
    }

    @FXML
    private void tumKayitlariSil(ActionEvent event) {
        if (AlertDialog.onay(AlertMessage.TUM_KAYITLARI_SIL_ONAY)) {
            try {
                ogrenciDAO.deleteAllRecords();
                AlertDialog.bilgi(AlertMessage.TUM_KAYITLAR_SILINDI);
                populateClassGrid();
            } catch (SQLException e) {
                ErrorLogger.logError("Tüm kayıtlar silinirken hata", e);
                AlertDialog.hataDetayli(AlertMessage.KAYIT_SILME_HATASI, e.getMessage());
            }
        }
    }
    
    @FXML
    private void yeniYilAyarla(ActionEvent event) {
        if (AlertDialog.onay(AlertMessage.YENI_YIL_ONAY)) {
            try {
                ogrenciDAO.performYearTransition();
                AlertDialog.bilgi(AlertMessage.YENI_YIL_TAMAMLANDI);
                populateClassGrid();
            } catch (SQLException e) {
                ErrorLogger.logError("Yeni yıl geçişi sırasında hata", e);
                AlertDialog.hataDetayli(AlertMessage.YIL_GECIS_HATASI, e.getMessage());
            }
        }
    }

    @FXML
    private void sinifAdi_tiklama(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();

        while (clickedNode != null && !(clickedNode instanceof StackPane)) {
            clickedNode = clickedNode.getParent();
        }
        
        if (clickedNode instanceof StackPane) {
            StackPane clickedPane = (StackPane) clickedNode;
            if (clickedPane.getUserData() != null) {
                String className = (String) clickedPane.getUserData();
                try {
                    SceneController.getInstance().gozlemEkraniAc(className);
                } catch (IOException e) {
                    ErrorLogger.logError("Gözlem ekranı açılırken hata - Sınıf: " + className, e);
                }
            }
        }
    }
    
}
