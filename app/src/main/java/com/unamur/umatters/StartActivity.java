package com.unamur.umatters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity {

    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        //Set the time before fading
        int SPLASH_TIME_OUT = 1500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "StartActivity: Launch screen");
                //Run the app
                Intent runApp = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(runApp);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}
