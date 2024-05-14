package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class startWindowController {
    public boolean adminFlag = false;
    public boolean sellerFlag = false;
    public boolean userFlag = false;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginButton;

    @FXML
    protected Label infoLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button selectSellerButton;

    @FXML
    private Button selectAdminButton;

    @FXML
    private Button selectUserButton;

    @FXML
    void initialize() {
        selectAdminButton.setOnAction(event -> {
            adminFlag = true;
            sellerFlag = false;
            userFlag = false;
        });

        selectSellerButton.setOnAction(event -> {
            adminFlag = false;
            sellerFlag = true;
            userFlag = false;
        });

        selectUserButton.setOnAction(event -> {
            adminFlag = false;
            sellerFlag = false;
            userFlag = true;
        });

        registerButton.setOnAction(event -> {
            try{
                if(adminFlag) {
                    openNewScene("/com/kursovaya/bookshop/views/RegisterAdmin.fxml");
                }
                else if(sellerFlag){
                    openNewScene("/com/kursovaya/bookshop/views/RegisterSeller.fxml");
                }
                else if(userFlag){
                    openNewScene("/com/kursovaya/bookshop/views/RegisterUser.fxml");
                }
                else {
                    infoLabel.setText("Не выбран пользователь!");
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> infoLabel.setText("")));
                    timeline.play();
                }
            } catch (IOException e){
                e.printStackTrace();
            }

        });

        loginButton.setOnAction(event -> {
            try{
                if(adminFlag) {
                    openNewScene("/com/kursovaya/bookshop/views/LoginAdmin.fxml");
                }
                else if(sellerFlag){
                    openNewScene("/com/kursovaya/bookshop/views/LoginSeller.fxml");
                }
                else if(userFlag){
                    openNewScene("/com/kursovaya/bookshop/views/LoginUser.fxml");
                }
                else {
                    infoLabel.setText("Не выбран пользователь!");
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> infoLabel.setText("")));
                    timeline.play();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        });

    }
    public void openNewScene(String window) throws IOException {
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(window));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);
        currentStage.setTitle("Authentication");
        currentStage.setScene(scene);
        currentStage.show();
    }


}
