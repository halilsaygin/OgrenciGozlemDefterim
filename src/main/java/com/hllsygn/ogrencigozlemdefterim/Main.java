package com.hllsygn.ogrencigozlemdefterim;

import com.hllsygn.ogrencigozlemdefterim.database.DBConnect;
import java.io.IOException;
import java.sql.SQLException;
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
    public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {
        // Veritabanı bağlantısını başlat
        DBConnect.getInstance();

        _primaryStage = primaryStage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/hllsygn/ogrencigozlemdefterim/fxmlfiles/Main.fxml")));
        Scene scene = new Scene(root);

        // ayarla uygualama pencere ikonu
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/hllsygn/ogrencigozlemdefterim/app_icon.png")));
        _primaryStage.getIcons().add(icon);

        _primaryStage.setResizable(false);
        _primaryStage.setTitle("Öğrenci Gözlem Defterim");
        _primaryStage.setScene(scene);
        _primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        // Uygulama kapanırken veritabanı bağlantısını kapat
        DBConnect.getInstance().closeConnection();
        super.stop();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
