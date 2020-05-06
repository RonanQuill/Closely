package cs4084.closely.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

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

        final ImageView i = view.findViewById(R.id.home_image);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.spin);
        Animation pulse = AnimationUtils.loadAnimation(getContext(), R.anim.pulse);
        final AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(rotate);
        animationSet.addAnimation(pulse);
        rotate.setRepeatMode(Animation.INFINITE);
        pulse.setRepeatMode(Animation.REVERSE);

        i.setOnClickListener(new View.OnClickListener() {
            boolean nfcOn = false;
            Closely closely = (Closely) getActivity();

            @Override
            public void onClick(View v) {
                nfcOn = !nfcOn;
                if (nfcOn) {
                    i.startAnimation(animationSet);
                    closely.getNfcManager().enableNFC();

                } else {
                    i.clearAnimation();
                    closely.getNfcManager().disableNFC();
                }

            }
        });

//        ToggleButton nfcToggleButton = view.findViewById(R.id.nfcToggleButton);
//        nfcToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    // The toggle is enabled
//
//                } else {
//                    // The toggle is disabled
//
//                }
//            }
//        });
        return view;
    }
}
