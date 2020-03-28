package cs4084.closely;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

class Blog {
    private String title;
    private String subtitle;
    private String body;
    private String author;
    private String userID;
    public Blog() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @NonNull
    @Override
    public String toString() {
        return title + ", " + author + "\n";
    }
}
