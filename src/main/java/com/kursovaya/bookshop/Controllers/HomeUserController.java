package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.Persons.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeUserController {

    public static User loginedUserToCatalogAndToBasket;
    public static User registeredUserToCatalogAndToBasket;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToHomeButton;

    @FXML
    private Label helloLabel;

    @FXML
    private Button toBasketButton;

    @FXML
    private Button toCatalogButton;

    @FXML
    void initialize() {
        backToHomeButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/startWindow.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        User loginedUser = LoginUserController.getLoginedUser();
        User registeredUser = RegisterUserController.getRegisteredUser();
        loginedUserToCatalogAndToBasket = loginedUser;
        registeredUserToCatalogAndToBasket = registeredUser;

        if (registeredUser != null) {
            helloLabel.setText("Добро пожаловать, " + registeredUser.getName() + "!");
        } else if (loginedUser != null) {
            helloLabel.setText("Добро пожаловать, " + loginedUser.getName() + "!");
        } else {
            helloLabel.setText("Пользователь не обнаружен");
        }

        toCatalogButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/CatalogUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        toBasketButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/BasketUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void openNewScene(String window) throws IOException {
        Stage currentStage = (Stage) toCatalogButton.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        currentStage.setTitle("Authentication");
        currentStage.setScene(scene);
        currentStage.show();
    }

    public static User getLoginedUserToCatalogAndToBasket(){
        return loginedUserToCatalogAndToBasket;
    }
    public static User getRegisteredUserToCatalogAndToBasket() { return registeredUserToCatalogAndToBasket; }

}
