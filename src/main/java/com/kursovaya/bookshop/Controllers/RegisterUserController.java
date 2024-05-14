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

public class RegisterUserController {
    public static User registeredUser;

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
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

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
            String passwordText = passwordField.getText().trim();
            String nameText = nameField.getText().trim();
            String surnameText = surnameField.getText().trim();

            if (!loginText.equals("") && !passwordText.equals("") && !nameText.equals("") && !surnameText.equals("")) {
                if (checkLoginExists(loginText)) {
                    infoLabel.setText("Такой пользователь уже существует!");
                    return; // Выйти из метода, если логин уже существует
                }
                registerNewUser();
                try {
                    openNewScene("/com/kursovaya/bookshop/views/HomeUser.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                infoLabel.setText("Не все поля заполнены!");
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> infoLabel.setText("")));
                timeline.play();
            }
        });
    }

    private boolean checkLoginExists(String login) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        return dataBaseHandler.checkIfLoginExists(login);
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

    public void registerNewUser(){
        DataBaseHandler dataBaseHandler = new DataBaseHandler();

        String registerName = nameField.getText();
        String registerSurname = surnameField.getText();
        String registerLogin = loginField.getText();
        String registerPassword = passwordField.getText();

        User user = new User(registerName, registerSurname, registerLogin, registerPassword);
        dataBaseHandler.registerUserInDB(user);

        // Дальше берем пользователя чтобы его передать в домашнюю страницу
        ResultSet resultSet = dataBaseHandler.getUserFromDB(user);

        try {
            if (resultSet.next()) {
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

                registeredUser = user; // Записываем пользователя для передачи
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static User getRegisteredUser(){return registeredUser;}
}

