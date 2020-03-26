package cs4084.closely;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class PostFragment extends Fragment implements View.OnClickListener {

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment,
                container, false);

        Log.d("", "onCreateView: Big view created");
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
        Log.d("Button pressed", "onClick: ");
        switch (v.getId()) {
            case R.id.post_button:
                postBlog();
        }
    }

    private void postBlog() {
        Log.d("Log text", "postBlog: ");
    }
}
