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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
        TextView subtitleView = view.findViewById(R.id.view_blog_subtitle);
        TextView authorView = view.findViewById(R.id.view_blog_author);
        TextView bodyView = view.findViewById(R.id.view_blog_body);
        Button postComment = view.findViewById(R.id.view_blog_add_comment);
        final ImageView imageView =  view.findViewById(R.id.imageView2);
        Button imageBtn = view.findViewById(R.id.imageButton);
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUsername();
            }
        });
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);

                    } else {
                        pickImageFromGallery();
                    }
                }
            }
            private void pickImageFromGallery() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_PICK_CODE);
            }


            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
                    imageView.setImageURI(data.getData());
                }
            }


            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                switch (requestCode){
                    case PERMISSION_CODE : {
                        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            pickImageFromGallery();
                        }
                        else {
                            Toast.makeText(getActivity(), "Permission DENIED",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        if (blog != null) {
            titleView.setText(blog.getTitle());
            subtitleView.setText(blog.getSubtitle());
            authorView.setText(blog.getAuthor());
            bodyView.setText(blog.getBody());
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
        DocumentReference washingtonRef = db.collection("blogs").document("6sYh3Sa69ccVmBDpYMSd");
        TextView content = getView().findViewById(R.id.view_blog_comment_content);
        HashMap<String, String> comments = new HashMap<String, String>();
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

}
