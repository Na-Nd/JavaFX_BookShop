package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.Persons.Seller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeSellerController {
    private static Seller loginedSellerToGraphsAndTables;
    private static Seller registeredSellerToGraprhsAndTables;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToHomeButton;

    @FXML
    private Label helloLabel;

    @FXML
    private Button toGraphsButton;

    @FXML
    private Button toTableButton;

    @FXML
    void initialize() {
        backToHomeButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/startWindow.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Seller loginedSeller = LoginSellerController.getLoginedSeller();
        Seller registeredSeller = RegisterSellerController.getRegisteredSeller();
        loginedSellerToGraphsAndTables = loginedSeller;
        registeredSellerToGraprhsAndTables = registeredSeller;

        if (registeredSeller != null) {
            helloLabel.setText("Добро пожаловать, " + registeredSeller.getName() + "!");
        } else if (loginedSeller != null) {
            helloLabel.setText("Добро пожаловать, " + loginedSeller.getName() + "!");
        } else {
            helloLabel.setText("Пользователь не обнаружен");
        }

        toGraphsButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/GraphsSeller.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        toTableButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/TablesSeller.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    public void openNewScene(String window) throws IOException {
        Stage currentStage = (Stage) toGraphsButton.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        currentStage.setTitle("Authentication");
        currentStage.setScene(scene);
        currentStage.show();
    }

    public static Seller getLoginedSellerToGraphsAndTables(){return loginedSellerToGraphsAndTables;}
    public static Seller getRegisteredSellerToGraprhsAndTables(){return registeredSellerToGraprhsAndTables;}

}
