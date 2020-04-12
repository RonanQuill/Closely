package cs4084.closely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import cs4084.closely.nfc.NFCManager;
import cs4084.closely.user.User;

public class Closely extends AppCompatActivity  {

    NFCManager nfcManager;
    User loggedInUser;

    public void onLogin() {
        loadLoggedInUser();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private void loadLoggedInUser() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            User.loadUser(userID, new User.OnLoaded() {
                @Override
                public void OnLoaded(User loadedUser) {
                    loggedInUser = loadedUser;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closely);

        nfcManager = new NFCManager(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(loggedInUser == null) {
            loadLoggedInUser();
        }

        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            nfcManager.receiveNfcMessage(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }
}
