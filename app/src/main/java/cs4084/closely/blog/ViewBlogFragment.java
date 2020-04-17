package cs4084.closely.blog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import java.util.HashMap;

import cs4084.closely.R;
import cs4084.closely.user.User;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBlogFragment extends Fragment {
    private Blog blog;
    CommentRecyclerViewAdapter adapter;
    private User currentUser;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


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
        initRecyclerView(view);
        TextView subtitleView = view.findViewById(R.id.view_blog_subtitle);
        TextView authorView = view.findViewById(R.id.view_blog_author);
        TextView bodyView = view.findViewById(R.id.view_blog_body);
        ImageView myimgV = view.findViewById(R.id.imageView_viewBlog);
        Button postComment = view.findViewById(R.id.view_blog_add_comment);
        final ImageView imageView =  view.findViewById(R.id.imageView2);
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
            if(blog.getBlogImage() != null && !blog.getBlogImage().isEmpty()) {
                Glide.with(getContext()).load(blog.getBlogImage()).into(myimgV);
            } else {
                myimgV.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private void getUsername () {
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
                            Toast.makeText(getActivity(), "Comment posted",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void postComment() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference washingtonRef = db.collection("blogs").document(blog.getDocumentId());
        TextView content = getView().findViewById(R.id.view_blog_comment_content);
        HashMap<String, String> comments = blog.getComments();
        comments.put(currentUser.getUsername(), content.getText().toString());
        washingtonRef
                .update("comments",comments)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });


    }
    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.comment_recycler_view);
        adapter = new CommentRecyclerViewAdapter(blog.getCommentList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
