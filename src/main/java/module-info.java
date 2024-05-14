module com.kursovaya.bookshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.kursovaya.bookshop.Controllers to javafx.fxml;
    opens com.kursovaya.bookshop.Persons to javafx.base;
    exports com.kursovaya.bookshop;
}