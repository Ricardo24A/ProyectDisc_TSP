module pepep.mavenproject1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web; // Este módulo habilita el uso de WebView y WebEngine
    requires java.net.http; // Si trabajas con APIs HTTP
    requires jdk.jsobject;
    requires com.google.gson;
    opens pepep.mavenproject1 to javafx.fxml; // Permite el acceso reflexivo a clases de tu paquete
    exports pepep.mavenproject1;
}
