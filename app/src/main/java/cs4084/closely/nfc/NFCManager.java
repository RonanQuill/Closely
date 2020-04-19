package cs4084.closely.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.widget.Toast;

import cs4084.closely.Closely;
import cs4084.closely.acceptconnection.AcceptConnectionDialog;
import cs4084.closely.user.User;

import static android.nfc.NdefRecord.createMime;

public class NFCManager implements NfcAdapter.CreateNdefMessageCallback{

    // record 0 contains the MIME type, record 1 is the AAR
    private static int NFC_USER_ID_ARRAY_INDEX = 0;

    private NfcAdapter nfcAdapter;
    private Closely closely;

    public NFCManager(Context context, Closely closely) {
        this.closely = closely;

        // Check for available NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        /*if (nfcAdapter == null) {
            Toast.makeText(context, "NFC is not available on this device.", Toast.LENGTH_LONG).show();
            return;
        }*/

        // Register callback
        nfcAdapter.setNdefPushMessageCallback(this, closely);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        User user = closely.getLoggedInUser();

        String text = user.getUserID();

        return new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/vnd.cs4084.closely",
                        text.getBytes()),
                        NdefRecord.createApplicationRecord("cs4084.closely") });
    }

    public void receiveNfcMessage(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        // only one message sent during the beam
        NdefMessage message = (NdefMessage) rawMessages[0];

        // record 0 contains the MIME type, record 1 is the AAR, if present
        final String userId = new String(message.getRecords()[NFC_USER_ID_ARRAY_INDEX].getPayload());

        final User loggedInUser = closely.getLoggedInUser();

        User.loadUser(userId, new User.OnLoadedEvent() {
            @Override
            public void OnLoaded(User connectionRequestUser) {
                if(loggedInUser == null) {
                //Display that XY wants to connect but you must be logged in
                    showLogInToAddConnectionDialog(connectionRequestUser);
                } else {
                    if(loggedInUser.getUserID().equals(userId) && !loggedInUser.getConnections().contains(userId)) {
                        showAddConnectionDialog(connectionRequestUser);
                    }
                }
            }
        });
    }

    private void showAddConnectionDialog(User user) {
        AcceptConnectionDialog acceptConnectionDialog = AcceptConnectionDialog.newInstance(user);
        acceptConnectionDialog.show(closely.getSupportFragmentManager(), "connection");
    }

    private void showLogInToAddConnectionDialog(User user) {
        Toast.makeText(closely, "Log in to add connections.", Toast.LENGTH_LONG).show();
    }
}
