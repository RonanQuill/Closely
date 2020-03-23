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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class BlogListFragment extends Fragment {
    private static final String TAG = "MainActivity";
    ArrayList<Blog> blogs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        fillBlogs();
        RecyclerView recyclerView = view.findViewById(R.id.blog_recycler_view);
        BlogRecyclerViewAdapter adapter = new BlogRecyclerViewAdapter(blogs, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getBlogs(final View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference blogsCollectionsRef = db.collection("blogs");
        blogsCollectionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    private void fillBlogs() {
        blogs.add(new Blog("hi", "ho", "lets", "go", ""));
        blogs.add(new Blog("hi", "ho", "lets", "go", ""));
        blogs.add(new Blog("hi", "ho", "lets", "go", ""));
        blogs.add(new Blog("hi", "ho", "lets", "go", ""));
        blogs.add(new Blog("hi", "ho", "lets", "go", ""));
    }
}
