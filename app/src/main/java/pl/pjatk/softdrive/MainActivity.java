package pl.pjatk.softdrive;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Start of application
 * @author Dominik Stec
 * @see AppCompatActivity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Activity with layout for control mobile internet access accessibility
     * @param savedInstanceState Android application Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent enRouterAct = new Intent(this, EnableRouterActivity.class);

        boolean mobDataFlag = isMobileDataEnabledFromLollipop(this);

        if(mobDataFlag) {
            this.startActivity(enRouterAct);
        }

        String instructionTxt = "            Mobile network data enable is need:\n" +
                "            1. Go to: \n" +
                "               -> Settings\n" +
                "               -> Connections\n" +
                "               -> Data Counter\n" +
                "            2. Enable mobile network data\n" +
                "            3. Enter into SoftDrive application again";

        TextView instructionTxtView = (TextView)findViewById(R.id.mob_data_instruction);
        instructionTxtView.setText(instructionTxt);

        Button enMobDataBtn = (Button) findViewById(R.id.data_enable_btn);
        enMobDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean mobDataFlag = isMobileDataEnabledFromLollipop(getApplicationContext());

                if(mobDataFlag) {
                    Intent enRouterAct = new Intent(getApplicationContext(), EnableRouterActivity.class);
                    startActivity(enRouterAct);
                }
                else {
                    String mobileDataDisableAlert = "Mobile data are disable\n" +
                                                    "Please follow instruction note";
                    TextView mobDataAlertTxtView = (TextView)findViewById(R.id.mob_data_dis);
                    mobDataAlertTxtView.setText(mobileDataDisableAlert);
                }
            }

            private boolean isMobileDataEnabledFromLollipop(Context context) {
                boolean state = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
                }
                return state;
            }
        });
    }

    /**
     * @param context this Android application Context object
     * @return true if mobile internet access is enable
     * Based on following configuration
     * @link https://stackoverflow.com/questions/26539445/the-setmobiledataenabled-method-is-no-longer-callable-as-of-android-l-and-later/50912113#50912113 [18.08.2021]
     */
    private boolean isMobileDataEnabledFromLollipop(Context context) {
        boolean state = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
        }
        return state;
    }
}