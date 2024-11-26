module org.example.projectweek48baeredygtighed {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.projectweek48baeredygtighed to javafx.fxml;
    exports org.example.projectweek48baeredygtighed;
}