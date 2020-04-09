package cs4084.closely.user;

import java.util.List;

public class User {

    private String userID;
    private String username;
    private String bio;
    private String documentID;
    private List<String> connections;

    public String getUserID() {
        return userID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getBio() {
        return bio;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getConnections() {
        return connections;
    }
    public User(){}
}
