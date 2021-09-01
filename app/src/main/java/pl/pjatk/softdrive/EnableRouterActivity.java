package pl.pjatk.softdrive;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

/**
 * Check if router access point is enabled
 * @author Dominik Stec
 * @see AppCompatActivity
 */
public class EnableRouterActivity extends AppCompatActivity {

    public String instructionTxt;
    private Button enRouterActBtn;
    String mobileDataDisableAlert;

    /**
     * Activity with layout for control router WiFi accessibility
     * @param savedInstanceState Android application Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_enable);

        if(isApEnable()) {
            startNextActivity();
        } else {
            showInstruction(getInstructionDefault());
            confBtnListener(getInfoAlertDefault());
        }
    }

    public void confBtnListener(String btnInfoOnClick) {
        enRouterActBtn = (Button) findViewById(R.id.ap_enable_btn);
        enRouterActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isApEnable()) {
                    startNextActivity();
                } else {
                    showInfoAlert(btnInfoOnClick);
                }
            }
        });
    }

    public void startNextActivity() {
        Intent enRestAct = new Intent(getApplicationContext(), RestCtrlActivity.class);
        startActivity(enRestAct);
    }

    public void showInfoAlert(String alertTxt) {
        mobileDataDisableAlert = alertTxt;
        TextView apRouterAlertTxtView = (TextView) findViewById(R.id.ap_data_dis);
        apRouterAlertTxtView.setText(mobileDataDisableAlert);
    }

    @NonNull
    public String getInfoAlertDefault() {
        String mobileDataDisableAlert = "Wifi router is disable\n" +
                "Please follow instruction note";
        return mobileDataDisableAlert;
    }

    public void showInstruction(String instruction) {
        instructionTxt = instruction;
        TextView instructionTxtView = (TextView) findViewById(R.id.instruction_str);
        instructionTxtView.setText(instructionTxt);
    }

    @NonNull
    public String getInstructionDefault() {
        String instructionTxt = "            Router Wifi enable is need:\n" +
                "            1. Go to: \n" +
                "               -> Settings\n" +
                "               -> Connections\n" +
                "               -> Wifi Hotspot\n" +
                "            2. Enable Wifi Hotspot\n" +
                "            3. Set SSID name:     safedrivea\n" +
                "            4. Set password:      safedrivea\n" +
                "            5. Enter into SoftDrive application again";
        return instructionTxt;
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

    public String getInstructionTxt() {
        return instructionTxt;
    }

    public void setInstructionTxt(String instructionTxt) {
        this.instructionTxt = instructionTxt;
    }

    public String getInfoAlertTxt() {
        return mobileDataDisableAlert;
    }

    public void setInfoAlertTxt(String mobileDataDisableAlert) {
        this.mobileDataDisableAlert = mobileDataDisableAlert;
    }
}
