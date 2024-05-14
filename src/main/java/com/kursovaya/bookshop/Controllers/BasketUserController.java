package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.DataBaseHandler;
import com.kursovaya.bookshop.Persons.Book;
import com.kursovaya.bookshop.Persons.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BasketUserController {
    private int totalCost = 0;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backToHomeButton;

    @FXML
    private ScrollPane basketScrollPane;

    @FXML
    private Label basketSummLabel;

    @FXML
    private VBox basketVBox;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label helloLabel;

    @FXML
    private void loadBooksToBasket() {
        try {
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            User currentUser = HomeUserController.getLoginedUserToCatalogAndToBasket();
            if (currentUser == null) {
                currentUser = HomeUserController.getRegisteredUserToCatalogAndToBasket();
            }

            if (currentUser != null) {
                String selectQuery = "SELECT B.* FROM UserBooks UB INNER JOIN Books B ON UB.bookId = B.bookId WHERE UB.userId = ?";
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
                preparedStatement.setInt(1, currentUser.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int bookId = resultSet.getInt("bookId");
                    String bookName = resultSet.getString("bookName");
                    String bookAuthor = resultSet.getString("bookAuthor");
                    String bookDescription = resultSet.getString("bookDescription");
                    String bookImageLocation = resultSet.getString("bookImageLocation");
                    int bookCount = resultSet.getInt("bookCount");
                    int bookCost = resultSet.getInt("bookCost");

                    Book book = new Book();
                    book.setId(bookId);
                    book.setName(bookName);
                    book.setAuthor(bookAuthor);
                    book.setDescription(bookDescription);
                    book.setImageLocation(bookImageLocation);
                    book.setCount(bookCount);
                    book.setCost(bookCost);

                    totalCost += bookCost;

                    ImageView bookImageView = new ImageView(new Image("file:" + bookImageLocation));
                    bookImageView.setFitHeight(190.0);
                    bookImageView.setFitWidth(147.0);

                    bookImageView.setOnMouseClicked(event -> {
                        StringBuilder formattedText = new StringBuilder();
                        int charCount = 0;
                        StringBuilder word = new StringBuilder();

                        for(char c : bookDescription.toCharArray()){
                            if(Character.isWhitespace(c)){
                                formattedText.append(word).append(c);
                                charCount += word.length()+1;
                                word.setLength(0);
                            } else{
                                word.append(c);
                            }

                            if(charCount >= 25){
                                formattedText.append("\n");
                                charCount=0;
                            }
                        }
                        formattedText.append(word);
                        descriptionLabel.setText(formattedText.toString());
                    });

                    Button bookButton = new Button("Удалить");
                    bookButton.setStyle("-fx-background-color: #008000; -fx-text-fill: #FFFFFF;");

                    HBox bookEntry = new HBox(bookImageView, bookButton);
                    bookEntry.setAlignment(Pos.CENTER);
                    basketVBox.getChildren().add(bookEntry);

                    User finalCurrentUser = currentUser;
                    bookButton.setOnAction(event -> {
                        try {
                            String deleteQuery = "DELETE FROM UserBooks WHERE userId = ? AND bookId = ?";
                            PreparedStatement preparedStatement2 = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
                            preparedStatement2.setInt(1, finalCurrentUser.getId());
                            preparedStatement2.setInt(2, book.getId());
                            int res = preparedStatement2.executeUpdate();
                            if (res > 0) {
                                System.out.println("Строка успешно удалена из таблицы.");
                                basketVBox.getChildren().remove(bookEntry);
                                totalCost -= bookCost;
                                // Увеличиваем на 1 количество книги в таблице Books
                                String updateQuery = "UPDATE Books SET bookCount = bookCount + 1 WHERE bookId = ?";
                                PreparedStatement updateStatement = dataBaseHandler.getConnection().prepareStatement(updateQuery);
                                updateStatement.setInt(1, book.getId());
                                updateStatement.executeUpdate();
                            } else {
                                System.out.println("Строка не была найдена или не удалось удалить ее из таблицы.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            openNewScene("/com/kursovaya/bookshop/views/BasketUser.fxml");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        loadBooksToBasket();

        basketSummLabel.setText("Итого: " + totalCost);

        backToHomeButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/HomeUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
