package com.kursovaya.bookshop.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.kursovaya.bookshop.DataBaseHandler;
import com.kursovaya.bookshop.Persons.Admin;
import com.kursovaya.bookshop.Persons.User;
import com.kursovaya.bookshop.Persons.Person;
import com.kursovaya.bookshop.Persons.Seller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TablesAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button addPersonButton;

    @FXML
    private TextField addPersonLoginTextField;

    @FXML
    private TextField addPersonNameTextField;

    @FXML
    private TextField addPersonPasswordTextField;

    @FXML
    private TextField addPersonSurnameTextField;

    @FXML
    private Button deletePersonButton;

    @FXML
    private TextField deletePersonIdTextField;

    @FXML
    private URL location;

    @FXML
    private CheckBox AdminsCheckBox;

    @FXML
    private Button applyTableButton;

    @FXML
    private Button backToHomeButton;

    @FXML
    private TableView<Person> firstTableView;

    @FXML
    private Label helloLabel;

    @FXML
    private CheckBox sellersCheckBox;

    @FXML
    private CheckBox usersCheckBox;

    @FXML
    private Label infoLabel;

    @FXML
    void initialize() {
        applyTableButton.setOnAction(event -> {
            if (AdminsCheckBox.isSelected()) {
                showAdminsTable();
            } else if (sellersCheckBox.isSelected()) {
                showSellersTable();
            } else if (usersCheckBox.isSelected()) {
                showUsersTable();
            }
        });

        addPersonButton.setOnAction(event -> {
            String name = addPersonNameTextField.getText();
            String surname = addPersonSurnameTextField.getText();
            String login = addPersonLoginTextField.getText();
            String password = addPersonPasswordTextField.getText();

            if (AdminsCheckBox.isSelected()) {
                addAdminToTable(name, surname, login, password);
            } else if (sellersCheckBox.isSelected()) {
                addSellerToTable(name, surname, login, password);
            } else if (usersCheckBox.isSelected()) {
                addUserToTable(name, surname, login, password);
            }
        });

        deletePersonButton.setOnAction(event -> {
            String idText = deletePersonIdTextField.getText();
            if (!isValidId(idText)) {
                infoLabel.setText("Вы ввели не ID!");
                return; // Выйти из метода, если ID не является числом
            }
            int id = Integer.parseInt(idText);

            if (AdminsCheckBox.isSelected()) {
                deleteAdminFromTable(id);
            } else if (sellersCheckBox.isSelected()) {
                deleteSellerFromTable(id);
            } else if (usersCheckBox.isSelected()) {
                deleteUserFromTable(id);
            }
        });


        backToHomeButton.setOnAction(event -> {
            try {
                openNewScene("/com/kursovaya/bookshop/views/HomeAdmin.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isValidId(String idText) {
        try {
            Integer.parseInt(idText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAdminsTable() {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для выборки данных из таблицы Admins
            String selectQuery = "SELECT * FROM Admins";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);

            // Получаем результаты запроса
            ResultSet resultSet = preparedStatement.executeQuery();

            // Очищаем таблицу перед добавлением новых данных
            firstTableView.getItems().clear();
            firstTableView.getColumns().clear();

            // Создаем столбцы таблицы
            TableColumn<?, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<?, String> nameColumn = new TableColumn<>("Имя");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<?, String> surnameColumn = new TableColumn<>("Фамилия");
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

            TableColumn<?, String> loginColumn = new TableColumn<>("Логин");
            loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

            TableColumn<?, String> passwordColumn = new TableColumn<>("Пароль");
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            // Добавляем столбцы к таблице
            firstTableView.getColumns().addAll((TableColumn<Person, ?>) idColumn, (TableColumn<Person, ?>) nameColumn, (TableColumn<Person, ?>) surnameColumn, (TableColumn<Person, ?>) loginColumn, (TableColumn<Person, ?>) passwordColumn);

            // Добавляем данные в таблицу
            while (resultSet.next()) {
                int adminId = resultSet.getInt("adminId");
                String adminName = resultSet.getString("adminName");
                String adminSurname = resultSet.getString("adminSurname");
                String adminLogin = resultSet.getString("adminLogin");
                String adminPassword = resultSet.getString("adminPassword");

                Admin admin = new Admin(adminId, adminName, adminSurname, adminPassword, adminLogin);
                firstTableView.getItems().add(admin);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showSellersTable() {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для выборки данных из таблицы Sellers
            String selectQuery = "SELECT * FROM Sellers";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);

            // Получаем результаты запроса
            ResultSet resultSet = preparedStatement.executeQuery();

            // Очищаем таблицу перед добавлением новых данных
            firstTableView.getItems().clear();
            firstTableView.getColumns().clear();

            // Создаем столбцы таблицы
            TableColumn<?, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<?, String> nameColumn = new TableColumn<>("Имя");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<?, String> surnameColumn = new TableColumn<>("Фамилия");
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

            TableColumn<?, String> loginColumn = new TableColumn<>("Логин");
            loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

            TableColumn<?, String> passwordColumn = new TableColumn<>("Пароль");
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            // Добавляем столбцы к таблице
            firstTableView.getColumns().addAll((TableColumn<Person, ?>) idColumn, (TableColumn<Person, ?>) nameColumn, (TableColumn<Person, ?>) surnameColumn, (TableColumn<Person, ?>) loginColumn, (TableColumn<Person, ?>) passwordColumn);

            // Добавляем данные в таблицу
            while (resultSet.next()) {
                int sellerId = resultSet.getInt("sellerId");
                String sellerName = resultSet.getString("sellerName");
                String sellerSurname = resultSet.getString("sellerSurname");
                String sellerLogin = resultSet.getString("sellerLogin");
                String sellerPassword = resultSet.getString("sellerPassword");

                Seller seller = new Seller(sellerId, sellerName, sellerSurname, sellerPassword, sellerLogin);
                firstTableView.getItems().add(seller);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showUsersTable() {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для выборки данных из таблицы Usrs
            String selectQuery = "SELECT * FROM Usrs";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(selectQuery);

            // Получаем результаты запроса
            ResultSet resultSet = preparedStatement.executeQuery();

            // Очищаем таблицу перед добавлением новых данных
            firstTableView.getItems().clear();
            firstTableView.getColumns().clear();

            // Создаем столбцы таблицы
            TableColumn<?, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<?, String> nameColumn = new TableColumn<>("Имя");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<?, String> surnameColumn = new TableColumn<>("Фамилия");
            surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

            TableColumn<?, String> loginColumn = new TableColumn<>("Логин");
            loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

            TableColumn<?, String> passwordColumn = new TableColumn<>("Пароль");
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            // Добавляем столбцы к таблице
            firstTableView.getColumns().addAll((TableColumn<Person, ?>) idColumn, (TableColumn<Person, ?>) nameColumn, (TableColumn<Person, ?>) surnameColumn, (TableColumn<Person, ?>) loginColumn, (TableColumn<Person, ?>) passwordColumn);

            // Добавляем данные в таблицу
            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String userName = resultSet.getString("userName");
                String userSurname = resultSet.getString("userSurname");
                String userLogin = resultSet.getString("userLogin");
                String userPassword = resultSet.getString("userPassword");

                User user = new User(userId, userName, userSurname, userPassword, userLogin);
                firstTableView.getItems().add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addAdminToTable(String name, String surname, String login, String password) {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для добавления данных в таблицу Admins
            String insertQuery = "INSERT INTO Admins (adminName, adminSurname, adminLogin, adminPassword) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, password);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            openNewScene("/com/kursovaya/bookshop/views/TablesAdmin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSellerToTable(String name, String surname, String login, String password) {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для добавления данных в таблицу Sellers
            String insertQuery = "INSERT INTO Sellers (sellerName, sellerSurname, sellerLogin, sellerPassword) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, password);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            openNewScene("/com/kursovaya/bookshop/views/TablesAdmin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addUserToTable(String name, String surname, String login, String password) {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для добавления данных в таблицу Usrs
            String insertQuery = "INSERT INTO Usrs (userName, userSurname, userLogin, userPassword) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, password);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            openNewScene("/com/kursovaya/bookshop/views/TablesAdmin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAdminFromTable(int id) {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для удаления записи из таблицы Admins
            String deleteQuery = "DELETE FROM Admins WHERE adminId = ?";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            openNewScene("/com/kursovaya/bookshop/views/TablesAdmin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSellerFromTable(int id) {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для удаления записи из таблицы Sellers
            String deleteQuery = "DELETE FROM Sellers WHERE sellerId = ?";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            openNewScene("/com/kursovaya/bookshop/views/TablesAdmin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserFromTable(int id) {
        try {
            // Подключаемся к базе данных
            DataBaseHandler dataBaseHandler = new DataBaseHandler();

            // Создаем запрос SQL для удаления записи из таблицы Usrs
            String deleteQuery = "DELETE FROM Usrs WHERE userId = ?";
            PreparedStatement preparedStatement = dataBaseHandler.getConnection().prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            openNewScene("/com/kursovaya/bookshop/views/TablesAdmin.fxml");
        } catch (IOException e) {
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
