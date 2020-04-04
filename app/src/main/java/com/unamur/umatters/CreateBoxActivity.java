package com.unamur.umatters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.unamur.umatters.API.AddBox;
import com.unamur.umatters.API.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CreateBoxActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout ll_multiple_choices;
    ImageButton addChoice;

    LinearLayout ll_other_tags;
    ImageButton addOtherTag;

    private EditText edtxt_title;
    private EditText edtxt_description;
    private LinearLayout multiple_choice_view;
    private RadioButton rdbtn_textual;
    private RadioButton rdbtn_yes_no;
    private RadioButton rdbtn_multiple_choice;
    private RadioGroup rdgrp_box_type;
    private SpinnerWrapContent typeTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_box);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavDrawer(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent runMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(runMain);
                finish();

            }
        });

        //Init
        ImageView img_arrowback = findViewById(R.id.pop_go_back);
        img_arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ImageView img_ok = findViewById(R.id.ok_btn);
        edtxt_title = findViewById(R.id.edtxt_title);
        edtxt_description = findViewById(R.id.edtxt_description);
        multiple_choice_view = findViewById(R.id.multiple_choice_view);
        rdgrp_box_type = findViewById(R.id.rdgrp_box_type);
        rdbtn_textual = findViewById(R.id.rdbtn_textual);
        rdbtn_yes_no = findViewById(R.id.rdbtn_yes_no);
        rdbtn_multiple_choice = findViewById(R.id.rdbtn_multiple_choice);


        //Init radiogroup box type
        initBoxTypes();

        //Init multiple choice poll
        initMultipleChoice();

        //Init of the spinner
        typeTag = (SpinnerWrapContent) findViewById(R.id.spinner_main_tag);
        List<String> data = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.typeTags)));
        SpinnerWrapContentAdapter adapter = new SpinnerWrapContentAdapter(this, data);
        typeTag.setAdapter(adapter);

        //Init other tags fields
        initOtherTags();

        //Create box
        img_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check entered fields
                boolean everythingOK = checkBoxInput();
                //If everything ok than create box
                if (everythingOK){

                    //Get tags
                    JSONArray tag_array = new JSONArray();
                    String main_tag = typeTag.getSelectedItem().toString();
                    tag_array.put(main_tag);

                    //Get other tag
                    int nbr_tags = ll_other_tags.getChildCount();
                    EditText tag1 = findViewById(R.id.optional_tag_1);
                    boolean FirstOptTagAtLeastOne = tag1.getText().toString().matches(".*[a-zA-Z0-9]+.*");
                    if (FirstOptTagAtLeastOne){
                        tag_array.put(tag1.getText().toString());
                    }

                    for (int i=0; i<nbr_tags; i++) {
                        if (ll_other_tags.getChildAt(i) instanceof LinearLayout) {
                            LinearLayout ll_tag = (LinearLayout) ll_other_tags.getChildAt(i);
                            EditText edtxt_optional_tag = (EditText) ll_tag.getChildAt(0);
                            boolean atLeastOne = edtxt_optional_tag.getText().toString().matches(".*[a-zA-Z0-9]+.*");
                            if (atLeastOne){
                                tag_array.put(edtxt_optional_tag.getText().toString());
                            }
                        }
                    }

                    //Get box type and choices
                    JSONObject choicesJSON = null;
                    JSONArray empty_choice = new JSONArray();
                    empty_choice.put("");
                    String type = "";
                    switch (rdgrp_box_type.getCheckedRadioButtonId()){
                        case R.id.rdbtn_textual:
                            type = "textuelle";
                            break;
                        case R.id.rdbtn_yes_no:
                            type = "oui_non";
                            choicesJSON = new JSONObject();
                            try {
                                choicesJSON.put("oui", empty_choice);
                                choicesJSON.put("non", empty_choice);
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                            break;

                        case R.id.rdbtn_multiple_choice:
                            type = "choix_multiple";
                            choicesJSON = new JSONObject();
                            //Get 2 first choices
                            EditText choice1 = findViewById(R.id.first_choice);
                            EditText choice2 = findViewById(R.id.second_choice);
                            try {
                                choicesJSON.put(choice1.getText().toString(), empty_choice);
                                choicesJSON.put(choice2.getText().toString(), empty_choice);
                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                            //Get other choices
                            int nbr_choice = ll_multiple_choices.getChildCount();
                            for (int i=0; i<nbr_choice; i++) {
                                if (ll_multiple_choices.getChildAt(i) instanceof LinearLayout) {
                                    LinearLayout ll_choice = (LinearLayout) ll_multiple_choices.getChildAt(i);
                                    EditText edtxt_choice = (EditText) ll_choice.getChildAt(0);

                                    String choice_text = edtxt_choice.getText().toString();
                                    try {
                                        choicesJSON.put(choice_text, empty_choice);
                                    } catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;
                    }

                    //Get createur
                    JSONObject userJSON = new JSONObject();
                    CurrentUser user = CurrentUser.getCurrentUser();
                    String email = user.getEmail();
                    String firstname = user.getFirstname();
                    String lastname = user.getLastname();
                    String role = user.getRole();
                    try{
                        userJSON.put("email", email);
                        userJSON.put("firstname", firstname);
                        userJSON.put("lastname", lastname);
                        userJSON.put("role", role);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    JSONObject createBoxJson = new JSONObject();
                    try {
                        createBoxJson.put("titre", edtxt_title.getText().toString());
                        createBoxJson.put("description", edtxt_description.getText().toString());
                        createBoxJson.put("tag", tag_array);
                        createBoxJson.put("type", type);
                        createBoxJson.put("createur", userJSON);
                        createBoxJson.put("choix", choicesJSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println(createBoxJson);

                    AddBox addBox = new AddBox(CreateBoxActivity.this);
                    addBox.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box", String.valueOf(createBoxJson));

                }
            }
        });
    }

    private boolean checkBoxInput(){

        //Check title
        String title = edtxt_title.getText().toString();
        boolean titleAtLeastOne = title.matches(".*[a-zA-Z0-9]+.*");
        //--Field is empty or only spaces
        if (!(titleAtLeastOne && title.length() != 0)) {
            Toast.makeText(CreateBoxActivity.this, R.string.error_box_empty_title, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (title.length() < 5){
            Toast.makeText(CreateBoxActivity.this, R.string.error_box_too_small_title, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Check description
        String description = edtxt_description.getText().toString();
        boolean descriptionAtLeastOne = description.matches(".*[a-zA-Z0-9]+.*");
        //--Field is empty or only spaces
        if (!(descriptionAtLeastOne && description.length() != 0)) {
            Toast.makeText(CreateBoxActivity.this, R.string.error_box_empty_description, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.length() < 10){
            Toast.makeText(CreateBoxActivity.this, R.string.error_box_too_small_description, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Check box type
        //--If box type is multiple choice, at least 2 choice have to be specified.
        if (rdgrp_box_type.getCheckedRadioButtonId() == R.id.rdbtn_multiple_choice){
            int nbr_choice = ll_multiple_choices.getChildCount();

            //Verifier que les 2 premiers choix sont remplis
            EditText choice1 = findViewById(R.id.first_choice);
            EditText choice2 = findViewById(R.id.second_choice);
            boolean choice1AtLeastOne = choice1.getText().toString().matches(".*[a-zA-Z0-9]+.*");
            boolean choice2AtLeastOne = choice2.getText().toString().matches(".*[a-zA-Z0-9]+.*");
            if (!(choice1AtLeastOne && choice2AtLeastOne && choice1.length()!= 0 && choice2.length()!=0)){
                Toast.makeText(CreateBoxActivity.this, R.string.error_at_least_2_multiple_choice, Toast.LENGTH_SHORT).show();
                return false;
            }

            //Pour chaque edittext choix ajouté en plus, vérifier s'ils ne sont pas vide.
            //Bon là compliqué vu qu'ils n'ont pas d'id... Mais j'ai leur nombre que j'ai récupérer plus haut.
            for (int i=0; i<nbr_choice; i++) {
                if (ll_multiple_choices.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout ll_choice = (LinearLayout) ll_multiple_choices.getChildAt(i);
                    EditText edtxt_choice = (EditText) ll_choice.getChildAt(0);

                    String choice = edtxt_choice.getText().toString();
                    boolean choiceAtLeastOne = choice.matches(".*[a-zA-Z0-9]+.*");
                    //--Field is empty or only spaces
                    if (!(choiceAtLeastOne && choice.length() != 0)) {
                        Toast.makeText(CreateBoxActivity.this, R.string.error_box_choice_empty, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

        }

        //Check main tag
        String type_tag = typeTag.getSelectedItem().toString();
        boolean tagAtLeastOne = type_tag.matches(".*[a-zA-Z0-9]+.*");
        //--Field is empty or only spaces
        if (!(tagAtLeastOne && type_tag.length() != 0)) {
            Toast.makeText(CreateBoxActivity.this, R.string.error_type_tag_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Open notification activity
        if (id == R.id.action_notifications) {

            Intent runNotifications = new Intent(getApplicationContext(), NotificationsActivity.class);
            startActivity(runNotifications);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id==R.id.nav_home){

            Intent runMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(runMain);
            finish();

        } else if (id == R.id.nav_profile) {

            Intent runProfile = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(runProfile);
            finish();

        } else if (id == R.id.nav_tags) {

            Intent runMain = new Intent(getApplicationContext(), TagsActivity.class);
            startActivity(runMain);
            finish();

        } else if (id == R.id.nav_subscriptions) {

            Intent runMain = new Intent(getApplicationContext(), SubscriptionsActivity.class);
            startActivity(runMain);
            finish();

        } else if (id == R.id.nav_interets) {

            Intent runMain = new Intent(getApplicationContext(), InterestActivity.class);
            startActivity(runMain);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initNavDrawer(Toolbar toolbar){
        //Init navigation drawer
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Setup nav drawer system
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initBoxTypes(){

        //Textual
        rdbtn_textual.setChecked(true);
        rdbtn_textual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiple_choice_view.setVisibility(View.GONE);
            }
        });

        //Yes-no
        rdbtn_yes_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiple_choice_view.setVisibility(View.GONE);
            }
        });

        //Multiple choice
        rdbtn_multiple_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiple_choice_view.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initMultipleChoice(){
        ll_multiple_choices = findViewById(R.id.ll_multiple_choices);
        addChoice = findViewById(R.id.imgbtn_add_choice);

        EditText first_choice = findViewById(R.id.first_choice);
        first_choice.setHint(getString(R.string.choice) + " 1");
        EditText second_choice = findViewById(R.id.second_choice);
        second_choice.setHint(getString(R.string.choice) + " 2");

        addChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If there is less than 5 choice, you can create a new field
                if (ll_multiple_choices.getChildCount() < 8){

                    final LinearLayout ll_choice = new LinearLayout(CreateBoxActivity.this);
                    ll_choice.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll_choice.setOrientation(LinearLayout.HORIZONTAL);

                    EditText new_edit_txt = new EditText(CreateBoxActivity.this);
                    new_edit_txt.setHint(getString(R.string.new_choice));
                    new_edit_txt.setTextSize(15);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                    new_edit_txt.setLayoutParams(params);

                    ImageView img_remove = new ImageView(CreateBoxActivity.this);
                    LinearLayout.LayoutParams params_img = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params_img.gravity = Gravity.CENTER;
                    img_remove.setLayoutParams(params_img);
                    img_remove.setImageResource(R.drawable.ic_remove);

                    img_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ll_multiple_choices.removeView(ll_choice);
                        }
                    });

                    ll_choice.addView(new_edit_txt);
                    ll_choice.addView(img_remove);
                    ll_multiple_choices.addView(ll_choice);
                }
                //Else tell the user he has too many choices
                else {
                    Toast.makeText(CreateBoxActivity.this, R.string.tooManyChoices, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initOtherTags(){
        ll_other_tags = findViewById(R.id.ll_other_tags);
        addOtherTag = findViewById(R.id.imgbtn_add_other_tag);
        addOtherTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If there is less than 5 other tag, you can create a new field
                if (ll_other_tags.getChildCount() < 5){

                    final LinearLayout ll_field = new LinearLayout(CreateBoxActivity.this);
                    ll_field.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll_field.setOrientation(LinearLayout.HORIZONTAL);

                    EditText new_edit_txt = new EditText(CreateBoxActivity.this);
                    new_edit_txt.setHint("#tag");
                    new_edit_txt.setTextSize(15);
                    //--set max length to 15
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(15);
                    new_edit_txt.setFilters(filterArray);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                    //Get px value of dp
                    Resources r = CreateBoxActivity.this.getResources();
                    int px = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            10,
                            r.getDisplayMetrics()
                    );
                    params.setMargins(px,0,0,0);
                    new_edit_txt.setLayoutParams(params);


                    ImageView img_remove = new ImageView(CreateBoxActivity.this);
                    LinearLayout.LayoutParams params_img = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params_img.gravity = Gravity.CENTER;
                    img_remove.setLayoutParams(params_img);
                    img_remove.setImageResource(R.drawable.ic_remove);

                    img_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ll_other_tags.removeView(ll_field);
                        }
                    });

                    ll_field.addView(new_edit_txt);
                    ll_field.addView(img_remove);
                    ll_other_tags.addView(ll_field);
                }
                //Else tell the user he has too many tags
                else {
                    Toast.makeText(CreateBoxActivity.this, R.string.tooManyOtherTags, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
