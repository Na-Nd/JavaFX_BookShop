    package com.kursovaya.bookshop.Controllers;

    import java.io.IOException;
    import java.net.URL;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ResourceBundle;

    import com.kursovaya.bookshop.DataBaseHandler;
    import com.kursovaya.bookshop.Persons.Book;
    import com.kursovaya.bookshop.Persons.Seller;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.stage.Stage;

    public class TablesSellerController {

        // Получаем пользователя
        private Seller loginedSeller = HomeSellerController.getLoginedSellerToGraphsAndTables();
        private Seller registeredSeller = HomeSellerController.getRegisteredSellerToGraprhsAndTables();

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Button addBookButton;

        @FXML
        private Button backToHomeButton;

        @FXML
        private TextField authorTextField;

        @FXML
        private Button changeCostButton;

        @FXML
        private TextField changeCostIdTextField;

        @FXML
        private TextField changeCostTextField;

        @FXML
        private TextField costTextField;

        @FXML
        private TextField countTextField;

        @FXML
        private Button deleteBookButton;

        @FXML
        private TextField deleteBookIdTextField;

        @FXML
        private TextField descriptionTextField;

        @FXML
        private Label infoLabel;

        @FXML
        private TextField imageLocationTextField;

        @FXML
        private TextField nameTextField;

        @FXML
        private Label helloLabel;

        @FXML
        private TableView<Book> booksTableView;

        @FXML
        private TableColumn<Book, Integer> idColumn;

        @FXML
        private TableColumn<Book, String> nameColumn;

        @FXML
        private TableColumn<Book, String> authorColumn;

        @FXML
        private TableColumn<Book, String> descriptionColumn;

        @FXML
        private TableColumn<Book, String> imageLocationColumn;

        @FXML
        private TableColumn<Book, Integer> countColumn;

        @FXML
        private TableColumn<Book, Integer> costColumn;

        @FXML
        void initialize() {
            // Настройка столбцов таблицы
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            imageLocationColumn.setCellValueFactory(new PropertyValueFactory<>("imageLocation"));
            countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

            // Загрузка данных в таблицу
            loadBooks();

            backToHomeButton.setOnAction(event -> {
                try {
                    openNewScene("/com/kursovaya/bookshop/views/HomeSeller.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            changeCostButton.setOnAction(event -> {
                try {
                    String idText = changeCostIdTextField.getText();
                    if (!isValidId(idText)) {
                        infoLabel.setText("Вы ввели неверный ID для изменения цены книги!");
                        return;
                    }
                    int bookId = Integer.parseInt(idText);
                    int newCost = Integer.parseInt(changeCostTextField.getText());

                    updateBookCost(bookId, newCost);

                    booksTableView.getItems().clear();
                    loadBooks();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            });

            deleteBookButton.setOnAction(event -> {
                try {
                    String idText = deleteBookIdTextField.getText();
                    if (!isValidId(idText)) {
                        infoLabel.setText("Вы ввели неверный ID для удаления книги!");
                        return;
                    }
                    int bookId = Integer.parseInt(idText);

                    deleteBook(bookId);

                    booksTableView.getItems().clear();
                    loadBooks();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            });

            addBookButton.setOnAction(event -> {
                try {
                    // Получаем значения из текстовых полей
                    String bookName = nameTextField.getText();
                    String bookAuthor = authorTextField.getText();
                    String bookDescription = descriptionTextField.getText();
                    String bookImageLocation = imageLocationTextField.getText();
                    int bookCount = Integer.parseInt(countTextField.getText());
                    int bookCost = Integer.parseInt(costTextField.getText());

                    // Добавляем новую книгу в базу данных
                    addBook(bookName, bookAuthor, bookDescription, bookImageLocation, bookCount, bookCost);

                    // Обновляем таблицу книг
                    booksTableView.getItems().clear();
                    loadBooks();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            });

        }

        private void loadBooks() {
            try {
                DataBaseHandler dataBaseHandler = new DataBaseHandler();
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement("SELECT * FROM Books");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int bookId = resultSet.getInt("bookId");
                    String bookName = resultSet.getString("bookName");
                    String bookAuthor = resultSet.getString("bookAuthor");
                    String bookDescription = resultSet.getString("bookDescription");
                    String bookImageLocation = resultSet.getString("bookImageLocation");
                    int bookCount = resultSet.getInt("bookCount");
                    int bookCost = resultSet.getInt("bookCost");

                    // Создание объекта Book и добавление его в TableView
                    Book book = new Book(bookId, bookName, bookAuthor, bookDescription, bookImageLocation, bookCount, bookCost);
                    booksTableView.getItems().add(book);
                }
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

        private void updateBookCost(int bookId, int newCost) {
            try {
                DataBaseHandler dataBaseHandler = new DataBaseHandler();
                String updateQuery = "UPDATE Books SET bookCost = ? WHERE bookId = ?";
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(updateQuery);
                preparedStatement.setInt(1, newCost);
                preparedStatement.setInt(2, bookId);
                preparedStatement.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private boolean isValidId(String idText) {
            try {
                Integer.parseInt(idText);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }


        private void deleteBook(int bookId) {
            try {
                DataBaseHandler dataBaseHandler = new DataBaseHandler();
                String deleteQuery = "DELETE FROM Books WHERE bookId = ?";
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
                preparedStatement.setInt(1, bookId);
                preparedStatement.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void addBook(String bookName, String bookAuthor, String bookDescription, String bookImageLocation, int bookCount, int bookCost) {
            try {
                DataBaseHandler dataBaseHandler = new DataBaseHandler();
                String insertQuery = "INSERT INTO Books (bookName, bookAuthor, bookDescription, bookImageLocation, bookCount, bookCost) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQuery);
                preparedStatement.setString(1, bookName);
                preparedStatement.setString(2, bookAuthor);
                preparedStatement.setString(3, bookDescription);
                preparedStatement.setString(4, bookImageLocation);
                preparedStatement.setInt(5, bookCount);
                preparedStatement.setInt(6, bookCost);
                preparedStatement.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
