package cs4084.closely.blog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs4084.closely.R;

public class CommentRecyclerViewAdapter  extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder>  {
    private List<Comment> mComments;

    public CommentRecyclerViewAdapter(List<Comment> comments) {
        mComments = comments;
    }

    @NonNull
    @Override
    public CommentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.author.setText(mComments.get(position).getAuthor());
            holder.body.setText(mComments.get(position).getBody());

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_commentview);
            body = itemView.findViewById(R.id.body_commentview);
        }
    }
}
