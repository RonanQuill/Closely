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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cs4084.closely.R;
import cs4084.closely.user.User;


public class BlogListFragment extends Fragment implements BlogRecyclerViewAdapter.OnBlogListener {
    private static final String TAG = "BlogListFragment";

    SwipeRefreshLayout swipeRefreshLayout;

    private List<Blog> blogs = new ArrayList<>();
    private User currentUser;
    private DocumentSnapshot lastQueriedDocument;

    BlogRecyclerViewAdapter adapter;
    FloatingActionButton createBlogButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        initRecyclerView(view);
        getBlogs();

        swipeRefreshLayout = view.findViewById(R.id.view_blog_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: Refreshing");
                getBlogs();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

    private void getBlogs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();

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
                            getConnectionBlogs();
                        }
                    }
                });
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

    private void getConnectionBlogs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;
        if (currentUser != null) {
            if (lastQueriedDocument != null) {
                Log.d(TAG, "starting after: " + lastQueriedDocument.toObject(Blog.class));
                query = db.collection("blogs")
                        .whereIn("userID", currentUser.getConnections())
                        .startAfter(lastQueriedDocument);
            } else {
                query = db.collection("blogs").whereIn("userID",
                        currentUser.getConnections());
            }

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            blogs.add(document.toObject(Blog.class));
                            Log.d(TAG, "onComplete: " + document.toObject(Blog.class));
                        }

                        if (task.getResult().size() != 0) {
                            lastQueriedDocument = task.getResult().getDocuments()
                                    .get(task.getResult().size() - 1);
                        }
                        adapter.notifyDataSetChanged();

                    }
                }
            });
        }
    }
}
