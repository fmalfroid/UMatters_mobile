package com.unamur.umatters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LoginActivity extends Activity {

    private FloatingActionButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent runApp;

                //If the previouslyStarted preference is already initialised then launch the MainActivity.
                //If it isn't, it is the first time opening the app, launch the tags setup activity.
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
                //App started for the first time
                if(!previouslyStarted) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                    edit.apply();
                    runApp = new Intent(getApplicationContext(), TagsSetupActivity.class);
                }
                //Not the first time
                else {
                    runApp = new Intent(getApplicationContext(), MainActivity.class);
                }

                startActivity(runApp);
                //finish();
            }
        });

    }
}
