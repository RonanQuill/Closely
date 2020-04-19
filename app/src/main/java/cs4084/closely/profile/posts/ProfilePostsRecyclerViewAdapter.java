package cs4084.closely.profile.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs4084.closely.R;
import cs4084.closely.blog.Blog;

public class ProfilePostsRecyclerViewAdapter extends RecyclerView.Adapter<ProfilePostsRecyclerViewAdapter.ViewHolder>{

    private List<Blog> posts;
    private OnBlogListener onBlogListener;

    public ProfilePostsRecyclerViewAdapter(List<Blog> posts, OnBlogListener onBlogListener) {
        this.posts = posts;
        this.onBlogListener = onBlogListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_posts_recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onBlogListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Blog post = posts.get(position);
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        ConstraintLayout layout;
        OnBlogListener onBlogListener;

        public ViewHolder(View itemView, OnBlogListener onBlogListener) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            layout = itemView.findViewById(R.id.postConstraintLayout);
            this.onBlogListener = onBlogListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBlogListener.onBlogClick(getAdapterPosition());
        }
    }

    public interface OnBlogListener {
        void onBlogClick(int position);
    }
}
