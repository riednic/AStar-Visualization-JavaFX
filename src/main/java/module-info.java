module de.riedlnico {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires annotations;

    opens de.riedlnico to javafx.fxml;
    exports de.riedlnico;
}