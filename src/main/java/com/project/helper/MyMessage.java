package com.project.helper;

public class MyMessage {

    private String content;
    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MyMessage(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public MyMessage() {

    }

}
