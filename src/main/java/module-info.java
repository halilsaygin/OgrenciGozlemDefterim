module com.hllsygn.ogrencigozlemdefterim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.hllsygn.ogrencigozlemdefterim to javafx.fxml;
    exports com.hllsygn.ogrencigozlemdefterim;
    exports com.hllsygn.ogrencigozlemdefterim.controllers;
    opens com.hllsygn.ogrencigozlemdefterim.controllers to javafx.fxml;
    exports com.hllsygn.ogrencigozlemdefterim.utils;
    opens com.hllsygn.ogrencigozlemdefterim.utils to javafx.fxml;
    exports com.hllsygn.ogrencigozlemdefterim.models;
    opens com.hllsygn.ogrencigozlemdefterim.models to javafx.fxml;
}
