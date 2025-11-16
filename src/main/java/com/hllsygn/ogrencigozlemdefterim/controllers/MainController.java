package com.hllsygn.ogrencigozlemdefterim.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

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
        // TODO
    }

    @FXML
    private void databaseAc(ActionEvent event) {
        System.out.println("databaseAc tıklandı");
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
        System.out.println("sinifAdi_tiklama tıklandı");
    }
}
