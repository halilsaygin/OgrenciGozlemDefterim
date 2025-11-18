package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.daos.OgrenciDAO;
import com.hllsygn.ogrencigozlemdefterim.models.Ogrenci;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class DatabaseController implements Initializable {

    @FXML
    private TextField txtf_ad_soyad;
    @FXML
    private TextField txtf_sinif;
    @FXML
    private TextField txtf_sube;
    @FXML
    private TextField txtf_no;
    @FXML
    private Button btn_ekle;
    @FXML
    private Button btn_guncelle;
    @FXML
    private Button btn_sil;
    @FXML
    private ComboBox<String> combx_sinif;
    @FXML
    private ComboBox<String> combx_sube;
    @FXML
    private TableView<Ogrenci> tableview_liste;
    @FXML
    private TableColumn<Ogrenci, Integer> noCol;
    @FXML
    private TableColumn<Ogrenci, String> adSoyadCol;
    @FXML
    private TableColumn<Ogrenci, Integer> sinifCol;
    @FXML
    private TableColumn<Ogrenci, String> subeCol;

    private final OgrenciDAO ogrenciDAO = new OgrenciDAO();
    private final ObservableList<Ogrenci> ogrenciListesi = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        adSoyadCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAd() + " " + cellData.getValue().getSoyad()));
        sinifCol.setCellValueFactory(new PropertyValueFactory<>("sinif"));
        subeCol.setCellValueFactory(new PropertyValueFactory<>("sube"));

        tableview_liste.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showOgrenciDetails(newSelection);
                    }
                });

        loadFilterComboBoxes();
        loadOgrenciData();
    }

    private void loadOgrenciData() {
        ogrenciListesi.clear();
        String sinifFilter = combx_sinif.getValue();
        String subeFilter = combx_sube.getValue();

        List<Ogrenci> result = ogrenciDAO.findAllWithFilter(sinifFilter, subeFilter);
        ogrenciListesi.addAll(result);
        tableview_liste.setItems(ogrenciListesi);
    }

    private void loadFilterComboBoxes() {
        List<String> siniflar = ogrenciDAO.findAllSiniflar();
        List<String> subeler = ogrenciDAO.findAllSubeler();

        String currentSinif = combx_sinif.getValue();
        String currentSube = combx_sube.getValue();

        combx_sinif.getItems().clear();
        combx_sube.getItems().clear();

        combx_sinif.getItems().add("Tümü");
        combx_sube.getItems().add("Tümü");

        combx_sinif.getItems().addAll(siniflar.stream().distinct().sorted().collect(Collectors.toList()));
        combx_sube.getItems().addAll(subeler.stream().distinct().sorted().collect(Collectors.toList()));

        combx_sinif.setValue(currentSinif == null ? "Tümü" : currentSinif);
        combx_sube.setValue(currentSube == null ? "Tümü" : currentSube);
    }

    private void showOgrenciDetails(Ogrenci ogrenci) {
        txtf_ad_soyad.setText(ogrenci.getAdSoyad());
        txtf_sinif.setText(String.valueOf(ogrenci.getSinif()));
        txtf_sube.setText(ogrenci.getSube());
        txtf_no.setText(String.valueOf(ogrenci.getNo()));
        txtf_no.setEditable(false);
    }

    private void clearForm() {
        txtf_ad_soyad.clear();
        txtf_sinif.clear();
        txtf_sube.clear();
        txtf_no.clear();
        txtf_no.setEditable(true);
        tableview_liste.getSelectionModel().clearSelection();
    }

    @FXML
    public void ogrenciEkle(ActionEvent event) {
        if (txtf_ad_soyad.getText().isEmpty() || txtf_sinif.getText().isEmpty() || txtf_sube.getText().isEmpty() || txtf_no.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Tüm alanlar doldurulmalıdır.");
            return;
        }
        
        String ad;
        String soyad;
        String tamAd = txtf_ad_soyad.getText().trim();
        int lastSpaceIndex = tamAd.lastIndexOf(' ');

        if (lastSpaceIndex == -1) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen hem ad hem de soyad giriniz.");
            return;
        }
        
        ad = tamAd.substring(0, lastSpaceIndex);
        soyad = tamAd.substring(lastSpaceIndex + 1);

        try {
            int no = Integer.parseInt(txtf_no.getText());
            int sinif = Integer.parseInt(txtf_sinif.getText());

            if (ogrenciDAO.findById(no) != null) {
                showAlert(Alert.AlertType.ERROR, "Hata", "Bu numaraya sahip bir öğrenci zaten mevcut.");
                return;
            }
            Ogrenci yeniOgrenci = new Ogrenci(no, ad, soyad, sinif, txtf_sube.getText());
            ogrenciDAO.save(yeniOgrenci);
            
            // Başarılı ekleme sonrası Alert kaldırıldı.
            loadOgrenciData();
            loadFilterComboBoxes();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Öğrenci numarası ve sınıf geçerli bir sayı olmalıdır.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Veritabanı Hatası", "Öğrenci eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    public void ogrenciGuncelle(ActionEvent event) {
        Ogrenci selectedOgrenci = tableview_liste.getSelectionModel().getSelectedItem();
        if (selectedOgrenci == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen güncellenecek bir öğrenci seçin.");
            return;
        }
        if (txtf_ad_soyad.getText().isEmpty() || txtf_sinif.getText().isEmpty() || txtf_sube.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Ad, Sınıf ve Şube alanları boş bırakılamaz.");
            return;
        }
        
        String ad;
        String soyad;
        String tamAd = txtf_ad_soyad.getText().trim();
        int lastSpaceIndex = tamAd.lastIndexOf(' ');

        if (lastSpaceIndex == -1) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen hem ad hem de soyad giriniz.");
            return;
        }
        
        ad = tamAd.substring(0, lastSpaceIndex);
        soyad = tamAd.substring(lastSpaceIndex + 1);

        try {
            int sinif = Integer.parseInt(txtf_sinif.getText());
            Ogrenci guncellenenOgrenci = new Ogrenci(selectedOgrenci.getNo(), ad, soyad, sinif, txtf_sube.getText());
            ogrenciDAO.update(guncellenenOgrenci);

            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Öğrenci bilgileri güncellendi.");
            loadOgrenciData();
            loadFilterComboBoxes();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Sınıf geçerli bir sayı olmalıdır.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Veritabanı Hatası", "Öğrenci güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    public void ogrenciSil(ActionEvent event) {
        Ogrenci selectedOgrenci = tableview_liste.getSelectionModel().getSelectedItem();
        if (selectedOgrenci == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen silinecek bir öğrenci seçin.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Öğrenci Sil");
        confirmationAlert.setHeaderText(selectedOgrenci.getAdSoyad() + " adlı öğrenciyi silmek istediğinizden emin misiniz?");
        confirmationAlert.setContentText("Bu işlem geri alınamaz.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ogrenciDAO.delete(selectedOgrenci.getNo());
                showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Öğrenci başarıyla silindi.");
                loadOgrenciData();
                loadFilterComboBoxes();
                clearForm();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Veritabanı Hatası", "Öğrenci silinirken bir hata oluştu: " + e.getMessage());
            }
        }
    }

    @FXML
    public void anaSahneAc(MouseEvent event) {
        try {
            SceneController.getInstance().anaSahneDon();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Hata", "Ana sahneye dönülürken bir hata oluştu.");
        }
    }

    @FXML
    public void sinifGetir(ActionEvent event) {
        loadOgrenciData();
    }

    @FXML
    public void subeGetir(ActionEvent event) {
        loadOgrenciData();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
