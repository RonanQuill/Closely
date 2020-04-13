package cs4084.closely.blog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import cs4084.closely.R;
import cs4084.closely.user.User;


public class PostFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PostFragment";

    private User user;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment,
                container, false);

        Log.d("", "onCreateView: Big view created");
        getUser();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button post = view.findViewById(R.id.post_button);
        post.setOnClickListener(this);
        Log.d("click", " added");
    }

    @Override
    public void onClick(View v) {
        Log.d("Button pressed", "onClick: ");
        switch (v.getId()) {
            case R.id.post_button:
                postBlog();
        }
    }

    private void getUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").whereEqualTo("userID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        user = document.toObject(User.class);
                        user.setDocumentID(document.getId());
                    }
                }
            }
        });
    }

    private void postBlog() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView title = getView().findViewById(R.id.create_blog_title);
        TextView subtitle = getView().findViewById(R.id.Create_blog_subtitle);
        TextView content = getView().findViewById(R.id.Create_blog_content);

        Blog b = new Blog(
                title.getText().toString(),
                subtitle.getText().toString(),
                content.getText().toString(),
                user.getUsername(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                null);

        db.collection("blogs").add(b)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }
}
