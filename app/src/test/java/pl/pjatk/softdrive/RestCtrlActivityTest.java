package pl.pjatk.softdrive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

import android.Manifest;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
@LooperMode(PAUSED)
public class RestCtrlActivityTest {

    private RestCtrlActivity activity;
    String backgroundActive;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(RestCtrlActivity.class);
    }

    @Test
    public void activity_isNotNull() {
        assertNotNull(activity);
    }

    @Test
    public void whenGrantLocationAccess_givenApplicationState_thenConfirmLocationAccess() {
        //GIVEN
        ShadowApplication app = Shadows.shadowOf(activity.getApplication());
        //WHEN
        app.grantPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        //THEN
        assertTrue(activity.isLocationGrant());
    }

    @Test
    public void whenExecuteThread_givenThreadInBackground_thenWorkInBackground_v1_IP() throws InterruptedException {
        //GIVEN
        int numberOfThreads = 1;
        ExecutorService service = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger counter = new AtomicInteger();
        //WHEN
        service.execute(() -> {
            activity.getIpSearchRunnable();
            counter.incrementAndGet();
            latch.countDown();
        });
        latch.await();
        //THEN
        assertEquals(numberOfThreads, counter.get());
    }

    @Test
    public void whenExecuteThread_givenThreadInBackground_thenWorkInBackground_v2_distance() throws InterruptedException {
        //GIVEN
        int numberOfThreads = 1;
        ExecutorService service = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger counter = new AtomicInteger();
        //WHEN
        service.execute(() -> {
            activity.getDistanceReadRunnable(228, null);
            counter.incrementAndGet();
            latch.countDown();
        });
        latch.await();
        //THEN
        assertEquals(numberOfThreads, counter.get());
    }

    @Test
    public void whenExecuteThread_givenThreadInBackground_thenWorkInBackground_v3_GUI() throws InterruptedException {
        //GIVEN
        int numberOfThreads = 1;
        ExecutorService service = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger counter = new AtomicInteger();
        //WHEN
        service.execute(() -> {
            activity.getGuiRunnable(0);
            counter.incrementAndGet();
            latch.countDown();
        });
        latch.await();
        //THEN
        assertEquals(numberOfThreads, counter.get());
    }

}
