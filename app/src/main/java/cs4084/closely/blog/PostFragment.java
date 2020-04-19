package cs4084.closely.blog;

import android.content.Intent;
import android.net.Uri;
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
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import cs4084.closely.R;
import cs4084.closely.user.User;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PostFragment";

    private User user;
    private Uri newImage;
    private ImageView postImageView;


    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment,
                container, false);
        postImageView = view.findViewById(R.id.create_blog_image);
        postImageView.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.post_button:
                postBlog();
                break;
            case R.id.create_blog_image:
                addImg();
                break;
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

        final Blog b = new Blog(
                title.getText().toString(),
                subtitle.getText().toString(),
                content.getText().toString(),
                user.getUsername(),
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                new HashMap<String, String>());

        DocumentReference blogref = db.collection("blogs").document();
        b.setDocumentId(blogref.getId());
        blogref.set(b).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (newImage != null) {
                    saveToFirestore(newImage, b.getDocumentId());
                }
                Navigation.findNavController(getView()).navigate(R.id.action_postFragment_to_blogListFragment);
            }
        });

    }

    private void addImg() {
        Log.d(TAG, "addImg: Opening Camera ya goob");
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                newImage = data.getData();
                postImageView.setImageURI(newImage);

            }
        }
    }
    private void saveToFirestore(Uri file, final String blogId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference profileReference = storageRef.child("images/" + blogId);

        profileReference.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return profileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("yeet", "onComplete: " + task.getResult().toString());
                    //Save uri to user object
                    addBlogImage(task.getResult().toString(), blogId);
                }
            }
        });

    }

    private void addBlogImage(String blogImageURI, String blogId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference blogReference = db.collection("blogs").document(blogId);
        blogReference.update("blogImage", blogImageURI).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Did mans ting hapen fam");
                Toast.makeText(getActivity(), "blog image added", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
