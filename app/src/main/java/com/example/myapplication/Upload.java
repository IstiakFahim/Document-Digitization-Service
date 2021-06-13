package com.example.myapplication;

public class Upload {

    public String name;
    public String url;
    public String uid;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url, String uid) {
        this.name = name;
        this.url = url;
        this.uid = uid;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getUid() {
        return uid;
    }
}
