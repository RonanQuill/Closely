package cs4084.closely.profile.connections;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs4084.closely.R;
import cs4084.closely.connection.Connection;

public class ProfileConnectionsRecyclerViewAdapter extends RecyclerView.Adapter<ProfileConnectionsRecyclerViewAdapter.ViewHolder>{

    private List<Connection> connections;

    public ProfileConnectionsRecyclerViewAdapter(List<Connection> connections) {
        this.connections = connections;
    }

    @NonNull
    @Override
    public ProfileConnectionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_connections_recycler_view_item, parent, false);
        ProfileConnectionsRecyclerViewAdapter.ViewHolder viewHolder = new ProfileConnectionsRecyclerViewAdapter.ViewHolder(view);

        Log.i("Recycler", "Hi");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileConnectionsRecyclerViewAdapter.ViewHolder holder, final int position) {
        Connection post = connections.get(position);
        holder.name.setText(post.getUsername());
        holder.bio.setText(post.getBio());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Recycler", connections.get(position).getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView bio;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            bio = itemView.findViewById(R.id.bioTextView);
            layout = itemView.findViewById(R.id.connectionsLayout);
        }
    }
}
