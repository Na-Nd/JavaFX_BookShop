package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import com.kursovaya.bookshop.Persons.Book;
import com.kursovaya.bookshop.Persons.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.kursovaya.bookshop.DataBaseHandler;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class CatalogUserController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    VBox VBoxForSearchedImage;

    @FXML
    private TextField authorFilterField;

    @FXML
    private Button backToHomeButton;

    @FXML
    private ScrollPane catalogScrollPane;

    @FXML
    private VBox catalogVBox;

    @FXML
    private TextField costFilterField;

    @FXML
    private TextField countFilterField;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button filterButton;

    @FXML
    private Label helloLabel;

    @FXML
    private ImageView searchBookImage;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private void loadImagesToImageViews(){
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        String selectQuery = "SELECT * FROM Books";
        try {
            Statement statement = dataBaseHandler.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            int imageIndex = 1;
            loadMethod(resultSet, imageIndex);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void loadMethod(ResultSet resultSet, int imageIndex) throws SQLException {
        while (resultSet.next()){
            int bookId = resultSet.getInt("bookId");
            String bookName = resultSet.getString("bookName");
            String bookAuthor = resultSet.getString("bookAuthor");
            String bookDescription = resultSet.getString("bookDescription");
            String bookImageLocation = resultSet.getString("bookImageLocation");
            int bookCount = resultSet.getInt("bookCount");
            int bookCost = resultSet.getInt("bookCost");

            Book currentBook = new Book();
            currentBook.setId(bookId);
            currentBook.setName(bookName);
            currentBook.setAuthor(bookAuthor);
            currentBook.setDescription(bookDescription);
            currentBook.setImageLocation(bookImageLocation);
            currentBook.setCount(bookCount);
            currentBook.setCost(bookCost);

            if (filterBook(currentBook)) {
                ImageView imageView = new ImageView();
                imageView.setFitHeight(260.0);
                imageView.setFitWidth(240.0);

                Image image = new Image("file:" + bookImageLocation);
                imageView.setImage(image);

                imageView.setOnMouseClicked(event -> {
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


                Button button = new Button("В корзину");
                button.setStyle("-fx-background-color: #008000; -fx-text-fill: #FFFFFF;");
                button.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
                Label label = new Label("Цена: " + currentBook.getCost());
                label.setStyle("-fx-background-color: #34C924; -fx-text-fill: #FFFFFF;");

                User loginedUser = HomeUserController.getLoginedUserToCatalogAndToBasket();
                User registeredUser = HomeUserController.getRegisteredUserToCatalogAndToBasket();
                User currentUser = new User();
                if(loginedUser != null){
                    currentUser = loginedUser;
                }
                if(registeredUser != null){
                    currentUser = registeredUser;
                }
                System.out.println("Пользователь в каталоге:" + currentUser.toString());

                User finalCurrentUser = currentUser;
                button.setOnAction(event -> {
                    //if(){
                        DataBaseHandler dataBaseHandler2 = new DataBaseHandler();
                        String insertQuery = "INSERT INTO UserBooks (userId, bookId) VALUES(?,?)";
                        try {
                            PreparedStatement preparedStatement = dataBaseHandler2.getConnection().prepareStatement(insertQuery);
                            preparedStatement.setInt(1, finalCurrentUser.getId());
                            preparedStatement.setInt(2, currentBook.getId());
                            preparedStatement.executeUpdate();

                            String updateQuery = "UPDATE Books SET bookCount = ? WHERE bookId = ?";
                            PreparedStatement updateStatement = dataBaseHandler2.getConnection().prepareStatement(updateQuery);
                            updateStatement.setInt(1, currentBook.getCount()-1);
                            updateStatement.setInt(2, currentBook.getId());
                            updateStatement.executeUpdate();

                        } catch (SQLException | ClassNotFoundException e){
                            e.printStackTrace();
                        }
                    //}
                    try {
                        openNewScene("/com/kursovaya/bookshop/views/CatalogUser.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                VBox vBox = new VBox(label, button);
                vBox.setAlignment(Pos.CENTER);
                vBox.setSpacing(5);

                HBox hbox = new HBox(imageView, vBox);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(5);

                catalogVBox.getChildren().add(hbox);
                imageIndex++;
            }
        }
    }

    private boolean filterBook(Book book) {
        String authorFilter = authorFilterField.getText().trim();
        String costFilter = costFilterField.getText().trim();
        String countFilter = countFilterField.getText().trim();

        boolean matchesAuthor = authorFilter.isEmpty() || book.getAuthor().toLowerCase().contains(authorFilter.toLowerCase());
        boolean matchesCost = costFilter.isEmpty() || book.getCost() <= Integer.parseInt(costFilter);
        boolean matchesCount = countFilter.isEmpty() || book.getCount() <= Integer.parseInt(countFilter);

        return matchesAuthor && matchesCost && matchesCount;
    }

    @FXML
    void initialize() {
        loadImagesToImageViews();

        User loginedUserFromHome = HomeUserController.getLoginedUserToCatalogAndToBasket();
        User registeredUserFromHome = HomeUserController.getRegisteredUserToCatalogAndToBasket();
        if (loginedUserFromHome != null) {
            helloLabel.setText("Здравствуйте, " + loginedUserFromHome.getName());
        }
        if (registeredUserFromHome != null) {
            helloLabel.setText("Здравствуйте, " + registeredUserFromHome.getName());
        }

        backToHomeButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/HomeUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        filterButton.setOnAction(event -> {
            catalogVBox.getChildren().clear();

            try {
                DataBaseHandler dataBaseHandler = new DataBaseHandler();
                String selectQuery = "SELECT * FROM Books WHERE ";
                String filterCondition = "";

                String authorFilter = authorFilterField.getText().trim();
                String costFilter = costFilterField.getText().trim();
                String countFilter = countFilterField.getText().trim();

                if (!authorFilter.isEmpty()) {
                    filterCondition += "bookAuthor LIKE '%" + authorFilter + "%'";
                }

                if (!costFilter.isEmpty()) {
                    if (!filterCondition.isEmpty()) {
                        filterCondition += " AND ";
                    }
                    filterCondition += "bookCost <= " + Integer.parseInt(costFilter);
                }

                if (!countFilter.isEmpty()) {
                    if (!filterCondition.isEmpty()) {
                        filterCondition += " AND ";
                    }
                    filterCondition += "bookCount <= " + Integer.parseInt(countFilter);
                }

                if (!filterCondition.isEmpty()) {
                    selectQuery += filterCondition;
                    PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    int imageIndex = 1;
                    loadMethod(resultSet, imageIndex);
                }

                if(authorFilter.isEmpty() && costFilter.isEmpty() && countFilter.isEmpty()){
                    openNewScene("/com/kursovaya/bookshop/views/CatalogUser.fxml");
                }

            } catch (SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

        });

        searchButton.setOnAction(event -> {
            String searchTitleText = searchTextField.getText().trim();
            if(!searchTitleText.equals("")){
                findBookByTitle(searchTitleText);
            }
            else {
                try {
                    openNewScene("/com/kursovaya/bookshop/views/CatalogUser.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findBookByTitle(String title){
        DataBaseHandler dataBaseHandler = new DataBaseHandler();

        Book book = new Book();
        book.setName(title);

        ResultSet resultSet = dataBaseHandler.getBookFromDB(book);
        boolean flag = false;

        try{
            if(resultSet.next()){
                flag = true;
                int bookId = resultSet.getInt("bookId");
                String bookName = resultSet.getString("bookName");
                String bookAuthor = resultSet.getString("bookAuthor");
                String bookDescription = resultSet.getString("bookDescription");
                String bookImageLocation = resultSet.getString("bookImageLocation");
                int bookCount = resultSet.getInt("bookCount");
                int bookCost = resultSet.getInt("bookCost");

                book.setId(bookId);
                book.setName(bookName);
                book.setAuthor(bookAuthor);
                book.setDescription(bookDescription);
                book.setImageLocation(bookImageLocation);
                book.setCount(bookCount);
                book.setCost(bookCost);

                System.out.println("Найденная книга из запроса: " + book.toString());

                Image image = new Image("file:" + book.getImageLocation());
                searchBookImage.setImage(image);

                // Разбиваем описание книги на строки по 25 символов для удобного отображения
                StringBuilder formattedText = new StringBuilder();
                int charCount = 0;
                StringBuilder word = new StringBuilder();
                for (char c : book.getDescription().toCharArray()) {
                    if (Character.isWhitespace(c)) {
                        formattedText.append(word).append(c);
                        charCount += word.length() + 1;
                        word.setLength(0);
                    } else {
                        word.append(c);
                    }
                    if (charCount >= 25) {
                        formattedText.append("\n");
                        charCount = 0;
                    }
                }
                formattedText.append(word); // Добавляем оставшееся слово или пробел, если последним был пробел
                descriptionLabel.setText(formattedText.toString());

                Button button = new Button("В корзину");
                button.setStyle("-fx-background-color: #008000; -fx-text-fill: #FFFFFF;");
                button.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
                Label label = new Label("Цена: " + book.getCost());
                label.setStyle("-fx-background-color: #34C924; -fx-text-fill: #FFFFFF;");

                button.setOnAction(event -> {
                    User loginedUser = HomeUserController.getLoginedUserToCatalogAndToBasket();
                    User registeredUser = HomeUserController.getRegisteredUserToCatalogAndToBasket();
                    User currentUser = new User();
                    if(loginedUser != null){
                        currentUser = loginedUser;
                    }
                    if(registeredUser != null){
                        currentUser = registeredUser;
                    }
                    if(currentUser != null){
                        DataBaseHandler dataBaseHandler2 = new DataBaseHandler();
                        String insertQuery = "INSERT INTO UserBooks (userId, bookId) VALUES(?,?)";
                        try {
                            PreparedStatement preparedStatement = dataBaseHandler2.getConnection().prepareStatement(insertQuery);
                            preparedStatement.setInt(1, currentUser.getId());
                            preparedStatement.setInt(2, book.getId());
                            preparedStatement.executeUpdate();

                            String updateQuery = "UPDATE Books SET bookCount = ? WHERE bookId = ?";
                            PreparedStatement updateStatement = dataBaseHandler2.getConnection().prepareStatement(updateQuery);
                            updateStatement.setInt(1, book.getCount()-1);
                            updateStatement.setInt(2, book.getId());
                            updateStatement.executeUpdate();

                        } catch (SQLException | ClassNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                    try {
                        openNewScene("/com/kursovaya/bookshop/views/CatalogUser.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                VBox buttonContainer = new VBox(button);
                buttonContainer.setAlignment(Pos.BOTTOM_CENTER);
                VBoxForSearchedImage.getChildren().add(buttonContainer);
            }else {
                System.out.println("Книга не найдена");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(flag){System.out.println("Книга найдена:" + book.toString());}
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