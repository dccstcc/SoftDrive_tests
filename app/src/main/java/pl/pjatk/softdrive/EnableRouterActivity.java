package pl.pjatk.softdrive;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.reflect.Method;

/**
 * Check if router access point is enabled
 * @author Dominik Stec
 * @see AppCompatActivity
 */
public class EnableRouterActivity extends AppCompatActivity {

    private WifiManager wifiManager;

    /**
     * Activity with layout for control router WiFi accessibility
     * @param savedInstanceState Android application Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_enable);

        boolean wifiApEnable = isApEnable();
        if(wifiApEnable) {
            Intent enRestAct = new Intent(getApplicationContext(), RestCtrlActivity.class);
            startActivity(enRestAct);
        }

        String instructionTxt = "            Router Wifi enable is need:\n" +
                "            1. Go to: \n" +
                "               -> Settings\n" +
                "               -> Connections\n" +
                "               -> Wifi Hotspot\n" +
                "            2. Enable Wifi Hotspot\n" +
                "            3. Set SSID name:     safedrivea\n" +
                "            4. Set password:      safedrivea\n" +
                "            5. Enter into SoftDrive application again";

        TextView instructionTxtView = (TextView) findViewById(R.id.instruction_str);
        instructionTxtView.setText(instructionTxt);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Button enRouterActBtn = (Button) findViewById(R.id.ap_enable_btn);
        enRouterActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean wifiApEnable = isApEnable();

                if (wifiApEnable) {
                    Intent enRestAct = new Intent(getApplicationContext(), RestCtrlActivity.class);
                    startActivity(enRestAct);
                } else {
                    String mobileDataDisableAlert = "Wifi router is disable\n" +
                            "Please follow instruction note";
                    TextView apRouterAlertTxtView = (TextView) findViewById(R.id.ap_data_dis);
                    apRouterAlertTxtView.setText(mobileDataDisableAlert);
                }
            }
        });
    }

    /**
     * Check if router WiFi is enable on device
     * @return true if access point is enable
     */
    private boolean isApEnable() {
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        boolean ret = false;
        try{
            Method isWifiApEnabledMethod = wifiManager.getClass().getMethod("isWifiApEnabled");
            boolean isApEnable = (Boolean) isWifiApEnabledMethod.invoke(wifiManager);
            ret = isApEnable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
