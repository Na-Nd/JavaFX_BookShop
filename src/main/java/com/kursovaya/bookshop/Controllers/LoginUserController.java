package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.DataBaseHandler;
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

public class LoginUserController {

    public static User loginedUser;

    @FXML
    private Label infoLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToMainWindowButton;

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
                    loginNewUser(loginText, passwordText);
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

    private void loginNewUser(String loginText, String passwordText) throws IOException {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(passwordText);
        ResultSet resultSet = dataBaseHandler.getUserFromDB(user);
        boolean flag = false;

        try {
            if (resultSet.next()) {
                flag = true;
                int userid = resultSet.getInt("userId");
                String name = resultSet.getString("userName");
                String surname = resultSet.getString("userSurname");
                String login = resultSet.getString("userLogin");
                String password = resultSet.getString("userPassword");
                user.setId(userid);
                user.setName(name);
                user.setSurname(surname);
                user.setLogin(login);
                user.setPassword(password);
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
            loginedUser = user;
            openNewScene("/com/kursovaya/bookshop/views/HomeUser.fxml");

        }
    }

    public static User getLoginedUser(){return loginedUser;}

}
