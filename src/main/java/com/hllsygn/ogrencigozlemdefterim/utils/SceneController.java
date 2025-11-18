package com.hllsygn.ogrencigozlemdefterim.utils;

import com.hllsygn.ogrencigozlemdefterim.Main;
import com.hllsygn.ogrencigozlemdefterim.controllers.GozlemEkraniController;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SceneController {

    private static SceneController instance;
    private final Stage stage = Main._primaryStage;
    private final double DURATION = 0.8;
    private final String ANA_SAHNE_FXML = "Main.fxml";
    private final String ANA_SAHNE_STIL_SAYFA = "main.css";
    private final String DATABASE_SAHNE_FXML = "Database.fxml";
    private final String DATABASE_STIL_SAYFA = "database.css";
    private final String GOZLEM_EKRANI_FXML = "GozlemEkrani.fxml";
    private final String GOZLEM_EKRANI_STIL_SAYFA = "gozlem_ekrani.css";


    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    public void sahneAc(String fxml_name) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml_name));
        Scene scene = new Scene(root);

        cssAyarla(scene, fxml_name);

        stage.setScene(scene);
        sahneGecisAnimasyon(root, scene, false);
    }

    public void anaSahne_Ac() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(ANA_SAHNE_FXML));
        Scene scene = new Scene(root);

        cssAyarla(scene, ANA_SAHNE_FXML);

        // sahnenin görünmemesini sağlayan bir geçiş süresi ekle
        stage.setScene(scene);
        sahneGecisAnimasyon(root, scene, true);

    }

    public void databaseSahneAc() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/fxmlfiles/" + DATABASE_SAHNE_FXML));
        Scene scene = new Scene(root);

        cssAyarla(scene, DATABASE_SAHNE_FXML);

        stage.setScene(scene);
        sahneGecisAnimasyon(root, scene, false);
    }

    public void gozlemEkraniAc(String className) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/fxmlfiles/" + GOZLEM_EKRANI_FXML));
        Parent root = loader.load();

        GozlemEkraniController controller = loader.getController();
        // Hata düzeltildi: initData -> setSinifAdi
        controller.setSinifAdi(className);

        Scene scene = new Scene(root);
        cssAyarla(scene, GOZLEM_EKRANI_FXML);
        stage.setScene(scene);
        sahneGecisAnimasyon(root, scene, false);
    }

    public void anaSahneDon() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/fxmlfiles/" + ANA_SAHNE_FXML));
        Scene scene = new Scene(root);

        cssAyarla(scene, ANA_SAHNE_FXML);

        stage.setScene(scene);
        sahneGecisAnimasyon(root, scene, true);
    }

    public void sahneGecisAnimasyon(Parent root, Scene scene, boolean isReverse) {
        // Sahne genişliğini alarak dinamik bir şekilde animasyonu başlat
        double sceneWidth = scene.getWidth();
        TranslateTransition transition = new TranslateTransition(Duration.seconds(DURATION), root);
        if (isReverse) transition.setFromX(-sceneWidth); // Sola doğru başlat
        else transition.setFromX(sceneWidth);
        transition.setToX(0); // Hedef konum
        transition.play();
    }

    public void cssAyarla(Scene scene, String fxmlName) {
        String css = "";
        if (fxmlName.equals(DATABASE_SAHNE_FXML)) {
            css = this.getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/styles/" + DATABASE_STIL_SAYFA).toExternalForm();
        } else if (fxmlName.equals(GOZLEM_EKRANI_FXML)) {
            css = this.getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/styles/" + GOZLEM_EKRANI_STIL_SAYFA).toExternalForm();
        } else {
            css = this.getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/styles/" + ANA_SAHNE_STIL_SAYFA).toExternalForm();
        }
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
    }

}
