package com.serialmmf.Anbattery;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


import java.util.HashMap;


public class SplashActivity extends Activity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // ---------------------- ANALYTICS ---------------------


        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < Integer.parseInt(getString(R.string.splash_delay))) {
                        sleep(100);
                        waited += 100;
                    }

                } catch (InterruptedException e) {
                    // do nothing
                } finally {

                    finish();

                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);


                }
            }
        };
        splashThread.start();


    }


}