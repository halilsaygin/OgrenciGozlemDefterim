package com.hllsygn.ogrencigozlemdefterim.controllers;

import com.hllsygn.ogrencigozlemdefterim.database.daos.OgrenciDAO;
import com.hllsygn.ogrencigozlemdefterim.models.Ogrenci;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertDialog;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertMessage;
import com.hllsygn.ogrencigozlemdefterim.utils.ErrorLogger;
import com.hllsygn.ogrencigozlemdefterim.utils.InputValidator;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private ComboBox<String> txtf_sinif;
    @FXML
    private ComboBox<String> txtf_sube;
    @FXML
    private TextField txtf_no;
    
    // Girilen değerleri hatırlamak için
    private final ObservableList<String> sinifHistory = FXCollections.observableArrayList();
    private final ObservableList<String> subeHistory = FXCollections.observableArrayList();
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
    private Ogrenci selectedOgrenci = null; // Seçili öğrenciyi takip etmek için

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
                        selectedOgrenci = newSelection;
                        showOgrenciDetails(newSelection);
                    }
                });
        
        // Tüm text field'lara listener ekle - tümü boşsa seçimi kaldır
        txtf_ad_soyad.textProperty().addListener((obs, oldVal, newVal) -> checkAndClearSelection());
        txtf_sinif.getEditor().textProperty().addListener((obs, oldVal, newVal) -> checkAndClearSelection());
        txtf_sube.getEditor().textProperty().addListener((obs, oldVal, newVal) -> checkAndClearSelection());
        txtf_no.textProperty().addListener((obs, oldVal, newVal) -> checkAndClearSelection());
        
        // ComboBox'ları ayarla
        setupInputComboBoxes();
        
        // Enter tuşu ile Ekle butonunu tetikle
        setupEnterKeyHandler();

        loadFilterComboBoxes();
        loadOgrenciData();
        
        // İlk açılışta öğrenci kontrolü yap - UI tam yüklendikten sonra
        javafx.application.Platform.runLater(this::checkFirstLaunchDatabase);
    }
    
    /**
     * Enter tuşu handler'larını ayarlar (sadece TextField'lar için)
     */
    private void setupEnterKeyHandler() {
        // Sadece TextField'lara Enter tuşu handler'ı ekle
        txtf_no.setOnAction(e -> btn_ekle.fire());
        txtf_ad_soyad.setOnAction(e -> btn_ekle.fire());
    }
    
    /**
     * Sınıf ve şube ComboBox'larını ayarlar
     * Ok butonuna tıklandığında açılır ve ilk item seçili gelir
     */
    private void setupInputComboBoxes() {
        // Sınıf ComboBox ayarları
        txtf_sinif.setItems(sinifHistory);
        
        // Seçim yapıldığında değeri text field'a yaz
        txtf_sinif.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && txtf_sinif.getEditor() != null) {
                txtf_sinif.getEditor().setText(newValue);
            }
        });
        
        // Dropdown açıldığında ilk item'ı seç
        txtf_sinif.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (isNowShowing && !sinifHistory.isEmpty()) {
                txtf_sinif.getSelectionModel().selectFirst();
            }
        });
        
        // Enter veya seçim yapıldığında değeri onayla
        txtf_sinif.setOnAction(e -> {
            String value = txtf_sinif.getValue();
            if (value != null) {
                txtf_sinif.getEditor().setText(value);
            }
            txtf_sinif.hide();
        });
        
        // Şube ComboBox ayarları
        txtf_sube.setItems(subeHistory);
        
        // Seçim yapıldığında değeri text field'a yaz
        txtf_sube.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && txtf_sube.getEditor() != null) {
                txtf_sube.getEditor().setText(newValue);
            }
        });
        
        // Dropdown açıldığında ilk item'ı seç
        txtf_sube.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (isNowShowing && !subeHistory.isEmpty()) {
                txtf_sube.getSelectionModel().selectFirst();
            }
        });
        
        // Enter veya seçim yapıldığında değeri onayla
        txtf_sube.setOnAction(e -> {
            String value = txtf_sube.getValue();
            if (value != null) {
                txtf_sube.getEditor().setText(value);
            }
            txtf_sube.hide();
        });
    }
    
    /**
     * Girilen değeri history'ye ekler (tekrar yoksa)
     * Son eklenen en üstte olacak şekilde
     */
    private void addToHistory(ObservableList<String> history, String value) {
        if (value != null && !value.trim().isEmpty()) {
            String trimmedValue = value.trim();
            // Eğer zaten varsa kaldır
            history.remove(trimmedValue);
            // En başa ekle (son eklenen en üstte)
            history.add(0, trimmedValue);
        }
    }
    
    /**
     * Database ekranı ilk açıldığında öğrenci kaydı olup olmadığını kontrol eder
     * Eğer kayıt yoksa kullanıcıyı bilgilendirir
     */
    private void checkFirstLaunchDatabase() {
        int ogrenciSayisi = ogrenciDAO.countOgrenciler();
        if (ogrenciSayisi == 0) {
            AlertDialog.bilgi(AlertMessage.DATABASE_ILK_ACILIS);
        }
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
        txtf_sinif.getEditor().setText(String.valueOf(ogrenci.getSinif()));
        txtf_sube.getEditor().setText(ogrenci.getSube());
        txtf_no.setText(String.valueOf(ogrenci.getNo()));
        txtf_no.setEditable(true); // Öğrenci no artık düzenlenebilir
    }
    
    /**
     * Ad Soyad ve No alanları boşsa öğrenci seçimini kaldırır
     * (Sınıf ve şube son değeri koruyacağı için kontrol edilmiyor)
     */
    private void checkAndClearSelection() {
        if (txtf_ad_soyad.getText().trim().isEmpty() && 
            txtf_no.getText().trim().isEmpty()) {
            selectedOgrenci = null;
            tableview_liste.getSelectionModel().clearSelection();
        }
    }

    private void clearForm() {
        txtf_ad_soyad.clear();
        txtf_no.clear();
        // Sınıf ve şube ComboBox'ları temizleme - son değer kalacak
        // txtf_sinif ve txtf_sube temizlenmiyor
        txtf_no.setEditable(true);
        selectedOgrenci = null;
        tableview_liste.getSelectionModel().clearSelection();
    }

    @FXML
    public void ogrenciEkle(ActionEvent event) {
        // Tüm alanlar dolu mu kontrol et
        if (txtf_ad_soyad.getText().trim().isEmpty() || 
            txtf_sinif.getEditor().getText().trim().isEmpty() || 
            txtf_sube.getEditor().getText().trim().isEmpty() || 
            txtf_no.getText().trim().isEmpty()) {
            AlertDialog.hata(AlertMessage.TUM_ALANLAR_DOLU);
            return;
        }
        
        // Öğrenci numarası validasyonu
        if (!InputValidator.validateOgrenciNo(txtf_no.getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_OGRENCI_NO);
            return;
        }
        
        // Ad Soyad validasyonu
        if (!InputValidator.validateAdSoyad(txtf_ad_soyad.getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_AD_SOYAD);
            return;
        }
        
        // Sınıf validasyonu
        if (!InputValidator.validateSinif(txtf_sinif.getEditor().getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_SINIF);
            return;
        }
        
        // Şube validasyonu
        if (!InputValidator.validateSube(txtf_sube.getEditor().getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_SUBE);
            return;
        }
        
        // Validasyonlar geçti, öğrenciyi kaydet
        String tamAd = txtf_ad_soyad.getText().trim();
        int lastSpaceIndex = tamAd.lastIndexOf(' ');
        String ad = tamAd.substring(0, lastSpaceIndex);
        String soyad = tamAd.substring(lastSpaceIndex + 1);
        
        try {
            int no = Integer.parseInt(txtf_no.getText().trim());
            int sinif = Integer.parseInt(txtf_sinif.getEditor().getText().trim());
            String sube = txtf_sube.getEditor().getText().trim();
            
            // History'ye ekle
            addToHistory(sinifHistory, String.valueOf(sinif));
            addToHistory(subeHistory, sube);

            if (ogrenciDAO.findById(no) != null) {
                AlertDialog.hata(AlertMessage.OGRENCI_MEVCUT);
                return;
            }
            
            Ogrenci yeniOgrenci = new Ogrenci(no, ad, soyad, sinif, sube);
            ogrenciDAO.save(yeniOgrenci);
            
            loadOgrenciData();
            loadFilterComboBoxes();
            clearForm();
        } catch (SQLException e) {
            ErrorLogger.logError("Öğrenci eklenirken hata", e);
            AlertDialog.hataDetayli(AlertMessage.OGRENCI_EKLEME_HATASI, e.getMessage());
        }
    }

    @FXML
    public void ogrenciGuncelle(ActionEvent event) {
        if (selectedOgrenci == null) {
            AlertDialog.hata(AlertMessage.OGRENCI_SECILMEDI_GUNCELLEME);
            return;
        }
        
        // Tüm alanlar dolu mu kontrol et
        if (txtf_ad_soyad.getText().trim().isEmpty() || 
            txtf_sinif.getEditor().getText().trim().isEmpty() || 
            txtf_sube.getEditor().getText().trim().isEmpty() || 
            txtf_no.getText().trim().isEmpty()) {
            AlertDialog.hata(AlertMessage.TUM_ALANLAR_DOLU);
            return;
        }
        
        // Öğrenci numarası validasyonu
        if (!InputValidator.validateOgrenciNo(txtf_no.getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_OGRENCI_NO);
            return;
        }
        
        // Öğrenci numarası değiştirilmiş mi kontrol et
        int girilenNo = Integer.parseInt(txtf_no.getText().trim());
        if (girilenNo != selectedOgrenci.getNo()) {
            AlertDialog.uyari(AlertMessage.OGRENCI_NO_DEGISTIRILDI);
            return;
        }
        
        // Ad Soyad validasyonu
        if (!InputValidator.validateAdSoyad(txtf_ad_soyad.getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_AD_SOYAD);
            return;
        }
        
        // Sınıf validasyonu
        if (!InputValidator.validateSinif(txtf_sinif.getEditor().getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_SINIF);
            return;
        }
        
        // Şube validasyonu
        if (!InputValidator.validateSube(txtf_sube.getEditor().getText())) {
            AlertDialog.uyari(AlertMessage.GECERSIZ_SUBE);
            return;
        }
        
        // Validasyonlar geçti, öğrenciyi güncelle
        String tamAd = txtf_ad_soyad.getText().trim();
        int lastSpaceIndex = tamAd.lastIndexOf(' ');
        String ad = tamAd.substring(0, lastSpaceIndex);
        String soyad = tamAd.substring(lastSpaceIndex + 1);

        try {
            int sinif = Integer.parseInt(txtf_sinif.getEditor().getText().trim());
            String sube = txtf_sube.getEditor().getText().trim();
            
            // History'ye ekle
            addToHistory(sinifHistory, String.valueOf(sinif));
            addToHistory(subeHistory, sube);
            
            Ogrenci guncellenenOgrenci = new Ogrenci(selectedOgrenci.getNo(), ad, soyad, sinif, sube);
            ogrenciDAO.update(guncellenenOgrenci);

            AlertDialog.bilgi(AlertMessage.OGRENCI_GUNCELLENDI);
            loadOgrenciData();
            loadFilterComboBoxes();
            clearForm();
        } catch (SQLException e) {
            ErrorLogger.logError("Öğrenci güncellenirken hata", e);
            AlertDialog.hataDetayli(AlertMessage.OGRENCI_GUNCELLEME_HATASI, e.getMessage());
        }
    }

    @FXML
    public void ogrenciSil(ActionEvent event) {
        if (selectedOgrenci == null) {
            AlertDialog.hata(AlertMessage.OGRENCI_SECILMEDI_SILME);
            return;
        }

        if (AlertDialog.onayHeader(
                AlertMessage.OGRENCI_SIL_ONAY.getBaslik(),
                selectedOgrenci.getAdSoyad() + " adlı öğrenciyi silmek istediğinizden emin misiniz?",
                AlertMessage.OGRENCI_SIL_ONAY.getMesaj())) {
            try {
                ogrenciDAO.delete(selectedOgrenci.getNo());
                AlertDialog.bilgi(AlertMessage.OGRENCI_SILINDI);
                loadOgrenciData();
                loadFilterComboBoxes();
                clearForm();
            } catch (SQLException e) {
                ErrorLogger.logError("Öğrenci silinirken hata", e);
                AlertDialog.hataDetayli(AlertMessage.OGRENCI_SILME_HATASI, e.getMessage());
            }
        }
    }

    @FXML
    public void anaSahneAc(MouseEvent event) {
        try {
            SceneController.getInstance().anaSahneDon();
        } catch (IOException e) {
            ErrorLogger.logError("Ana sahneye dönülürken hata", e);
            AlertDialog.hata(AlertMessage.ANA_SAHNE_HATA);
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
}
