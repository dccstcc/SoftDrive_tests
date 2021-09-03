package pl.pjatk.softdrive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(RobolectricTestRunner.class)
public class EnableRouterActivityTest {

    private EnableRouterActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(EnableRouterActivity.class);
    }

    @Test
    public void activity_isNotNull() {
        assertNotNull(activity);
    }

    @Test
    public void whenCheckAccessPointIsEnable_givenAccessPointDisable_thenReturnFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //GIVEN
        Method privateisApEnable = EnableRouterActivity.class.getDeclaredMethod("isApEnable",  null);
        privateisApEnable.setAccessible(true);
        //WHEN
        boolean isApEnable = (boolean) privateisApEnable.invoke(activity, null);
        //THEN
        assertFalse(isApEnable);
    }

    @Test
    public void whenExpectedNewActivity_givenStartNewActivityMethod_thenStartRestCtrlActivity() {
        //GIVEN
        Intent expectedIntent = new Intent(activity.getApplicationContext(), RestCtrlActivity.class);
        //WHEN
        activity.startNextActivity();
        Intent actual = shadowOf(activity.getApplication()).getNextStartedActivity();
        //THEN
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void whenClickButtonWithRouterOff_givenGrantAccessPermissions_thenShowInfoAlert() {
        //GIVEN
        Button btn = activity.findViewById(R.id.ap_enable_btn);
        ShadowApplication app = Shadows.shadowOf(activity.getApplication());
        app.grantPermissions(Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET);
        //WHEN
        btn.performClick();
        //THEN
        TextView txt = activity.findViewById(R.id.ap_data_dis);
        assertEquals(txt.getText().toString(), activity.getInfoAlertDefault());
    }
}
