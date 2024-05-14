package com.kursovaya.bookshop.Persons;

public class Book {
    private int id;

    private String name;
    private String author;
    private String description;
    private String imageLocation;
    private int count;
    private int cost;

    public Book(){}

    public Book(int id, String name, String author, String description, String imageLocation, int count, int cost) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.imageLocation = imageLocation;
        this.count = count;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description= есть какое-то '" + '\'' +
                ", imageLocation='" + imageLocation + '\'' +
                ", count=" + count +
                ", cost=" + cost +
                '}';
    }
}
