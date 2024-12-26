module pepep.mavenproject1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens pepep.mavenproject1 to javafx.fxml;
    exports pepep.mavenproject1;
}
