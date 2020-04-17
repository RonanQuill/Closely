package cs4084.closely.blog;

class Comment {
    String body;
    String author;

    public Comment (String author, String body) {
        this.author = author;
        this.body = body;
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
}
