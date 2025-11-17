package com.hllsygn.ogrencigozlemdefterim.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import java.io.IOException;

public class DatabaseController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Handles the "Ekle" (Add) button action
     */
    public void ogrenciEkle(ActionEvent event) {
        // TODO: Implement student addition logic
    }
    
    /**
     * Handles the "Güncelle" (Update) button action
     */
    public void ogrenciGuncelle(ActionEvent event) {
        // TODO: Implement student update logic
    }
    
    /**
     * Handles the "Sil" (Delete) button action
     */
    public void ogrenciSil(ActionEvent event) {
        // TODO: Implement student deletion logic
    }
    
    /**
     * Handles the home image click to return to main screen
     */
    public void anaSahneAc(MouseEvent event) {
        try {
            SceneController.getInstance().anaSahneDon();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Handles the class selection combobox action
     */
    public void sinifGetir(ActionEvent event) {
        // TODO: Implement class filtering logic
    }
    
    /**
     * Handles the branch/section selection combobox action
     */
    public void subeGetir(ActionEvent event) {
        // TODO: Implement branch/section filtering logic
    }
}