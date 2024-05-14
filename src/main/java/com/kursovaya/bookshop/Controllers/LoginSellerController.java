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

public class LoginSellerController {

    public static Seller loginedSeller;

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
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    void initialize() {
        backToMainWindowButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/startWindow.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loginButton.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String passwordText = passwordField.getText().trim();

            if(!loginText.equals("") && !passwordText.equals("")){
                try {
                    loginNewSeller(loginText, passwordText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
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

    private void loginNewSeller(String loginText, String passwordText) throws IOException {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        Seller seller = new Seller();
        seller.setLogin(loginText);
        seller.setPassword(passwordText);
        ResultSet resultSet = dataBaseHandler.getSellerFromDB(seller);
        boolean flag = false;

        try {
            if (resultSet.next()) {
                flag = true;
                int sellerid = resultSet.getInt("sellerId");
                String sellername = resultSet.getString("sellerName");
                String sellersurname = resultSet.getString("sellerSurname");
                String sellerlogin = resultSet.getString("sellerLogin");
                String sellerpassword = resultSet.getString("sellerPassword");
                seller.setId(sellerid);
                seller.setName(sellername);
                seller.setSurname(sellersurname);
                seller.setLogin(sellerlogin);
                seller.setPassword(sellerpassword);
            }
            else {
                infoLabel.setText("Поля неправильно заполнены!");
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> infoLabel.setText("")));
                timeline.play();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        if (flag) {
            loginedSeller = seller;
            System.out.println("Передан пользователь: " + loginedSeller.toString());
            openNewScene("/com/kursovaya/bookshop/views/HomeSeller.fxml");

        }
    }

    public static Seller getLoginedSeller(){return loginedSeller;}

}
