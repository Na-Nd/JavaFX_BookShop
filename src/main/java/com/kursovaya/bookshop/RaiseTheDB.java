// Класс для поднятия БД
package com.kursovaya.bookshop;

import java.sql.*;

public class RaiseTheDB {
    static final String url = "jdbc:mysql://localhost:3306/bookshop";
    static final String dbUser = "root";
    static final String password = "12345";
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection(url, dbUser, password)){
            createTablesIfNotExists(connection);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void createTablesIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Проверка существования таблицы пользователей
            ResultSet resultSetUsrs = connection.getMetaData().getTables(null, null, "Usrs", null);

            // Если таблицы не существует
            if (!resultSetUsrs.next()) {
                // То создаём её
                String createUsrsTableQuery = "CREATE TABLE Usrs (" +
                        "userId BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "userName VARCHAR(255) NOT NULL," +
                        "userSurname VARCHAR(255) NOT NULL," +
                        "userLogin VARCHAR(255) NOT NULL," +
                        "userPassword VARCHAR(255) NOT NULL)";
                statement.executeUpdate(createUsrsTableQuery);
                System.out.println("Таблица Usrs успешно создана");
            }

            // Проверка существования таблицы администраторов
            ResultSet resultSetAdmins = connection.getMetaData().getTables(null, null, "Admins", null);

            // Если таблицы не существует
            if (!resultSetAdmins.next()) {
                // То создаём её
                String createAdminsTableQuery = "CREATE TABLE Admins (" +
                        "adminId BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "adminName VARCHAR(255) NOT NULL," +
                        "adminSurname VARCHAR(255) NOT NULL," +
                        "adminLogin VARCHAR(255) NOT NULL," +
                        "adminPassword VARCHAR(255) NOT NULL)";
                statement.executeUpdate(createAdminsTableQuery);
                System.out.println("Таблица Admins успешно создана");
            }

            // Проверка существования таблицы продавцов
            ResultSet resultSetSellers = connection.getMetaData().getTables(null, null, "Sellers", null);

            // Если таблицы не существует
            if (!resultSetSellers.next()) {
                // То создаём её
                String createSellersTableQuery = "CREATE TABLE Sellers (" +
                        "sellerId BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "sellerName VARCHAR(255) NOT NULL," +
                        "sellerSurname VARCHAR(255) NOT NULL," +
                        "sellerLogin VARCHAR(255) NOT NULL," +
                        "sellerPassword VARCHAR(255) NOT NULL)";
                statement.executeUpdate(createSellersTableQuery);
                System.out.println("Таблица Sellers успешно создана");
            }

            // Проверка существования таблицы книг
            ResultSet resultSetBooks = connection.getMetaData().getTables(null, null, "Books", null);

            // Если таблицы не существует
            if (!resultSetBooks.next()) {
                // То создаём её
                String createBooksTableQuery = "CREATE TABLE Books (" +
                        "bookId BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "bookName VARCHAR(255) NOT NULL," +
                        "bookAuthor VARCHAR(255) NOT NULL," +
                        "bookDescription TEXT," +
                        "bookImageLocation TEXT," +
                        "bookCount INT NOT NULL," +
                        "bookCost INT NOT NULL)";
                statement.executeUpdate(createBooksTableQuery);
                System.out.println("Таблица Books успешно создана");
            }

            // Проверка существования таблицы книг для пользователей
            ResultSet resultSetUserBooks = connection.getMetaData().getTables(null, null, "UserBooks", null);

            // Если таблицы не существует

            if (!resultSetUserBooks.next()) {
                // То создаём её
                String createUserBooksTableQuery = "CREATE TABLE UserBooks (" +
                        "basketId BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "userId BIGINT NOT NULL," +
                        "bookId BIGINT NOT NULL," +
                        "FOREIGN KEY (userId) REFERENCES Usrs(userId)," +
                        "FOREIGN KEY (bookId) REFERENCES Books(bookId)" +
                        ")";
                statement.executeUpdate(createUserBooksTableQuery);
                System.out.println("Таблица UserBooks успешно создана");
            }

        }
    }

}




