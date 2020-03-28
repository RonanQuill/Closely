package cs4084.closely;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class BlogListFragment extends Fragment {
    private static final String TAG = "MainActivity";
    ArrayList<Blog> blogs = new ArrayList<>();
    BlogRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
//        getPersonalBlogs();
        fillBlogs();
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.blog_recycler_view);
        adapter = new BlogRecyclerViewAdapter(blogs, getContext());
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

    // TODO change query to search users connections IDs
    private void getConnectionsBlogs(final View view) {
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
                                Log.d(TAG, "Snapshot: blog created");
                                Blog blog = document.toObject(Blog.class);
                                blogs.add(blog);
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
}
