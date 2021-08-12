package pl.pjatk.softdrive.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;

import pl.pjatk.softdrive.R;

public class MainViewActivity extends AppCompatActivity {

    ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        executor = Executors.newCachedThreadPool();
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                setContentView(R.layout.activity_main_view);
//            }
//        });

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                setContentView(R.layout.activity_main_view);

                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();

    }


}