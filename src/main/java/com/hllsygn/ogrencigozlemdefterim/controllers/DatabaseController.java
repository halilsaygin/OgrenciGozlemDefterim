/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.hllsygn.ogrencigozlemdefterim.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import com.hllsygn.ogrencigozlemdefterim.models.Ogrenci;
import com.hllsygn.ogrencigozlemdefterim.utils.AlertDialog;
import com.hllsygn.ogrencigozlemdefterim.utils.DatabaseOperations;
import com.hllsygn.ogrencigozlemdefterim.utils.PushAnimation;
import com.hllsygn.ogrencigozlemdefterim.utils.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author C-MONSTER
 */
public class DatabaseController implements Initializable {

    DatabaseOperations database;
    ObservableList<Ogrenci> ogrenciList;
    ObservableList<Integer> ogrenciSinifList = FXCollections.observableArrayList();
    ObservableList<String> ogrenciSubeList = FXCollections.observableArrayList();

    @FXML
    public TableView tableview_liste;
    public TableColumn<Ogrenci, Integer> noCol;
    public TableColumn<Ogrenci, String> adSoyadCol;
    public TableColumn<Ogrenci, Integer> sinifCol;
    public TableColumn<Ogrenci, Integer> subeCol;
    public TextField txtf_ad_soyad, txtf_sinif, txtf_sube, txtf_no;
    public ComboBox combx_sinif, combx_sube;
    public Button btn_ekle, btn_guncelle, btn_sil;

    @FXML
    public void anaSahneAc() {
        try {
            if (database.ogrenciSayisiGetir() == 0) {
                AlertDialog.gosterAlert("BİLGİ", "Veritabanında hiç öğrenci yok. Öğrenci eklenmeden uygulama işlevsel değildir.\n"
                        + "Tekrar bu ekrana dönmek için Menü çubuğunda Liste>Listeleri Ayarla yolunu izleyin.", Alert.AlertType.INFORMATION);
            }

            SceneController controller = SceneController.getInstance();
            controller.anaSahne_Ac();

        } catch (IOException ex) {
            AlertDialog.gosterAlert("Hata", "Sahne geçişi hatası alındı" + ex.getMessage(), Alert.AlertType.ERROR);
        } catch (SQLException ex) {
            AlertDialog.gosterAlert("Hata", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void ogrenciEkle() {
        PushAnimation.nodeAnimation(btn_ekle);
        try {
            girisAlanlariBicimKontrol();

            // Veritabanına öğrenci ekler
            // Artı Eksi Tablosuna 0-0 başlangıç degerli kayıt girer.
            int ogrenci_no = Integer.parseInt(txtf_no.getText());
            String ogrenci_adSoyad = txtf_ad_soyad.getText();
            int ogrenci_sinif = Integer.parseInt(txtf_sinif.getText());
            String ogrenci_sube = txtf_sube.getText();

            Ogrenci ogrenci = new Ogrenci(ogrenci_no, ogrenci_adSoyad, ogrenci_sinif, ogrenci_sube);

            database.ogrenciEkle(ogrenci);
            girisAlanlariTemizle();
            listeVeTabloYenile(null);
            AlertDialog.gosterAlert("Bilgi", "Öğrenci Eklendi", Alert.AlertType.INFORMATION);
        } catch (SQLException ex) {
            AlertDialog.gosterAlert("Uyarı", "Öğrenci EKLENEMEDİ!\n" + ex.getMessage(), Alert.AlertType.WARNING);
        } catch (Throwable ex) {
            girisThrowableActions(ex);
        }
    }

    public void ogrenciGuncelle() {
        // Veritabanındaki öğrenciyi günceller 
        // Veritabanından öğrenci al
        PushAnimation.nodeAnimation(btn_guncelle);
        try {
            girisAlanlariBicimKontrol();
            Ogrenci ogrenci = database.ogrenciGetir(Integer.parseInt(txtf_no.getText()));
            if (ogrenci == null) {
                throw new SQLException("Öğrenci Kaydı yok!\n No alanını değiştirmediğinizden emin olun.");
            }
            // Veritabanına kaydet
            int response = AlertDialog.gosterConfirm("Öğrenci güncellenecek. Emin misiniz?");
            if (response == 1) {
                // Öğrenci özelliklerini güncelle
                ogrenci.setAdSoyad(txtf_ad_soyad.getText());
                ogrenci.setSinif(Integer.parseInt(txtf_sinif.getText()));
                ogrenci.setSube(txtf_sube.getText());
                
                database.ogrenciGuncelle(ogrenci);
                girisAlanlariTemizle();
                listeVeTabloYenile(null);
                AlertDialog.gosterAlert("Bilgi", "Öğrenci güncellendi!", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException ex) {
            AlertDialog.gosterAlert("Uyarı", "Öğrenci GÜNCELLENMEDİ!\n" + ex.getMessage(), Alert.AlertType.WARNING);
        } catch (Throwable ex) {
            girisThrowableActions(ex);
        }
    }

    public void ogrenciSil() {
        // Veritabanından öğrenci siler 
        PushAnimation.nodeAnimation(btn_sil);

        try {
            girisAlanlariBicimKontrol();
            // buraya bir confirm ekle 
            Ogrenci ogrenci = database.ogrenciGetir(Integer.parseInt(txtf_no.getText()));
            int response = AlertDialog.gosterConfirm("Öğrenci silinecek. Emin misiniz?");
            if (response == 1) {
                // Veritabanından sil
                if (ogrenci != null) {
                    database.ogrenciSil(ogrenci);
                    girisAlanlariTemizle();
                     listeVeTabloYenile(null);
                    AlertDialog.gosterAlert("Bilgi", "Öğrenci silindi!", Alert.AlertType.INFORMATION);
                } else {
                    throw new SQLException("Öğrenci Kaydı yok!\n No alanını değiştirmediğinizden emin olun.");
                }
            } else {
                return;
            }
        } catch (SQLException ex) {
            AlertDialog.gosterAlert("Uyarı", "Öğrenci SİLİNMEDİ!\n" + ex.getMessage(), Alert.AlertType.WARNING);
        } catch (Throwable ex) {
            girisThrowableActions(ex);
        }

    }

    public void sinifGetir() {
        // seçilen sınıftan öğrencileri getirir. şube kontrolü?
        int secilenSinif = (int) combx_sinif.getValue();
        listeVeTabloYenile(secilenSinif);
    }

    public void subeGetir() {
        // Tabloda sadece seçilen şubeleri gösterir -> combobox
        // sınıf kontrolü?
        String secilenSube = (String) combx_sube.getValue();
        listeVeTabloYenile(secilenSube);
    }

    public void girisAlanlariTemizle() {
        txtf_ad_soyad.setText("");
        txtf_sinif.setText("");
        txtf_sube.setText("");
        txtf_no.setText("");
    }

    public void girisAlanlariBicimKontrol() throws Throwable {
        if (txtf_ad_soyad.getText().isEmpty() || txtf_sinif.getText().isEmpty() || txtf_sube.getText().isEmpty() || txtf_no.getText().isEmpty()) {
            throw new Throwable("bos girdi alani var");
        }
        if (!(txtf_ad_soyad.getText().matches("^[A-ZŞĞÜÖÇİŞ][a-zşğüöçış]* [A-ZŞĞÜÖÇİŞ]*$") || txtf_ad_soyad.getText().matches("^[A-ZŞĞÜÖÇİŞ][a-zşğüöçış]* [A-ZŞĞÜÖÇİŞ][a-zşğüöçış]* [A-ZŞĞÜÖÇİŞ]*$"))) {
            throw new Throwable("ad soyad formati yanlis");
        }

        if (!txtf_sinif.getText().matches("^(1[0-2]|[1-9])$")) {
            throw new Throwable("sinif formati yanlis");
        }

        if (!txtf_sube.getText().matches("^[A-Z]{1}$")) {
            throw new Throwable("sube formati yanlis");
        }

        if (!txtf_no.getText().matches("^[0-9]+$")) {
            throw new Throwable("ogrenci no formati yanlis");
        }

    }

    public void girisThrowableActions(Throwable ex) {
        if (ex.getMessage().equals("bos girdi alani var")) {
            AlertDialog.gosterAlert("Uyarı", "Girdi Alanları boş bırakılamaz!", Alert.AlertType.WARNING);
        }

        if (ex.getMessage().equals("ad soyad formati yanlis")) {
            AlertDialog.gosterAlert("Uyarı", "Ad Soyad formatı yanlış!\nDoğru format örnek -> Ali VELİOĞLU veya Mehmet Ali VELİOĞLU", Alert.AlertType.WARNING);
        }

        if (ex.getMessage().equals("sinif formati yanlis")) {
            AlertDialog.gosterAlert("Uyarı", "Sınıf formatı yanlış.\n1 den 12 ye kadar sayı girmelisiniz. ", Alert.AlertType.WARNING);
        }

        if (ex.getMessage().equals("sube formati yanlis")) {
            AlertDialog.gosterAlert("Uyarı", "Sınıf şubesi formatı yanlış.\nTek karakter ve büyük harf girebilirsiniz", Alert.AlertType.WARNING);
        }

        if (ex.getMessage().equals("ogrenci no formati yanlis")) {
            AlertDialog.gosterAlert("Uyarı", "Öğrenci no formatı yanlış. Sadece sayı girebilirsniz.", Alert.AlertType.WARNING);
        }
    }

    public void listeVeTabloYenile(Object obj) {
        try {
            if (obj == null) {
                ogrenciList = database.ogrenciListesiGetir();
            } else {
                if (obj instanceof Integer) {
                    ogrenciList = database.ogrenciListesiGetir((int) obj);
                } else {
                    ogrenciList = database.ogrenciListesiGetir((String) obj);
                }
            }
        } catch (SQLException ex) {
            ex.getMessage();
            AlertDialog.gosterAlert("Hata", "Öğrenci listesi getirilemedi!", Alert.AlertType.ERROR);
        }

        tableview_liste.setItems(ogrenciList);
        tableview_liste.refresh();
    }

    // Tablodaki tıklanan ögrencileri, ilgili texfield içlerine yazar
    public void tabloOgrenciAl() {
        tableview_liste.setRowFactory(tv -> {
            TableRow<Ogrenci> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Ogrenci clickedItem = row.getItem();
                    txtf_no.setText(String.valueOf(clickedItem.getNo()));
                    txtf_ad_soyad.setText(clickedItem.getAdSoyad());
                    txtf_sinif.setText(String.valueOf(clickedItem.getSinif()));
                    txtf_sube.setText(clickedItem.getSube());
                }
            });
            return row;
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        adSoyadCol.setCellValueFactory(new PropertyValueFactory<>("adSoyad"));
        sinifCol.setCellValueFactory(new PropertyValueFactory<>("sinif"));
        subeCol.setCellValueFactory(new PropertyValueFactory<>("sube"));

        try {
            database = DatabaseOperations.getInstance();
            ogrenciList = database.ogrenciListesiGetir();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.getMessage();
            AlertDialog.gosterAlert("HATA", "Veritabanına BAĞLANILAMADI!", Alert.AlertType.ERROR);
        } catch (IOException ex) {
           AlertDialog.gosterAlert("HATA", ex.getMessage(), Alert.AlertType.ERROR);
        }
        tableview_liste.setItems(ogrenciList);

        // Sınıf ve Şube seçim combobox ayarlama    
        Set<Integer> ogrenciSinifSet = new HashSet<>();
        Set<String> ogrenciSubeSet = new HashSet<>();
        for (Ogrenci ogrenci : ogrenciList) {
            ogrenciSinifSet.add(ogrenci.getSinif());
            ogrenciSubeSet.add(ogrenci.getSube());
        }

        ogrenciSinifList.addAll(ogrenciSinifSet);
        ogrenciSubeList.addAll(ogrenciSubeSet);
        combx_sinif.setItems(ogrenciSinifList);
        combx_sube.setItems(ogrenciSubeList);
        //---------------------------------------

        // Tableview satır tıklayınca bilgileri textfield'lere aktar
        tabloOgrenciAl();
    }

}
