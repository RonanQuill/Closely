package cs4084.closely.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cs4084.closely.R;


public class NavigationFragment extends Fragment {

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
//
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.nagivationDisplay,  new BlogListFragment()).commit();
//
//        bottomNavigationView = view.findViewById(R.id.bottomNavigationBar);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                Fragment selectedFragment = null;
//
//                switch(menuItem.getItemId()) {
//                    case R.id.all_blogs:
//                        selectedFragment = new BlogListFragment();
//                        break;
//
//                    case R.id.add_connection:
//                        selectedFragment = new HomeFragment();
//                        break;
//
//                    case R.id.profile:
//                        selectedFragment = new ProfileFragment();
//                        break;
//                }
//
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nagivationDisplay, selectedFragment).commit();
//
//                return true;
//            }
//        });
    }


}
