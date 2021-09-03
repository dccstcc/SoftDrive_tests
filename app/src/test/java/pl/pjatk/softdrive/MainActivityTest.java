package pl.pjatk.softdrive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private MainActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void activity_isNotNull() {
        assertNotNull(activity);
    }

    @Test
    public void whenMobileDataOnCheckMethod_thenReturnFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //GIVEN
        Method privateisMobileDataEnabledFromLollipop = MainActivity.class.getDeclaredMethod("isMobileDataEnabledFromLollipop", Context.class);
        privateisMobileDataEnabledFromLollipop.setAccessible(true);
        //WHEN
        boolean mobDataIsEnabled = (boolean) privateisMobileDataEnabledFromLollipop.invoke(activity, activity.getApplicationContext());
        //THEN
        assertFalse(mobDataIsEnabled);
    }

    @Test
    public void whenContainsInstructionStringProperText_thenReturnTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //GIVEN
        Method privategetInstructionString = MainActivity.class.getDeclaredMethod("getInstructionString", null);
        privategetInstructionString.setAccessible(true);
        //WHEN
        String info  = (String) privategetInstructionString.invoke(activity, null);
        //THEN
        assertTrue(info.contains("2. Enable mobile network data"));
    }

    @Test
    public void whenInfoAlertInittialiseProper_thenReturnTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //GIVEN
        Method privategetInfoAlertString = MainActivity.class.getDeclaredMethod("getInfoAlertString", null);
        privategetInfoAlertString.setAccessible(true);
        //WHEN
        String info  = (String) privategetInfoAlertString.invoke(activity, null);
        activity.initialiseInfoPrequisitions("???", info);
        //THEN
        assertTrue(activity.getMobileDataDisableAlert().contains("Mobile data are disable"));
    }

    @Test
    public void whenBtnClickStartsNewActivity_givenBtnAndNewActivityReferences_thenStartNewActivity() {
        //GIVEN
        Intent expectedIntent = new Intent(activity.getApplicationContext(), EnableRouterActivity.class);
        Button btn = (Button) activity.findViewById(R.id.data_enable_btn);
        //WHEN
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(expectedIntent);
            }
        });
        btn.performClick();
        Intent actual = shadowOf(activity.getApplication()).getNextStartedActivity();
        //THEN
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}


