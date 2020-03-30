package cs4084.closely.blog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cs4084.closely.R;
import cs4084.closely.user.User;


public class BlogListFragment extends Fragment implements BlogRecyclerViewAdapter.OnBlogListener {
    private static final String TAG = "MainActivity";

    List<Blog> blogs = new ArrayList<>();

    BlogRecyclerViewAdapter adapter;
    FloatingActionButton createBlogButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        getPersonalBlogs();
 //       fillBlogs();
//        getConnectionsBlogs();
        initRecyclerView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createBlogButton = view.findViewById(R.id.createBlogButton);
        createBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(
                        R.id.action_blogListFragment_to_postFragment
                );
            }
        });

    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.blog_recycler_view);
        adapter = new BlogRecyclerViewAdapter(blogs, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getPersonalBlogs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        CollectionReference blogsCollectionsRef = db.collection("blogs");
        blogsCollectionsRef
                .whereEqualTo("userID", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Successful connection to firestore");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Blog blog = document.toObject(Blog.class);
                        blogs.add(blog);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void getConnectionsBlogs() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getUid();

        db.collection("users").whereEqualTo("userID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        User user = document.toObject(User.class);

                        CollectionReference blogsCollectionsRef = db.collection("blogs");
                        blogsCollectionsRef
                                .whereIn("userID", user.getConnections())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                blogs.add(document.toObject(Blog.class));
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                    }
                }
            }
        });


        Log.d(TAG, "getBlogs: Number of blogs: " + blogs.size());
    }

    private void getAllBlogs(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference blogsCollectionsRef = db.collection("blogs");
        blogsCollectionsRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Successful connection to firestore");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Blog blog = document.toObject(Blog.class);
                                blogs.add(blog);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void fillBlogs() {
        blogs.add(new Blog("We're", "ho", "lets", "go", ""));
        blogs.add(new Blog("no", "ho", "lets", "go", ""));
        blogs.add(new Blog("strangers", "ho", "lets", "go", ""));
        blogs.add(new Blog("to", "ho", "lets", "go", ""));
        blogs.add(new Blog("love,", "ho", "lets", "go", ""));
        blogs.add(new Blog("you", "ho", "lets", "go", ""));
        blogs.add(new Blog("know", "ho", "lets", "go", ""));
        blogs.add(new Blog("the", "ho", "lets", "go", ""));
        blogs.add(new Blog("rules", "ho", "lets", "go", ""));
        blogs.add(new Blog("and", "ho", "lets", "go", ""));
        blogs.add(new Blog("so", "ho", "lets", "go", ""));
        blogs.add(new Blog("do", "ho", "lets", "go", ""));
        blogs.add(new Blog("I", "ho", "lets", "go", ""));
    }

    @Override
    public void onBlogClick(int position) {
        Blog blog = blogs.get(position);
        Bundle blogBundle = new Bundle();
        blogBundle.putParcelable("blog", blog);

        Navigation.findNavController(getView()).navigate(
                R.id.action_blogListFragment_to_viewBlogFragment,
                blogBundle
        );
    }
}
