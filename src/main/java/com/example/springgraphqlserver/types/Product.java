package com.example.springgraphqlserver.types;

public class Product {
    private int id;
    private String content;

    public Product (int id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Product { id: " + id + ", content: " + content + " }";
    }
}
