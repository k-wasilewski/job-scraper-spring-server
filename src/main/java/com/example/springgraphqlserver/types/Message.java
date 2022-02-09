package com.example.springgraphqlserver.types;

public class Message {
    private String content;

    public Message withContent(String content) {
        this.content = content;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
