package pl.pjatk.softdrive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.pjatk.softdrive.rest.controllers.RestSearchIpCtrl;

@RunWith(AndroidJUnit4.class)
public class RestSearchIpCtrlTest {

    RestSearchIpCtrl ctrl;

    @Before
    public void setUp() {
        ctrl = new RestSearchIpCtrl();
    }

    @Test
    public void whenNoAction_givenDefaultValue_thenValueNotChange()  {
        assertEquals(ctrl.getIp4Byte(), "none");
        assertEquals(ctrl.getFourthIp(), 1);
        assertEquals(ctrl.getIp(), 1);
    }

    @Test
    public void whenBuildUrl_givenPartialIp_thenHaveCorrectParts()  {
        //GIVEN
        String url;
        //WHEN
        url = ctrl.getPreparedUrl(224);
        //THEN
        assertTrue(url.contains("http"));
        assertTrue(url.contains("192.168."));
        assertTrue(url.contains(":8080"));
        assertTrue(url.contains("224"));
        assertTrue(url.contains("null"));
    }

}
