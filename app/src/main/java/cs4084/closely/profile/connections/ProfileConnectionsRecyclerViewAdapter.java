package cs4084.closely.profile.connections;

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

public class ProfileConnectionsRecyclerViewAdapter extends RecyclerView.Adapter<ProfileConnectionsRecyclerViewAdapter.ViewHolder> {

    private List<Connection> connections;
    private OnConnectionListener onConnectionListener;

    public ProfileConnectionsRecyclerViewAdapter(List<Connection> connections, OnConnectionListener onConnectionListener) {
        this.connections = connections;
        this.onConnectionListener = onConnectionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_connections_recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onConnectionListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileConnectionsRecyclerViewAdapter.ViewHolder holder, final int position) {
        Connection post = connections.get(position);
        holder.name.setText(post.getUsername());
        holder.bio.setText(post.getBio());
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView bio;
        ConstraintLayout layout;
        OnConnectionListener onConnectionListener;

        public ViewHolder(View itemView, OnConnectionListener onConnectionListener) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            bio = itemView.findViewById(R.id.bioTextView);
            layout = itemView.findViewById(R.id.connectionsLayout);
            this.onConnectionListener = onConnectionListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onConnectionListener.onConnectionClick(getAdapterPosition());
        }
    }

    public interface OnConnectionListener {
        void onConnectionClick(int position);
    }
}
