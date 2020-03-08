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
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CreateBoxActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout ll_multiple_choices;
    ImageButton addChoice;

    LinearLayout ll_other_tags;
    ImageButton addOtherTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_box);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drw_notif = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_notifications);
        toolbar.setOverflowIcon(drw_notif);
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

        //Go back button
        ImageView img_arrowback = findViewById(R.id.pop_go_back);
        img_arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Init radiogroup box type
        initBoxTypes();

        //Init multiple choice poll
        initMultipleChoice();

        //Init of the spinner
        SpinnerWrapContent typeTag = (SpinnerWrapContent) findViewById(R.id.typeTag);
        List<String> data = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.typeTags)));
        SpinnerWrapContentAdapter adapter = new SpinnerWrapContentAdapter(this, data);
        typeTag.setAdapter(adapter);

        //Init other tags fields
        initOtherTags();


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

        } else if (id == R.id.nav_subscriptions) {

        } else if (id == R.id.nav_interets) {

        } else if (id == R.id.nav_share) {

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

        final LinearLayout multiple_choice_view = findViewById(R.id.multiple_choice_view);

        RadioButton rdbtn_textual = findViewById(R.id.rdbtn_textual);
        RadioButton rdbtn_yes_no = findViewById(R.id.rdbtn_yes_no);
        RadioButton rdbtn_multiple_choice = findViewById(R.id.rdbtn_multiple_choice);

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
