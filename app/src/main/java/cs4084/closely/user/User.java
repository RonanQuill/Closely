package cs4084.closely.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    public interface OnLoaded
    {
        public void OnLoaded(User user);
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

    private String documentID;
    private String userID;
    private String username;
    private String bio;
    private List<String> connections;

    public User(){}

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getUserID() {
        return userID;
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

    public void addConnection(String userID) {
        connections.add(userID);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        map.put("username", username);
        map.put("bio", bio);
        map.put("connections", connections);
        return map;
    }
}
