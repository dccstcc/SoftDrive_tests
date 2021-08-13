package pl.pjatk.softdrive;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Exit extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        IpAddressCtrl.getInstance().getSchedExecutor().shutdownNow();
        finishAndRemoveTask();
    }
}