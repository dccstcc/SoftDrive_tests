package pl.pjatk.softdrive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Exit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finishAffinity();
    }
}