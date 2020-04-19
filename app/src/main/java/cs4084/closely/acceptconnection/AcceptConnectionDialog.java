package cs4084.closely.acceptconnection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cs4084.closely.Closely;
import cs4084.closely.R;
import cs4084.closely.user.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceptConnectionDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcceptConnectionDialog extends DialogFragment {

    User connectionRequestUser;

    public AcceptConnectionDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AcceptConnection.
     */
    public static AcceptConnectionDialog newInstance(@NonNull User connectionRequestUser) {
        AcceptConnectionDialog fragment = new AcceptConnectionDialog();

        fragment.connectionRequestUser = connectionRequestUser;

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accept_connection, container, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.fragment_accept_connection, null);

        final TextView messageText = dialogView.findViewById(R.id.messageText);
        messageText.setText(connectionRequestUser.getUsername() + " wants to connect with you");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                .setPositiveButton(R.string.connection_dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Connect the users
                        Closely closely = (Closely)getActivity();
                        User loggedInUser = closely.getLoggedInUser();
                        loggedInUser.connectToUser(connectionRequestUser);
                    }
                })
                .setNegativeButton(R.string.connection_dialog_decline, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AcceptConnectionDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
