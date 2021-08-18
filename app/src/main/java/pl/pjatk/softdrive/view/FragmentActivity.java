package pl.pjatk.softdrive.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;

import pl.pjatk.softdrive.R;


public class FragmentActivity extends Fragment {
    private UiView uiView;    // custom view to display the UI

    private ExecutorService executorService;

    private View viewRet;

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // inflate the fragment_main.xml layout
        View view =
                inflater.inflate(R.layout.fragment_main, container, false);

        // get a reference to the UiView
        uiView = (UiView) view.findViewById(R.id.uiView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}



