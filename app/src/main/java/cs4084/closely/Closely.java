package cs4084.closely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import cs4084.closely.nfc.NFCManager;
import cs4084.closely.user.User;

public class Closely extends AppCompatActivity  {

    NFCManager nfcManager;
    User loggedInUser;

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public NFCManager getNfcManager() {
        return nfcManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closely);

        nfcManager = new NFCManager(this, this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }
}
