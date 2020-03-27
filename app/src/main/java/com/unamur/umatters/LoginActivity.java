package com.unamur.umatters;

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

import com.unamur.umatters.API.CallAPIPost;

public class LoginActivity extends AppCompatActivity {

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
                /*CallAPIPost login = new CallAPIPost();
                login.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/connect", "{\"id\":\"1\", \"password\":\"1234\"}");*/
                Intent runApp = new Intent(getApplicationContext(), TagsSetupActivity.class);
                startActivity(runApp);
                //TODO : Open tags setup if first time, else open main
                finish();
            }
        });

    }
}
