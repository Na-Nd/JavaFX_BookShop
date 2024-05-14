package com.kursovaya.bookshop.Persons;

public class User extends Person{
    private int id;
    private String name;
    private String surname;
    private String password;
    private String login;

    public User(int id, String name, String surname, String password, String login) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.login = login;
    }

    public User(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.login = login;
    }

    public User(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
