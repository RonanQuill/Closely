package cs4084.closely.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cs4084.closely.R;
import cs4084.closely.user.User;


public class WelcomeFragment extends Fragment {

    private Button signUpButton;
    private Button signInButton;
    private LinearLayout splashScreen;
    private TextView welcomeText;
    private ImageView welcomeImage;
    private boolean userLoggedIn = true;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            checkForLoggedInUser();
        } else {
            userLoggedIn = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        splashScreen = view.findViewById(R.id.welcome_splash);
        welcomeImage = view.findViewById(R.id.welcome_image);
        welcomeText = view.findViewById(R.id.welcomeText);
        signInButton = view.findViewById(R.id.signInButton);
        signUpButton = view.findViewById(R.id.signUpButton);
        if (!userLoggedIn) {
            unHideWelcome();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_welcomeFragment_to_signInFragment);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_welcomeFragment_to_signUpFragment);
            }
        });
    }

    private void checkForLoggedInUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").whereEqualTo("userID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        navigateAway(user);
                    }
                }
            }
        });
    }

    private void navigateAway(User user) {
        if (user != null) {
            if (!user.getIsProfileCreated()) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IsNewUser", true);
                Navigation.findNavController(getView()).navigate(
                        R.id.action_welcomeFragment_to_editProfileFragment2, bundle);
            } else {
                NavHostFragment.findNavController(this).navigate(R.id.action_welcomeFragment_to_navigationFragment);
            }
        }
    }

    private void unHideWelcome() {
        splashScreen.setVisibility(View.GONE);
        welcomeText.setVisibility(View.VISIBLE);
        welcomeImage.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);
    }
}
