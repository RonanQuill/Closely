package cs4084.closely.user;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userID;
    private boolean isProfileCreated;
    private String username;

    private String bio;
    private String documentID;
    private String profileURI;
    private List<String> connections;

    public User(String userID, String documentID) {
        this.userID = userID;
        this.documentID = documentID;
        connections = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public String getProfileURI() {
        return profileURI;
    }

    public void setProfileURI(String profileURI) {
        this.profileURI = profileURI;
    }

    public boolean getIsProfileCreated() {
        return isProfileCreated;
    }

    public void setIsProfileCreated(boolean isCreated) {
        isProfileCreated = isCreated;
    }

    public User(){}
}
