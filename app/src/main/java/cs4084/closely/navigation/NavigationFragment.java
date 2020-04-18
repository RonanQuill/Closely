package cs4084.closely.navigation;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import cs4084.closely.Closely;
import cs4084.closely.R;
import cs4084.closely.user.User;


public class NavigationFragment extends Fragment {

    private ConstraintLayout progressBarLayout;
    private ConstraintLayout navigationLayout;

    private BottomNavigationView bottomNavigationView;

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        navigationLayout = view.findViewById(R.id.navigationLayout);
        navigationLayout.setVisibility(View.GONE);
        progressBarLayout = view.findViewById(R.id.progressBarLayout);
        progressBarLayout.setVisibility(View.VISIBLE);

        NavController navController = Navigation.findNavController(view.findViewById(R.id.inner_nav_host_fragment));
        bottomNavigationView = view.findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.homeFragment);
        bottomNavigationView.setItemIconTintList(null);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Closely closely = (Closely)getActivity();
        if(closely.getLoggedInUser() == null) {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                User.loadUser(userID, new User.OnLoaded() {
                    @Override
                    public void OnLoaded(User loadedUser) {
                        closely.setLoggedInUser(loadedUser);
                        progressBarLayout.setVisibility(View.GONE);
                        navigationLayout.setVisibility(View.VISIBLE);

                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        navigationLayout.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.VISIBLE);

        final Closely closely = (Closely)getActivity();
        if(closely.getLoggedInUser() == null) {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                User.loadUser(userID, new User.OnLoaded() {
                    @Override
                    public void OnLoaded(User loadedUser) {
                        closely.setLoggedInUser(loadedUser);
                        progressBarLayout.setVisibility(View.GONE);
                        navigationLayout.setVisibility(View.VISIBLE);

                        // Check to see that the Activity started due to an Android Beam
                        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(closely.getIntent().getAction())) {
                            closely.getNfcManager().receiveNfcMessage(closely.getIntent());
                        }

                    }
                });
            }
        }
    }


}
