package cs4084.closely.profile.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs4084.closely.R;
import cs4084.closely.blog.Blog;
import cs4084.closely.blog.ViewBlogFragment;


public class ProfilePostsFragment extends Fragment implements ProfilePostsRecyclerViewAdapter.OnBlogListener {
    private static final String TAG = "ProfilePostsFragment";
    private ProfilePostsRecyclerViewAdapter profilePostsRecyclerViewAdapter;
    private RecyclerView postsRecyclerView;
    private List<Blog> posts;

    public ProfilePostsFragment() {
        // Required empty public constructor
    }

    public ProfilePostsFragment(List<Blog> posts) {
        this.posts = posts;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_posts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilePostsRecyclerViewAdapter = new ProfilePostsRecyclerViewAdapter(posts, this);

        postsRecyclerView = (RecyclerView) view.findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setAdapter(profilePostsRecyclerViewAdapter);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void notifyDataSetChanged() {
        profilePostsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBlogClick(int position) {
        Log.d(TAG, "onBlogClick: Clicked the posts list");
        Blog blog = posts.get(position);
        Bundle blogBundle = new Bundle();
        blogBundle.putParcelable("blog", blog);
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        ViewBlogFragment viewBlogFragment = new ViewBlogFragment();
        viewBlogFragment.setArguments(blogBundle);
        t.replace(R.id.nagivationDisplay, viewBlogFragment).commit();
    }
}
