package pl.pjatk.softdrive.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import pl.pjatk.softdrive.R;

public class FragmentActivity extends Fragment {
    private TrafficView trafficView;    // custom view to display the game

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // inflate the fragment_main.xml layout
        View view =
                inflater.inflate(R.layout.fragment_main, container, false);

        // get a reference to the TrafficView
        trafficView = (TrafficView) view.findViewById(R.id.trafficView);
        return view;
    }
}
