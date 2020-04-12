package cs4084.closely.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import cs4084.closely.R;
import cs4084.closely.blog.Blog;
import cs4084.closely.connection.Connection;
import cs4084.closely.profile.connections.ProfileConnectionsFragment;
import cs4084.closely.profile.posts.ProfilePostsFragment;
import cs4084.closely.user.User;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private TextView usernameTextView;
    private TextView bioTextView;
    private TextView numberOfPostsTextView;
    private TextView memberSinceTextView;
    private ImageButton editProfileButton;
    private ImageView profileImageView;

    private String userID;
    private User user;
    private List<Blog> posts = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();

    private ProfilePostsFragment profilePostsFragment;
    private ProfileConnectionsFragment profileConnectionsFragment;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        Connection conn = null;
        if (bundle != null) {
            conn = bundle.getParcelable("connection");
        }

        if (conn != null) {
            userID = conn.getUserID();
        } else {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        bioTextView = view.findViewById(R.id.bioTextView);
        numberOfPostsTextView = view.findViewById(R.id.numberOfPostsTextView);
        memberSinceTextView = view.findViewById(R.id.memberSinceTextView);
        editProfileButton = view.findViewById(R.id.profile_edit_profile_btn);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);

        profilePostsFragment = new ProfilePostsFragment(posts);
        profileConnectionsFragment = new ProfileConnectionsFragment(connections);

        ProfileViewPagerAdapter profileViewPagerAdapter = new ProfileViewPagerAdapter(this);
        profileViewPagerAdapter.addFragment(profilePostsFragment);
        profileViewPagerAdapter.addFragment(profileConnectionsFragment);


        viewPager2.setAdapter(profileViewPagerAdapter);


        profileImageView = view.findViewById(R.id.profile_profile_img);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
                cameraIntent.setType("image/*");
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 1000);
                }
            }
        });


        loadAndDisplayUserProfile(userID);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] titles = new String[]{"Posts", "Connections"};
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_editProfileFragment);
            }
        });


    }

    private void loadAndDisplayUserProfile(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("userID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        user = document.toObject(User.class);
                        user.setDocumentID(document.getId());
                        displayProfileForUser();

                    }
                }
            }
        });
    }

    private void getPostsForUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("blogs").whereEqualTo("userID", user.getUserID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for(QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        posts.add(documentSnapshot.toObject(Blog.class));
                    }

                    displayPostsForUser();
                }
            }
        });
    }

    private void getConnectionsForUser() {
        List<String> userConnections = user.getConnections();

        if(userConnections != null && !userConnections.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").whereIn("userID", userConnections).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            connections.add(documentSnapshot.toObject(Connection.class));
                        }

                        profileConnectionsFragment.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private void displayProfileForUser() {
        usernameTextView.setText(user.getUsername());
        bioTextView.setText(user.getBio());

        getPostsForUser();
        getConnectionsForUser();
        user.setProfileURI("");
        if (user.getProfileURI().equals("")) {
            String imgRequest = "https://api.adorable.io/avatars/285/" + user.getUserID() + ".png";
            Glide.with(getContext()).load(imgRequest)
                    .into(profileImageView);
        } else {
            Glide.with(getContext()).load(user.getProfileURI())
                    .into(profileImageView);
        }

    }

    private void displayPostsForUser() {
        numberOfPostsTextView.setText("Number of Posts: " + posts.size());
        profilePostsFragment.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                profileImageView.setImageURI(returnUri);
                saveToFirestore(returnUri);
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
