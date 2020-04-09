package cs4084.closely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cs4084.closely.user.User;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private User user;

    private EditText editBioView;
    private EditText editUsernameView;
    private Button applyButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        editBioView = v.findViewById(R.id.edit_profile_bio);
        editUsernameView = v.findViewById(R.id.edit_profile_username);
        applyButton = v.findViewById(R.id.edit_profile_apply_btn);
        applyButton.setOnClickListener(this);

        loadUser();
        return v;
    }

    private void loadUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").whereEqualTo("userID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        user = document.toObject(User.class);
                        user.setDocumentID(document.getId());
                    }
                    editBioView.setText(user.getBio());
                    editUsernameView.setText(user.getUsername());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_profile_apply_btn) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (!editBioView.getText().toString().equals(user.getBio())) {
                updateBio(db);
            }

            if (!editUsernameView.getText().toString().equals(user.getUsername())) {
                updateUsername(db);
            }
        }
    }

    private void updateBio(FirebaseFirestore db) {
        DocumentReference userReference = db.collection("users").document(user.getDocumentID());
        userReference.update("bio", editBioView.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Bio updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUsername(FirebaseFirestore db) {
        DocumentReference userReference = db.collection("users").document(user.getDocumentID());
        userReference.update("username", editUsernameView.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Username updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
