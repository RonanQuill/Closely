package cs4084.closely.blog;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Blog {
    private String title;
    private String subtitle;
    private String body;
    private String author;
    private String userID;
    private Timestamp datePosted;

    public Blog(Map<String, Object> data) {
        this.author = data.get("author").toString();
        this.title = data.get("title").toString();
        this.subtitle = data.get("subtitle").toString();
        this.body = data.get("body").toString();
        this.userID = data.get("userID").toString();
        this.datePosted = (Timestamp)data.get("datePosted");
    }

    public Blog(String title, String subtitle, String body, String author, String userID) {
        this.title = title;
        this.subtitle = subtitle;
        this.body = body;
        this.author = author;
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }

    public String getUserID() {
        return userID;
    }

    public Timestamp getDatePosted() {
        return datePosted;
    }

    public String toString() {
        return title + ", " + author + "\n";
    }

}
