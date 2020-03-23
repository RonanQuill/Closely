package cs4084.closely;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "BlogRecyclerViewAdapter";

    private ArrayList<Blog> mBlogs;
    private Context context;

    public BlogRecyclerViewAdapter(ArrayList<Blog> blogs, Context context) {
        mBlogs = blogs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blogTitle;
        public TextView blogSubtitle;
        public TextView blogAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blog_title);
            blogSubtitle = itemView.findViewById(R.id.blog_subtitle);
            blogAuthor = itemView.findViewById(R.id.blog_author);
        }
    }
}
