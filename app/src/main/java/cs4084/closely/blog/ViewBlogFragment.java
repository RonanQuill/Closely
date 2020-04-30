package cs4084.closely.blog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import cs4084.closely.R;
import cs4084.closely.user.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBlogFragment extends Fragment {

    private Blog blog;
    private List<Comment> blogComments;

    private WebView webView;
    private EditText writeCommentBox;
    private CommentRecyclerViewAdapter commentRecyclerViewAdapter;

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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_blog, container, false);
        TextView titleView = view.findViewById(R.id.view_blog_title);
        TextView subtitleView = view.findViewById(R.id.view_blog_subtitle);
        TextView authorView = view.findViewById(R.id.view_blog_author);

        writeCommentBox = view.findViewById(R.id.view_blog_comment_content);

        webView = view.findViewById(R.id.blog_post_renderer);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("MyApplication", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );
                return true;
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        TextView commentTitleText = view.findViewById(R.id.view_blog_comment_text);
        ImageView blogImageView = view.findViewById(R.id.imageView_viewBlog);
        Button postComment = view.findViewById(R.id.view_blog_add_comment);

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCommentForCurrentUser();
            }
        });

        if (blog != null) {
            titleView.setText(blog.getTitle());
            subtitleView.setText(blog.getSubtitle());
            authorView.setText(blog.getAuthor());

            String html = processBlogPostHtml(blog.getTitle(), blog.getBody());
            Log.d("HTML", html);
            webView.setVisibility(View.GONE);
            webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

            if (blog.getBlogImage() != null && !blog.getBlogImage().isEmpty()) {
                Log.d(TAG, "onCreateView: " + blog.getBlogImage());
                Glide.with(getContext()).load(blog.getBlogImage()).into(blogImageView);
            } else {
                blogImageView.setVisibility(View.GONE);
            }

            blogComments = blog.getCommentList();
            if (blogComments.isEmpty()) {
                commentTitleText.setVisibility(View.GONE);
            }

        }

        initRecyclerView(view);
        return view;
    }

    private String processBlogPostHtml(String title, String bodyHtml) {
        String htmlDocumentTags ="<!DOCTYPE html><html lang=\"en\"><head><title>%s</title><link href=\"https://fonts.googleapis.com/css?family=Montserrat\" rel=\"stylesheet\"><style>*{color: white; font-family: 'Montserrat', sans-serif;}</style></head><body>%s</body></html>";

        return String.format(htmlDocumentTags, title, bodyHtml);
    }

    private void postCommentForCurrentUser() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").whereEqualTo("userID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot results = task.getResult();
                            try {
                                List<DocumentSnapshot> documents = results.getDocuments();
                                if(!documents.isEmpty()) {
                                    DocumentSnapshot document = documents.get(0);

                                    User currentUser = document.toObject(User.class);
                                    Log.d(TAG, "onComplete: User found");

                                    postComment(currentUser.getUsername());
                                }
                            } catch (NullPointerException e) {
                                Log.e(TAG, e.toString());
                            }

                        }
                    }

                });
    }

    private void postComment(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference blogRef = db.collection("blogs").document(blog.getDocumentId());

        String commentContent = writeCommentBox.getText().toString();
        if (!commentContent.isEmpty()) {

            Map<String, String> currentBlogComments = blog.getComments();
            currentBlogComments.put(username, commentContent);

            blogRef.update("comments", currentBlogComments)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Comment posted", Toast.LENGTH_SHORT).show();

                            blogComments = blog.getCommentList();

                            commentRecyclerViewAdapter.notifyDataSetChanged();
                            writeCommentBox.setText("");
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
        commentRecyclerViewAdapter = new CommentRecyclerViewAdapter(blogComments);
        recyclerView.setAdapter(commentRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
