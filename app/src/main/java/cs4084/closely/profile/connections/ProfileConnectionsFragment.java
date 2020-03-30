package cs4084.closely.profile.connections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs4084.closely.R;
import cs4084.closely.connection.Connection;
import cs4084.closely.profile.ProfileFragment;


public class ProfileConnectionsFragment extends Fragment implements ProfileConnectionsRecyclerViewAdapter.OnConnectionListener {
    private ProfileConnectionsRecyclerViewAdapter profileConnectionsRecyclerViewAdapter;
    private RecyclerView connectionsRecyclerView;
    private List<Connection> connectionList;

    public ProfileConnectionsFragment() {
        // Required empty public constructor
    }

    public ProfileConnectionsFragment(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_connections, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileConnectionsRecyclerViewAdapter = new ProfileConnectionsRecyclerViewAdapter(connectionList, this);

        connectionsRecyclerView = (RecyclerView) view.findViewById(R.id.connectionsRecyclerView);
        connectionsRecyclerView.setAdapter(profileConnectionsRecyclerViewAdapter);
        connectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void notifyDataSetChanged() {
        profileConnectionsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnectionClick(int position) {
        Connection connection = connectionList.get(position);
        Bundle connectionBundle = new Bundle();
        connectionBundle.putParcelable("connection", connection);
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(connectionBundle);
        t.replace(R.id.nagivationDisplay, profileFragment).commit();
    }
}
