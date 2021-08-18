package pl.pjatk.softdrive;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Exit application
 * @author Dominik Stec
 * @see AppCompatActivity
 */
public class Exit extends AppCompatActivity {

    /**
     * Disable screen always on option and restore screen brightness.
     * @param savedInstanceState Android application Bundle
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setScreenOff();

        finishAndRemoveTask();
    }

    private void setScreenOff() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = -1;
        getWindow().setAttributes(params);
    }
}