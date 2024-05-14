package com.kursovaya.bookshop;

import com.kursovaya.bookshop.Persons.Admin;
import com.kursovaya.bookshop.Persons.Book;
import com.kursovaya.bookshop.Persons.Seller;
import com.kursovaya.bookshop.Persons.User;

import java.sql.*;

public class DataBaseHandler{
    Connection connection;
    public Connection getConnection() throws ClassNotFoundException, SQLException{
        String urlConnection = "jdbc:mysql://localhost:3306/bookshop";
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(urlConnection, "root", "12345");
        return connection;
    }

    // Регистрация пользователя
    public void registerUserInDB(User user){
        String insertQuery = "INSERT INTO Usrs (userName, userSurname, userLogin, userPassword) VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    // Регистрация продавца
    public void registerSellerInDB(Seller seller){
        String insertQuery = "INSERT INTO Sellers (sellerName, sellerSurname, sellerLogin, sellerPassword) VALUES(?,?,?,?)";
        try{
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getSurname());
            preparedStatement.setString(3, seller.getLogin());
            preparedStatement.setString(4, seller.getPassword());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    // Регистрация администратора
    public void registerAdminInDB(Admin admin){
        String insertQuery = "INSERT INTO Admins (adminName, adminSurname, adminLogin, adminPassword) VALUES(?,?,?,?)";
        try{
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, admin.getName());
            preparedStatement.setString(2, admin.getSurname());
            preparedStatement.setString(3, admin.getLogin());
            preparedStatement.setString(4, admin.getPassword());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    // Получения пользователя из БД
    public ResultSet getUserFromDB(User user){
        ResultSet resultSet = null;
        String selectQuery = "SELECT * FROM Usrs WHERE userLogin =? AND userPassword =?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(selectQuery);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    // Получение продавца из БД
    public ResultSet getSellerFromDB(Seller seller){
        ResultSet resultSet = null;
        String selectQuery = "SELECT * FROM Sellers WHERE sellerLogin =? AND sellerPassword =?";
        try{
            PreparedStatement preparedStatement = getConnection().prepareStatement(selectQuery);
            preparedStatement.setString(1, seller.getLogin());
            preparedStatement.setString(2, seller.getPassword());
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    // Получение админа из бд
    public ResultSet getAdminFromDB(Admin admin){
        ResultSet resultSet = null;
        String selectQuerry = "SELECT * FROM Admins WHERE adminLogin =? AND adminPassword =?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(selectQuerry);
            preparedStatement.setString(1, admin.getLogin());
            preparedStatement.setString(2, admin.getPassword());
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    // Получение книги по названию
    public ResultSet getBookFromDB(Book book){
        ResultSet resultSet = null;
        String selectQuery = "SELECT * FROM Books WHERE bookName =?";
        try{
            PreparedStatement preparedStatement = getConnection().prepareStatement(selectQuery);
            preparedStatement.setString(1, book.getName());
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public boolean checkIfLoginExists(String login) {
        boolean loginExists = false;
        try {
            Connection connection = getConnection();
            String query = "SELECT * FROM Usrs WHERE userLogin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                loginExists = true;
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loginExists;
    }

    public boolean checkAdminIfLoginExists(String login) {
        boolean loginExists = false;
        try {
            Connection connection = getConnection();
            String query = "SELECT * FROM Admins WHERE adminLogin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                loginExists = true;
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loginExists;
    }

    public boolean checkSellerIfLoginExists(String login) {
        boolean loginExists = false;
        try {
            Connection connection = getConnection();
            String query = "SELECT * FROM Sellers WHERE sellerLogin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                loginExists = true;
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loginExists;
    }


}
