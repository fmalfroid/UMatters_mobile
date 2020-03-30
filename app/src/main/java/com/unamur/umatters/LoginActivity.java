package com.unamur.umatters;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import com.unamur.umatters.API.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private FloatingActionButton loginButton;
    private EditText email;
    private EditText password;
    private TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_activity_email);
        password = findViewById(R.id.login_activity_password);
        loginError = findViewById(R.id.login_error);
        loginError.setVisibility(View.INVISIBLE);

        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject loginJson = new JSONObject();
                try {
                    loginJson.put("email", email.getText().toString());
                    loginJson.put("password", password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Login login = new Login(LoginActivity.this);
                login.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/connect", String.valueOf(loginJson));

                //Décommenter pour retirer le login

                //Intent runApp = new Intent(getApplicationContext(), TagsSetupActivity.class);
                //startActivity(runApp);
                //TODO : Open tags setup if first time, else open main
                //finish();
            }
        });

    }
}
