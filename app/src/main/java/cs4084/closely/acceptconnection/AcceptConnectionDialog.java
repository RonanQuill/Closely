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

import cs4084.closely.R;
import cs4084.closely.user.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceptConnectionDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcceptConnectionDialog extends DialogFragment {

    TextView messageText;

    public AcceptConnectionDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AcceptConnection.
     */
    public static AcceptConnectionDialog newInstance() {
        AcceptConnectionDialog fragment = new AcceptConnectionDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accept_connection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageText = view.findViewById(R.id.messageText);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_accept_connection, null))
                .setPositiveButton(R.string.connection_dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Connect the users
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

    public void setUserData(String userId) {
        User.loadUser(userId, new User.OnLoaded() {
            @Override
            public void OnLoaded(User user) {
                messageText.setText(user.getUsername() + "wants to connect with you");
            }
        });
    }
}
