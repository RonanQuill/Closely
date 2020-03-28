package cs4084.closely.post;

import java.util.Map;

public class Post {
    private String author;
    private String title;
    private String subtitle;
    private String body;
    private String userID;

    public Post(Map<String, Object> data) {
        this.author = data.get("author").toString();
        this.title = data.get("title").toString();
        this.subtitle = data.get("subtitle").toString();
        this.body = data.get("body").toString();
        this.userID = data.get("userID").toString();
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
