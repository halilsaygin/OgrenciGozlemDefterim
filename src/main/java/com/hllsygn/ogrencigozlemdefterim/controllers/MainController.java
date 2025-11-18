package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.daos.OgrenciDAO;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private GridPane gridP_siniflar;

    private final OgrenciDAO ogrenciDAO = new OgrenciDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateClassGrid();
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
            ex.printStackTrace();
        }
    }

    @FXML
    private void tumKayitlariSil(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tüm Kayıtları Sil");
        alert.setHeaderText("Tüm öğrenci ve gözlem kayıtları kalıcı olarak silinecektir.");
        alert.setContentText("Bu işlem geri alınamaz. Emin misiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ogrenciDAO.deleteAllRecords();
                showAlert(Alert.AlertType.INFORMATION, "İşlem Başarılı", "Tüm kayıtlar başarıyla silindi.");
                populateClassGrid(); // Grid'i güncelle
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Veritabanı Hatası", "Kayıtlar silinirken bir hata oluştu: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void yeniYilAyarla(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Yeni Yıl Geçişi");
        alert.setHeaderText("8. sınıflar silinecek ve diğer sınıflar bir üst sınıfa geçirilecektir.");
        alert.setContentText("5. sınıf kayıtlarını daha sonra manuel olarak girmeniz gerekecektir. Onaylıyor musunuz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ogrenciDAO.performYearTransition();
                showAlert(Alert.AlertType.INFORMATION, "İşlem Başarılı", "Yeni yıl geçişi başarıyla tamamlandı.");
                populateClassGrid(); // Grid'i güncelle
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Veritabanı Hatası", "Yıl geçişi sırasında bir hata oluştu: " + e.getMessage());
                e.printStackTrace();
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
                    e.printStackTrace();
                }
            }
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
