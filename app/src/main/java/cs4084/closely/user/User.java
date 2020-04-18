package cs4084.closely.user;

import java.util.ArrayList;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public interface OnLoaded
    {
        public void OnLoaded(User user);
    }

    private String documentID;
    private String userID = "";
    private String username = "";
    private String bio = "";
    private List<String> connections = new ArrayList<>();

    private boolean isProfileCreated;
    private String profileURI;

    public User(){}

    public User(String userID, String documentID) {
        this.userID = userID;
        this.documentID = documentID;
        connections = new ArrayList<>();
    }

    public static void loadUser(String userID, final User.OnLoaded onLoaded) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").whereEqualTo("userID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        user.setDocumentID(document.getId());
                        onLoaded.OnLoaded(user);
                    }
                }
            }
        });
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

    public void connectToUser(User otherUser) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        this.connections.add(otherUser.getUserID());
        db.collection("users").document(this.getDocumentID()).update(this.toMap());

        otherUser.connections.add(this.getUserID());
        db.collection("users").document(otherUser.getDocumentID()).update(otherUser.toMap());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        map.put("username", username);
        map.put("bio", bio);
        map.put("connections", connections);
        return map;
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
}
