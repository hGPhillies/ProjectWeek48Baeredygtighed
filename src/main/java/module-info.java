module org.example.projectweek48baeredygtighed {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.datatransfer;
    requires org.controlsfx.controls;


    opens org.example.projectweek48baeredygtighed to javafx.fxml;
    exports org.example.projectweek48baeredygtighed;
}