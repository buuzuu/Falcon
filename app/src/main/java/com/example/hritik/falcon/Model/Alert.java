package com.example.hritik.falcon.Model;

public class Alert {

    private String imageUrl;
    private String userId;
    private String lati;
    private String longi;
    private String caption;

    public Alert() {
    }

    public Alert(String imageUrl, String userId, String lati, String longi, String caption) {
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.lati = lati;
        this.longi = longi;
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
