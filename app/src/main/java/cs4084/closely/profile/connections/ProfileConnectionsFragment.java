package cs4084.closely.profile.connections;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs4084.closely.R;
import cs4084.closely.connection.Connection;


public class ProfileConnectionsFragment extends Fragment implements ProfileConnectionsRecyclerViewAdapter.OnConnectionListener {
    private ProfileConnectionsRecyclerViewAdapter profileConnectionsRecyclerViewAdapter;
    private RecyclerView connectionsRecyclerView;
    private List<Connection> connectionList;
    private static final String TAG = "ProfileConnectionsFragm";

    public ProfileConnectionsFragment() {
        // Required empty public constructor
    }

    public ProfileConnectionsFragment(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment#
        Log.d(TAG, "onCreateView: Profile connections ");
        View view = inflater.inflate(R.layout.fragment_profile_connections, container, false);
        profileConnectionsRecyclerViewAdapter = new ProfileConnectionsRecyclerViewAdapter(connectionList, this);

        connectionsRecyclerView = (RecyclerView) view.findViewById(R.id.connectionsRecyclerView);
        connectionsRecyclerView.setAdapter(profileConnectionsRecyclerViewAdapter);
        connectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    public void notifyDataSetChanged() {
        if (profileConnectionsRecyclerViewAdapter != null) {
            profileConnectionsRecyclerViewAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onConnectionClick(int position) {
        Connection connection = connectionList.get(position);
        Bundle connectionBundle = new Bundle();
        connectionBundle.putParcelable("connection", connection);

        Navigation.findNavController(getView()).navigate(
                R.id.action_profileFragment_self,
                connectionBundle
        );
    }
}
