package cs4084.closely.nfc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.widget.Toast;

import cs4084.closely.Closely;
import cs4084.closely.R;
import cs4084.closely.user.User;

import static android.nfc.NdefRecord.createMime;

public class NFCManager implements NfcAdapter.CreateNdefMessageCallback{

    // record 0 contains the MIME type, record 1 is the AAR
    private static int NFC_USER_ID_ARRAY_INDEX = 2;

    private NfcAdapter nfcAdapter;
    private Activity activity;

    public NFCManager(Context context, Activity activity) {
        this.activity = activity;

        // Check for available NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (nfcAdapter == null) {
            Toast.makeText(context, "NFC is not available", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(context, "NFC is available", Toast.LENGTH_LONG).show();
        }

        // Register callback
        nfcAdapter.setNdefPushMessageCallback(this, activity);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        Closely closely = (Closely)activity;
        User user = closely.getUser();

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
        String userId = new String(message.getRecords()[NFC_USER_ID_ARRAY_INDEX].getPayload());

        Closely closely = (Closely)activity;
        closely.addConnectionToUser(userId);
    }
}
