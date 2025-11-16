package com.hllsygn.ogrencigozlemdefterim;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage _primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        _primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/fxmlfiles/Main.fxml")));
        Scene scene = new Scene(root);

        // ayarla uygualama pencere ikonu
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/hllsygn/ogrencigozlemdefterim/icon_app.png")));
        _primaryStage.getIcons().add(icon);

        _primaryStage.setResizable(false);
        _primaryStage.setTitle("Öğrenci Gözlem Defterim");
        _primaryStage.setScene(scene);
        _primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
