package com.unamur.umatters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.unamur.umatters.API.Register;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_register;

    private EditText edtxt_email;
    private EditText edtxt_password;
    private EditText edtxt_password_confirm;
    private EditText edtxt_firstname;
    private EditText edtxt_lastname;
    private Spinner spinner_role;

    private String email;
    private String password;
    private String password_confirmation;
    private String firstname;
    private String lastname;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = findViewById(R.id.btn_register);

        //Get infos from form
        edtxt_email = findViewById(R.id.edtxt_email_adress);
        edtxt_password = findViewById(R.id.edtxt_password);
        edtxt_password_confirm = findViewById(R.id.edtxt_password_confirmation);
        edtxt_firstname = findViewById(R.id.edtxt_firstname);
        edtxt_lastname = findViewById(R.id.edtxt_lastname);
        spinner_role = findViewById(R.id.spinner_role);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check info
                boolean everything_ok = checkInput();

                //Get selected role
                role = spinner_role.getSelectedItem().toString();
                switch (role){
                    case "Student":
                        role = "Etudiant";
                        break;
                    case "Academic":
                        role = "Académique";
                        break;
                    case "Scientist":
                        role = "Scientifique";
                        break;
                }

                //If everything ok
                if (everything_ok){

                    String register_infos = "" + "\nEmail : " + email + "\nPassword : " + password + "\nConfirmation password : " + password_confirmation + "\nFirstname : " + firstname + "\nLastname : " + lastname;
                    Log.d("RegisterActivity", "Register infos: " + register_infos);

                    //register the user
                    JSONObject registerJson = new JSONObject();
                    try {
                        registerJson.put("email", email);
                        registerJson.put("firstname", firstname);
                        registerJson.put("lastname", lastname);
                        registerJson.put("password", password);
                        registerJson.put("role", role);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    Register register = new Register(RegisterActivity.this);
                    register.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/register", String.valueOf(registerJson));
                    //return to login activity
                    finish();
                }
            }
        });
    }

    public boolean checkInput(){

        email = edtxt_email.getText().toString();
        password = edtxt_password.getText().toString();
        password_confirmation = edtxt_password_confirm.getText().toString();
        firstname = edtxt_firstname.getText().toString();
        lastname = edtxt_lastname.getText().toString();

        //email
        boolean email_characters = email.matches("^[a-z]+[.]+[a-z]+@(student.)?unamur.be");
        if (!email_characters){
            Toast.makeText(RegisterActivity.this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.length()==0){
            Toast.makeText(RegisterActivity.this, R.string.error_empty_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        //password
        //check for at least one character and not empty
        if (password.trim().length()==0){
            Toast.makeText(RegisterActivity.this, R.string.error_empty_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password_confirmation.trim().length()==0){
            Toast.makeText(RegisterActivity.this, R.string.error_empty_confirm_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(password_confirmation)){
            Toast.makeText(RegisterActivity.this, R.string.error_difference_password_confirm_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6){
            Toast.makeText(RegisterActivity.this, R.string.error_min_length_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        //firstname
        if (firstname.trim().length()==0){
            Toast.makeText(RegisterActivity.this, R.string.error_empty_firstname, Toast.LENGTH_SHORT).show();
            return false;
        }

        //lastname
        if (lastname.trim().length()==0){
            Toast.makeText(RegisterActivity.this, R.string.error_empty_lastname, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
