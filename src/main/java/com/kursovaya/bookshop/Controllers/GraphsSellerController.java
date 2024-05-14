package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.DataBaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GraphsSellerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PieChart authorsPieChart;

    @FXML
    private Button backToHomeButton;

    @FXML
    private BarChart<String, Integer> booksBarChart;

    @FXML
    private Label helloLabel;

    @FXML
    void initialize() {
        backToHomeButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/HomeSeller.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        try {
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // SQL запрос, чтобы выбрать топ-3 популярных авторов
            String selectQuery = "SELECT bookAuthor, COUNT(*) AS count FROM Books WHERE bookId IN (SELECT bookId FROM UserBooks) GROUP BY bookAuthor ORDER BY count DESC LIMIT 3";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Очистить PieChart перед добавлением новых значений
            authorsPieChart.getData().clear();

            // Добавить данные в PieChart
            while (resultSet.next()) {
                String author = resultSet.getString("bookAuthor");
                int count = resultSet.getInt("count");
                PieChart.Data slice = new PieChart.Data(author, count);
                authorsPieChart.getData().add(slice);
            }

            // SQL запрос, чтобы выбрать топ-5 популярных названий книг
            String selectBooksQuery = "SELECT B.bookName, COUNT(*) AS count FROM Books B INNER JOIN UserBooks UB ON B.bookId = UB.bookId GROUP BY B.bookName ORDER BY count DESC LIMIT 5";
            PreparedStatement booksStatement = dataBaseHandler.getConnection().prepareStatement(selectBooksQuery);
            ResultSet booksResultSet = booksStatement.executeQuery();

            // Очистить BarChart перед добавлением новых значений
            booksBarChart.getData().clear();

            // Добавить данные в BarChart
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            while (booksResultSet.next()) {
                String bookName = booksResultSet.getString("bookName");
                int count = booksResultSet.getInt("count");
                series.getData().add(new XYChart.Data<>(bookName, count));
            }
            booksBarChart.getData().add(series);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
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
}
