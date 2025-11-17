package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.SampleData;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Menu menu_liste;

    @FXML
    private MenuItem menuItem_listeAyarla;

    @FXML
    private MenuItem menuItem_tumunuSil;

    @FXML
    private MenuItem menuItem_tumArtiEksiSil;

    @FXML
    private MenuItem menuItem_yeniYil;

    @FXML
    private GridPane gridP_siniflar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateClassGrid();
    }

    private void populateClassGrid() {
        // FXML'deki kısıtlamalar zaten ayarlı olduğu için programatik olarak eklemeye gerek yok.
        // Sadece mevcut içeriği temizliyoruz.
        gridP_siniflar.getChildren().clear();

        // Add class labels to grid
        String[] classes = SampleData.getSampleClasses();
        int row = 0;
        int col = 0;

        for (String className : classes) {
            StackPane cellPane = new StackPane();
            cellPane.getStyleClass().add("class-cell"); // Stil için bir sınıf ekleyelim
            cellPane.setUserData(className); // Tıklama olayı için veriyi sakla

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
        System.out.println("tumKayitlariSil tıklandı");
    }

    @FXML
    private void tumArtiEksiKayitlariniSil(ActionEvent event) {
        System.out.println("tumArtiEksiKayitlariniSil tıklandı");
    }

    @FXML
    private void yeniYilAyarla(ActionEvent event) {
        System.out.println("yeniYilAyarla tıklandı");
    }

    @FXML
    private void sinifAdi_tiklama(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        // Tıklanan node veya onun parent'ı bir StackPane ise veriyi al
        StackPane clickedPane = null;
        if (clickedNode instanceof StackPane) {
            clickedPane = (StackPane) clickedNode;
        } else if (clickedNode.getParent() instanceof StackPane) {
            clickedPane = (StackPane) clickedNode.getParent();
        }

        if (clickedPane != null && clickedPane.getUserData() != null) {
            String className = (String) clickedPane.getUserData();
            try {
                SceneController.getInstance().gozlemEkraniAc(className);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
