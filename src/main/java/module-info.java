module org.example.socialnet {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.socialnet to javafx.fxml;
    exports org.example.socialnet;
}