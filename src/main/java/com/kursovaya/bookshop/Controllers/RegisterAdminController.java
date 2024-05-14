package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.DataBaseHandler;
import com.kursovaya.bookshop.Persons.Admin;
import com.kursovaya.bookshop.Persons.Seller;
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

public class RegisterAdminController {
    public static Admin registeredAdmin;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField adminKeyField;

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
            String keyText = adminKeyField.getText().trim();

            if(!loginText.equals("") && !passwordText.equals("") && !nameText.equals("") && !surnameText.equals("") && keyText.equals("admin")){ // Если поля не пустые
                if (checkLoginExists(loginText)) {
                    infoLabel.setText("Такой пользователь уже существует!");
                    return; // Выйти из метода, если логин уже существует
                }
                registerNewAdmin();
                try {
                    openNewScene("/com/kursovaya/bookshop/views/HomeAdmin.fxml");
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

    private boolean checkLoginExists(String login) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        return dataBaseHandler.checkAdminIfLoginExists(login);
    }

    public void registerNewAdmin(){
        DataBaseHandler dataBaseHandler = new DataBaseHandler();

        String registerName = nameField.getText();
        String registerSurname = surnameField.getText();
        String registerLogin = loginField.getText();
        String registerPassword = passwordField.getText();

        Admin admin = new Admin(registerName, registerSurname, registerLogin, registerPassword);


        dataBaseHandler.registerAdminInDB(admin);

        ResultSet resultSet = dataBaseHandler.getAdminFromDB(admin);
        try{
            if(resultSet.next()){
                int adminId = resultSet.getInt("adminId");
                String adminName = resultSet.getString("adminName");
                String adminSurname = resultSet.getString("adminSurname");
                String adminLogin = resultSet.getString("adminLogin");
                String adminPassword = resultSet.getString("adminPassword");

                admin = new Admin(adminId, adminName, adminSurname, adminPassword, adminLogin);

                registeredAdmin = admin;

            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Admin getRegisteredAdmin(){
        return registeredAdmin;
    }

}
