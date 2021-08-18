package pl.pjatk.softdrive.view;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Display dimensions getter
 * @author Dominik Stec
 */
public class Display {

    private int displayWidth;
    private int displayHeight;

    /**
     * Initialize getter of display pixels dimensions
     * @param context this Android application Context object
     */
    public Display(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

}
