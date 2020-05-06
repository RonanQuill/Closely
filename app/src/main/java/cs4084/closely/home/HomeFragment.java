package cs4084.closely.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import cs4084.closely.Closely;
import cs4084.closely.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ToggleButton nfcToggleButton = view.findViewById(R.id.nfcToggleButton);
        nfcToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Closely closely = (Closely) getActivity();
                if (isChecked) {
                    // The toggle is enabled
                    closely.getNfcManager().enableNFC();
                } else {
                    // The toggle is disabled
                    closely.getNfcManager().disableNFC();
                }
            }
        });
        return view;
    }
}
