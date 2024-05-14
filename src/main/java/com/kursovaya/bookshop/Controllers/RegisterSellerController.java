package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.DataBaseHandler;
import com.kursovaya.bookshop.Persons.Seller;
import com.kursovaya.bookshop.Persons.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterSellerController {
    public static Seller registeredSeller;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToMainWindowButton;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField loginField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField sellerKeyField;

    @FXML
    private TextField surnameField;

    @FXML
    void initialize() {
        backToMainWindowButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/startWindow.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        registerButton.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String passwordText= passwordField.getText().trim();
            String nameText = nameField.getText().trim();
            String surnameText = surnameField.getText().trim();
            String keyText = sellerKeyField.getText().trim();

            if(!loginText.equals("") && !passwordText.equals("") && !nameText.equals("") && !surnameText.equals("") && keyText.equals("seller")){ // Если поля не пустые
                if (checkLoginExists(loginText)) {
                    infoLabel.setText("Такой пользователь уже существует!");
                    return; // Выйти из метода, если логин уже существует
                }
                registerNewSeller();
                try {
                    openNewScene("/com/kursovaya/bookshop/views/HomeSeller.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                infoLabel.setText("Не все поля заполнены!");
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> infoLabel.setText("")));
                timeline.play();
            }
        });

    }


    public void openNewScene(String window) throws IOException {
        Stage currentStage = (Stage) backToMainWindowButton.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        currentStage.setTitle("Authentication");
        currentStage.setScene(scene);
        currentStage.show();
    }

    public void registerNewSeller(){
        DataBaseHandler dataBaseHandler = new DataBaseHandler();

        String registerName = nameField.getText();
        String registerSurname = surnameField.getText();
        String registerLogin = loginField.getText();
        String registerPassword = passwordField.getText();

        Seller seller = new Seller(registerName, registerSurname, registerLogin, registerPassword);

        dataBaseHandler.registerSellerInDB(seller);

        ResultSet resultSet = dataBaseHandler.getSellerFromDB(seller);
        try{
            if(resultSet.next()){
                int sellerId = resultSet.getInt("sellerId");
                String sellerName = resultSet.getString("sellerName");
                String sellerSurname = resultSet.getString("sellerSurname");
                String sellerLogin = resultSet.getString("sellerLogin");
                String sellerPassword = resultSet.getString("sellerPassword");

                seller.setId(sellerId);
                seller.setName(sellerName);
                seller.setSurname(sellerSurname);
                seller.setLogin(sellerLogin);
                seller.setPassword(sellerPassword);

                registeredSeller = seller;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private boolean checkLoginExists(String login) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        return dataBaseHandler.checkSellerIfLoginExists(login);
    }
    public static Seller getRegisteredSeller(){return registeredSeller;}

}
