package cs4084.closely.connection;

import java.util.Map;

public class Connection {
    private String username;
    private String bio;

    public Connection(Map<String, Object> data) {
        this.username = data.get("username").toString();
        this.bio = data.get("bio").toString();
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }
}
