package pl.pjatk.softdrive;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean mobDataFlag;
    private TextView instructionTxtView;
    private Button enMobDataBtn;
    private String instructionTxt;
    private String mobileDataDisableAlert;
    private Intent nextIntentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseInfoPrequisitions(getInstructionString(), getInfoAlertString());
        setNextIntentActivity(new Intent(this, EnableRouterActivity.class));

        setContentView(R.layout.activity_main);

        mobDataFlag = isMobileDataEnabledFromLollipop(this);

        if(mobDataFlag) {
            startActivity(getNextIntentActivity());
        }

        instructionTxtView = (TextView)findViewById(R.id.mob_data_instruction);
        instructionTxtView.setText(instructionTxt);

        enMobDataBtn = (Button) findViewById(R.id.data_enable_btn);
        enMobDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobDataFlag = isMobileDataEnabledFromLollipop(getApplicationContext());

                if(mobDataFlag) {
                    startActivity(getNextIntentActivity());
                }
                else {
                    TextView mobDataAlertTxtView = (TextView)findViewById(R.id.mob_data_dis);
                    mobDataAlertTxtView.setText(mobileDataDisableAlert);
                }
            }
        });
    }

    public void initialiseInfoPrequisitions(String instruction, String offInfoAlert) {
        this.instructionTxt = instruction;
        this.mobileDataDisableAlert = offInfoAlert;
    }

    @NonNull
    private String getInfoAlertString() {
        String infoAlert = "Mobile data are disable\n" +
                "Please follow instruction note";
        return infoAlert;
    }

    @NonNull
    private String getInstructionString() {
        String instructionTxt = "            Mobile network data enable is need:\n" +
                "            1. Go to: \n" +
                "               -> Settings\n" +
                "               -> Connections\n" +
                "               -> Data Counter\n" +
                "            2. Enable mobile network data\n" +
                "            3. Enter into SoftDrive application again";
        return instructionTxt;
    }

    private boolean isMobileDataEnabledFromLollipop(Context context) {
        boolean state = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
        }
        return state;
    }

    public boolean isMobDataFlag() {
        return mobDataFlag;
    }

    public void setMobDataFlag(boolean mobDataFlag) {
        this.mobDataFlag = mobDataFlag;
    }

    public String getInstructionTxt() {
        return instructionTxt;
    }

    public String getMobileDataDisableAlert() {
        return mobileDataDisableAlert;
    }

    public Intent getNextIntentActivity() {
        return nextIntentActivity;
    }

    public void setNextIntentActivity(Intent nextIntentActivity) {
        this.nextIntentActivity = nextIntentActivity;
    }
}