package cs4084.closely.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import cs4084.closely.R;
import cs4084.closely.user.User;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private User user;

    private EditText editBioView;
    private EditText editUsernameView;
    private Button applyButton;
    private ImageView profileImageView;

    private boolean isNewUser = false;
    private Uri newProfileImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isNewUser = bundle.getBoolean("IsNewUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        editBioView = v.findViewById(R.id.edit_profile_bio);
        editUsernameView = v.findViewById(R.id.edit_profile_username);
        applyButton = v.findViewById(R.id.edit_profile_apply_btn);
        profileImageView = v.findViewById(R.id.edit_profile_image);
        profileImageView.setOnClickListener(this);

        TextView editBioTextView = v.findViewById(R.id.edit_profile_bio_text);
        TextView editUsernameTextView = v.findViewById(R.id.edit_profile_username_text);
        if (isNewUser) {
            editBioTextView.setText("Create Bio");
            editUsernameTextView.setText("Create username");
        } else {
            editUsernameView.setVisibility(View.GONE);
            editUsernameTextView.setVisibility(View.GONE);
        }
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
                        Log.d("yeet", "onComplete: User found");
                    }
                    editBioView.setText(user.getBio());
                    editUsernameView.setText(user.getUsername());
                    if (user.getProfileURI() != null && !user.getProfileURI().isEmpty()) {
                        Glide.with(getContext()).load(user.getProfileURI())
                                .into(profileImageView);
                    } else {
                        String imgRequest = "https://api.adorable.io/avatars/285/" + user.getUserID() + ".png";
                        Glide.with(getContext()).load(imgRequest)
                                .into(profileImageView);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_profile_apply_btn) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Bundle bundle = new Bundle();
            if (isNewUser) {
                if (isValidUsernameAndBio()) {
                    populateUserProfile();
                    if (newProfileImage != null) {
                        saveToFirestore(newProfileImage);
                    }
                }
            } else {
                if (!editBioView.getText().toString().equals(user.getBio())) {
                    updateBio(db);
                }

                if (!editUsernameView.getText().toString().equals(user.getUsername())) {
                    updateUsername(db);
                }

                if (newProfileImage != null) {
                    saveToFirestore(newProfileImage);
                    bundle.putString("new_img", newProfileImage.toString());
                }

                Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_profileFragment, bundle);
            }
        } else if (v.getId() == R.id.edit_profile_image) {
            Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
            cameraIntent.setType("image/*");
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 1000);
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

    private boolean isValidUsernameAndBio() {
        boolean isValidUser = true;
        String username = editUsernameView.getText().toString();
        String bio = editBioView.getText().toString();
        bio = bio.trim();
        username = username.trim();
        if (username.length() < 3) {
            isValidUser = false;
            Toast.makeText(getActivity(), "Username is too short", Toast.LENGTH_SHORT).show();
        }
        if (bio.length() < 5) {
            isValidUser = false;
            Toast.makeText(getActivity(), "Bio is too short", Toast.LENGTH_SHORT).show();

        }
        return isValidUser;
    }

    private void populateUserProfile() {
        user.setIsProfileCreated(true);
        user.setBio(editBioView.getText().toString());
        user.setUsername(editUsernameView.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(user.getDocumentID());
        userRef.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "User data updated", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView())
                        .navigate(R.id.action_editProfileFragment2_to_navigationFragment);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                newProfileImage = data.getData();
                profileImageView.setImageURI(newProfileImage);

            }
        }
    }

    private void saveToFirestore(Uri file) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference profileReference = storageRef.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        profileReference.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return profileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("yeet", "onComplete: " + task.getResult().toString());
                    //Save uri to user object
                    updateUserProfileImage(task.getResult().toString());
                }
            }
        });

    }

    private void updateUserProfileImage(String profileURI) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userReference = db.collection("users").document(user.getDocumentID());
        userReference.update("profileURI", profileURI).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Profile image updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}