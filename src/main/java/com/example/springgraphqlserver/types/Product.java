package com.example.springgraphqlserver.types;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("products")
public class Product {
    @Id
    private int id;
    private String content;
    private ProductOperation operation;

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

    public ProductOperation getOperation() {
        return operation;
    }

    public void setOperation(ProductOperation operation) {
        this.operation = operation;
    }

    public Product withOperation(ProductOperation operation) {
        this.operation = operation;
        return this;
    }

    public String toString() {
        return "Product { id: " + id + ", content: " + content + " }";
    }
}
