package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unamur.umatters.API.APIKeys;
import com.unamur.umatters.API.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private FloatingActionButton loginButton;
    private EditText email;
    private EditText password;
    private Button btn_register;
    private SwitchCompat remember_me_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_activity_email);
        password = findViewById(R.id.login_activity_password);

        remember_me_btn = findViewById(R.id.login_activity_rememberme);

        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(email.getText().toString(), password.getText().toString());

                if (remember_me_btn.isChecked()) {
                    SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.saved_email_key), email.getText().toString());
                    editor.putString(getString(R.string.saved_password_key), password.getText().toString());
                    editor.commit();
                }

            }
        });

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent runRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(runRegister);
            }
        });
    }

    public void login(String email, String password) {
        JSONObject loginJson = new JSONObject();
        try {
            loginJson.put("email", email);
            loginJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CurrentUser user = CurrentUser.getCurrentUser();
        user.setEmail(email);
        user.setPassword(password);

        Login login = new Login(LoginActivity.this);
        login.execute(APIKeys.getUrl() + "users/connect", String.valueOf(loginJson));
    }
}
