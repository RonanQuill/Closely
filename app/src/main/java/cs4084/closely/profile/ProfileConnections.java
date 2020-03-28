package cs4084.closely.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cs4084.closely.R;
import cs4084.closely.connection.Connection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileConnections#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileConnections extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProfileConnectionsRecyclerViewAdapter profileConnectionsRecyclerViewAdapter;
    private RecyclerView connectionsRecyclerView;
    private List<Connection> connectionList;

    public ProfileConnections() {
        // Required empty public constructor
    }

    public ProfileConnections(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileConnections.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileConnections newInstance(String param1, String param2) {
        ProfileConnections fragment = new ProfileConnections();
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
        return inflater.inflate(R.layout.fragment_profile_connections, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileConnectionsRecyclerViewAdapter = new ProfileConnectionsRecyclerViewAdapter(connectionList);

        connectionsRecyclerView = (RecyclerView) view.findViewById(R.id.connectionsRecyclerView);
        connectionsRecyclerView.setAdapter(profileConnectionsRecyclerViewAdapter);
        connectionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void notifyDataSetChanged() {
        profileConnectionsRecyclerViewAdapter.notifyDataSetChanged();
    }
}
