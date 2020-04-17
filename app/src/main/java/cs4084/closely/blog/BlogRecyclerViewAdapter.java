package cs4084.closely.blog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs4084.closely.R;

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.ViewHolder> {

    private List<Blog> mBlogs;


    private OnBlogListener onBlogListener;

    public BlogRecyclerViewAdapter(List<Blog> blogs, OnBlogListener onBlogListener) {
        mBlogs = blogs;
        this.onBlogListener = onBlogListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view, onBlogListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.blogTitle.setText(mBlogs.get(position).getTitle());
        holder.blogSubtitle.setText(mBlogs.get(position).getSubtitle());
        holder.blogAuthor.setText(mBlogs.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return mBlogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView blogTitle;
        public TextView blogSubtitle;
        public TextView blogAuthor;
        OnBlogListener onBlogListener;

        public ViewHolder(@NonNull View itemView, OnBlogListener onBlogListener) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blog_title);
            blogSubtitle = itemView.findViewById(R.id.blog_subtitle);
            blogAuthor = itemView.findViewById(R.id.blog_author);
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
