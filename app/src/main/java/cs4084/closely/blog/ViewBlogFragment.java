package cs4084.closely.blog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cs4084.closely.R;
import cs4084.closely.user.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBlogFragment extends Fragment {

    private Blog blog;
    private ArrayList<Comment> blogComments;
    private CommentRecyclerViewAdapter adapter;
    private User currentUser;

    public ViewBlogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle blogBundle = this.getArguments();
        if (blogBundle != null) {
            blog = blogBundle.getParcelable("blog");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_blog, container, false);
        TextView titleView = view.findViewById(R.id.view_blog_title);
        TextView subtitleView = view.findViewById(R.id.view_blog_subtitle);
        TextView authorView = view.findViewById(R.id.view_blog_author);
        TextView bodyView = view.findViewById(R.id.view_blog_body);
        TextView commentTitleText = view.findViewById(R.id.view_blog_comment_text);
        ImageView blogImageView = view.findViewById(R.id.imageView_viewBlog);
        Button postComment = view.findViewById(R.id.view_blog_add_comment);
        blogComments = new ArrayList<Comment>();

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUsername();
            }
        });

        if (blog != null) {
            titleView.setText(blog.getTitle());
            subtitleView.setText(blog.getSubtitle());
            authorView.setText(blog.getAuthor());
            bodyView.setText(blog.getBody());
            if (blog.getBlogImage() != null && !blog.getBlogImage().isEmpty()) {
                Log.d(TAG, "onCreateView: " + blog.getBlogImage());
                Glide.with(getContext()).load(blog.getBlogImage()).into(blogImageView);
            } else {
                blogImageView.setVisibility(View.GONE);
            }

            if (blog.getCommentList().size() == 0) {
                commentTitleText.setVisibility(View.GONE);
            }

            blogComments = (ArrayList<Comment>) blog.getCommentList();
        }

        initRecyclerView(view);
        return view;
    }

    private void getUsername() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("userID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentUser = document.toObject(User.class);
                                Log.d(TAG, "onComplete: User found");
                            }
                            postComment();
                        }
                    }
                });
    }

    private void postComment() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference blogRef = db.collection("blogs").document(blog.getDocumentId());
        final TextView content = getView().findViewById(R.id.view_blog_comment_content);
        if (!content.getText().toString().isEmpty()) {
            blog.getComments().put(currentUser.getUsername(), content.getText().toString());
            blogRef.update("comments", blog.getComments())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Comment posted",
                                    Toast.LENGTH_SHORT).show();
                            blogComments.clear();
                            blogComments.addAll(blog.getCommentList());
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });


        } else {
            Toast.makeText(getActivity(), "Comments cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.comment_recycler_view);
        adapter = new CommentRecyclerViewAdapter(blogComments);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
