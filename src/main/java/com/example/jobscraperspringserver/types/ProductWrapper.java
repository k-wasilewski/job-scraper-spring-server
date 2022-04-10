package com.example.jobscraperspringserver.types;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class ProductWrapper {
    @Id
    private Long id;
    private String content;
    private String operation;

    public ProductWrapper() {}

    public ProductWrapper(Product product, ProductOperation operation) {
        this.id = (long) product.getId();
        this.content = product.getContent();
        this.operation = operation.toString();
    }

    @javax.persistence.Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
