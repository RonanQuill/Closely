package cs4084.closely.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import cs4084.closely.R;
import cs4084.closely.user.User;


public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";

    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button signUpBtn;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        emailField = view.findViewById(R.id.sign_up_email);
        passwordField = view.findViewById(R.id.sign_up_password);
        confirmPasswordField = view.findViewById(R.id.sign_up_pwd_confirm);
        signUpBtn = view.findViewById(R.id.sign_up_btn);
        auth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(emailField.getText().toString(), passwordField.getText().toString());
            }
        });

    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getActivity(), "Sign up successful", Toast.LENGTH_SHORT).show();

                            createBlankUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email Required");
            valid = false;
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password Required");
            valid = false;
        } else if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters");
            valid = false;
        }

        String confirmedPassword = confirmPasswordField.getText().toString();
        if (TextUtils.isEmpty(confirmedPassword)) {
            confirmPasswordField.setError("Required");
            valid = false;
        } else if (!confirmedPassword.equals(password)) {
            confirmPasswordField.setError("Passwords not the same");
            valid = false;
        }
        return valid;
    }


    private void createBlankUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newUserRef = db.collection("users").document();
        User blankUser = new User(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                newUserRef.getId()
        );

        newUserRef.set(blankUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IsNewUser", true);
                Navigation.findNavController(getView()).navigate(
                        R.id.action_signUpFragment_to_editProfileFragment2, bundle);
            }
        });
    }
}
