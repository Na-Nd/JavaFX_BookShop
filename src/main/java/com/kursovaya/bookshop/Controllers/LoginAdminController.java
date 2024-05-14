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

public class LoginAdminController {

    public static Admin loginedAdmin;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToMainWindowButton;

    @FXML
    private Label infoLabel;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

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
                    loginNewAdmin(loginText, passwordText);
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

    private void loginNewAdmin(String loginText, String passwordText) throws IOException {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        Admin admin = new Admin();
        admin.setLogin(loginText);
        admin.setPassword(passwordText);
        ResultSet resultSet = dataBaseHandler.getAdminFromDB(admin);
        boolean flag = false;

        try {
            if (resultSet.next()) {
                flag = true;
                int adminid = resultSet.getInt("adminId");
                String adminname = resultSet.getString("adminName");
                String adminsurname = resultSet.getString("adminSurname");
                String adminlogin = resultSet.getString("adminLogin");
                String adminpassword = resultSet.getString("adminPassword");
                admin.setId(adminid);
                admin.setName(adminname);
                admin.setSurname(adminsurname);
                admin.setLogin(adminlogin);
                admin.setPassword(adminpassword);
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
            loginedAdmin = admin;
            System.out.println("Передан пользователь: " + loginedAdmin.toString());
            openNewScene("/com/kursovaya/bookshop/views/HomeAdmin.fxml");

        }
    }

    public static Admin getLoginedAdmin(){return loginedAdmin;}



}
