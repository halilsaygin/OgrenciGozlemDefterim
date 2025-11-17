package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.SampleData;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GozlemEkraniController implements Initializable {

    @FXML
    private Label label_sinif;

    @FXML
    private ComboBox<String> combobox_ogrenciler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initData(String className) {
        label_sinif.setText(className);
        populateStudentComboBox(className);
    }

    private void populateStudentComboBox(String className) {
        ObservableList<String> studentList = FXCollections.observableArrayList();
        String[] classAndBranch = className.split("(?<=\\d)(?=\\D)");
        int sinif = Integer.parseInt(classAndBranch[0]);
        String sube = classAndBranch[1];

        for (SampleData student : SampleData.values()) {
            if (student.getSinif() == sinif && student.getSube().equals(sube)) {
                studentList.add(student.getAdSoyad());
            }
        }
        combobox_ogrenciler.setItems(studentList);
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
     * Handles the save button action
     */
    public void kaydet(ActionEvent event) {
        // TODO: Implement save logic
    }
}
