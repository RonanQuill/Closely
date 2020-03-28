package cs4084.closely.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cs4084.closely.R;
import cs4084.closely.connection.Connection;
import cs4084.closely.post.Post;
import cs4084.closely.user.User;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView usernameTextView;
    private TextView bioTextView;
    private TextView numberOfPostsTextView;
    private TextView memberSinceTextView;

    private User user;
    private List<Post> posts = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();

    private ProfilePosts profilePostsFragment;
    private ProfileConnections profileConnectionsFragment;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        bioTextView = view.findViewById(R.id.bioTextView);
        numberOfPostsTextView = view.findViewById(R.id.numberOfPostsTextView);
        memberSinceTextView = view.findViewById(R.id.memberSinceTextView);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        profilePostsFragment = new ProfilePosts(posts);
        profileConnectionsFragment = new ProfileConnections(connections);

        ProfileViewPagerAdapter profileViewPagerAdapter = new ProfileViewPagerAdapter(getFragmentManager());
        profileViewPagerAdapter.addFragment(profilePostsFragment, "Posts");
        profileViewPagerAdapter.addFragment(profileConnectionsFragment, "Connections");

        viewPager.setAdapter(profileViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        loadAndDisplayUserProfile("rjgjQZNEHJuAGbY6tUPM");
    }

    private void loadAndDisplayUserProfile(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = new User(document.getData());
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
                        posts.add(new Post(documentSnapshot.getData()));
                    }

                    displayPostsForUser();
                }
            }
        });
    }

    private void getConnectionsForUser() {
        List<String> userConnections = user.getConnections();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").whereIn("userID", userConnections).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for(QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        connections.add(new Connection(documentSnapshot.getData()));
                    }

                    profileConnectionsFragment.notifyDataSetChanged();
                    //displayConnectionsForUser();
                }
            }
        });

    }

    private void displayProfileForUser() {
        usernameTextView.setText(user.getUsername());
        bioTextView.setText(user.getBio());

        getPostsForUser();
        getConnectionsForUser();
    }

    private void displayPostsForUser() {
        numberOfPostsTextView.setText("Number of Posts: " + posts.size());
        profilePostsFragment.notifyDataSetChanged();
    }
}
