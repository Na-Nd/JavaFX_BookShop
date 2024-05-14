package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.Persons.Admin;
import com.kursovaya.bookshop.Persons.Seller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeAdminController {

    private static Admin registeredAdminToTables;
    private static Admin loginedAdminToTables;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToHomeButton;

    @FXML
    private Label helloLabel;

    @FXML
    private Button toUsersTableButton;

    @FXML
    void initialize() {
        backToHomeButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/startWindow.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        toUsersTableButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/TablesAdmin.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Admin loginedAdmin = LoginAdminController.getLoginedAdmin();
        Admin registeredAdmin = RegisterAdminController.getRegisteredAdmin();
        loginedAdminToTables = loginedAdmin;
        registeredAdminToTables = registeredAdmin;

        if (registeredAdmin != null) {
            helloLabel.setText("Добро пожаловать, " + registeredAdmin.getName() + "!");
        } else if (loginedAdmin != null) {
            helloLabel.setText("Добро пожаловать, " + loginedAdmin.getName() + "!");
        } else {
            helloLabel.setText("Пользователь не обнаружен");
        }
    }

    public void openNewScene(String window) throws IOException {
        Stage currentStage = (Stage) backToHomeButton.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        currentStage.setTitle("Authentication");
        currentStage.setScene(scene);
        currentStage.show();
    }

    public static Admin getRegisteredAdminToTables(){return registeredAdminToTables;}
    public static Admin getLoginedAdminToTables(){return loginedAdminToTables;}

}
