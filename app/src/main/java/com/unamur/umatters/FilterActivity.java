package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.FilterBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RadioGroup rdgrp_sort;
    private CheckBox tag_general;
    private CheckBox tag_info;
    private CheckBox tag_medecine;
    private CheckBox tag_eco;
    private CheckBox tag_age;
    private CheckBox tag_droit;
    private CheckBox tag_sciences;
    private CheckBox tag_philo;

    private CheckBox type_textuelle;
    private CheckBox type_oui_non;
    private CheckBox type_choix_multiples;

    private CheckBox role_student;
    private CheckBox role_academic;
    private CheckBox role_atg;
    private CheckBox role_sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

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

        //Go back button
        ImageView img_arrowback = findViewById(R.id.pop_go_back);
        img_arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Init radiobutton and checkbox
        init();

        //filter button
        ImageView img_filter = findViewById(R.id.ok_btn);
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get checked buttons
                //--sort

                String sort;
                switch (rdgrp_sort.getCheckedRadioButtonId()){
                    case R.id.rdbtn_oldest:
                        sort = "plus_ancien";
                        break;
                    case R.id.rdbtn_popularity:
                        sort = "plus_populaire";
                        break;
                    case R.id.rdbtn_participation_level:
                        sort = "plus_haut_niveau";
                        break;
                    default:
                        sort = "plus_recent";
                }
                //--tag
                ArrayList<String> tag_list = new ArrayList<>();

                ArrayList<CheckBox> tag_checkbox = new ArrayList<>();
                tag_checkbox.add(tag_general);
                tag_checkbox.add(tag_info);
                tag_checkbox.add(tag_medecine);
                tag_checkbox.add(tag_eco);
                tag_checkbox.add(tag_age);
                tag_checkbox.add(tag_droit);
                tag_checkbox.add(tag_sciences);
                tag_checkbox.add(tag_philo);
                for (CheckBox checkbox : tag_checkbox) {
                    if (checkbox.isChecked()){
                        tag_list.add((String)checkbox.getTag());
                    }
                }

                //--type
                ArrayList<String> type_list = new ArrayList<>();

                ArrayList<CheckBox> type_checkbox = new ArrayList<>();
                type_checkbox.add(type_textuelle);
                type_checkbox.add(type_oui_non);
                type_checkbox.add(type_choix_multiples);
                for (CheckBox checkbox : type_checkbox) {
                    if (checkbox.isChecked()){
                        type_list.add((String)checkbox.getTag());
                    }
                }

                //--role
                ArrayList<String> role_list = new ArrayList<>();

                ArrayList<CheckBox> role_checkbox = new ArrayList<>();
                role_checkbox.add(role_student);
                role_checkbox.add(role_academic);
                role_checkbox.add(role_atg);
                role_checkbox.add(role_sc);
                for (CheckBox checkbox : role_checkbox) {
                    if (checkbox.isChecked()){
                        role_list.add((String)checkbox.getTag());
                    }
                }

                //change value in main activity
                MainActivity.filter_sort = sort;
                if (tag_list.isEmpty()){
                    MainActivity.filter_tag_list = null;
                } else {
                    MainActivity.filter_tag_list = tag_list;
                }
                if (type_list.isEmpty()){
                    MainActivity.filter_type_list = null;
                } else {
                    MainActivity.filter_type_list = type_list;
                }
                if (role_list.isEmpty()){
                    MainActivity.filter_role_list = null;
                } else {
                    MainActivity.filter_role_list = role_list;
                }

                //Close activity
                Intent runMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(runMain);
                finish();
            }
        });


        //Reset button
        Button btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sort
                rdgrp_sort.check(R.id.rdbtn_lastest);
                //tag
                tag_general.setChecked(false);
                tag_info.setChecked(false);
                tag_medecine.setChecked(false);
                tag_eco.setChecked(false);
                tag_age.setChecked(false);
                tag_droit.setChecked(false);
                tag_sciences.setChecked(false);
                tag_philo.setChecked(false);
                //type
                type_textuelle.setChecked(false);
                type_oui_non.setChecked(false);
                type_choix_multiples.setChecked(false);
                //role
                role_student.setChecked(false);
                role_academic.setChecked(false);
                role_atg.setChecked(false);
                role_sc.setChecked(false);
            }
        });

        //Pref tags button
        Button btn_pref_tags = findViewById(R.id.btn_pref_tags);
        btn_pref_tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> pref_tags_list = CurrentUser.getCurrentUser().getTag_pref();
                if (pref_tags_list!=null){
                    for (String tag: pref_tags_list) {
                        switch (tag){
                            case "#Informatique":
                                tag_info.setChecked(true);
                                break;
                            case "#Droit":
                                tag_droit.setChecked(true);
                                break;
                            case "#Médecine":
                                tag_medecine.setChecked(true);
                                break;
                            case "#Sciences":
                                tag_sciences.setChecked(true);
                                break;
                            case "#Economie":
                                tag_eco.setChecked(true);
                                break;
                            case "#Philo&Lettres":
                                tag_philo.setChecked(true);
                                break;
                            case "#AGE":
                                tag_age.setChecked(true);
                                break;
                            case "#Général":
                                tag_general.setChecked(true);
                                break;
                        }
                    }
                }
            }
        });

    }

    public void init(){

        rdgrp_sort = findViewById(R.id.rdgrp_sort);
        tag_general = findViewById(R.id.tag_general);
        tag_info = findViewById(R.id.tag_informatique);
        tag_medecine = findViewById(R.id.tag_medecine);
        tag_eco = findViewById(R.id.tag_economie);
        tag_age = findViewById(R.id.tag_age);
        tag_droit = findViewById(R.id.tag_droit);
        tag_sciences = findViewById(R.id.tag_sciences);
        tag_philo = findViewById(R.id.tag_philo);

        type_textuelle = findViewById(R.id.type_textuelle);
        type_oui_non = findViewById(R.id.type_oui_non);
        type_choix_multiples = findViewById(R.id.type_choix_multiples);

        role_student = findViewById(R.id.role_student);
        role_academic = findViewById(R.id.role_academic);
        role_atg = findViewById(R.id.role_atg);
        role_sc = findViewById(R.id.role_sc);

        //--init sort
        switch (MainActivity.filter_sort){
            case "plus_ancien":
                rdgrp_sort.check(R.id.rdbtn_oldest);
                break;
            case "plus_populaire":
                rdgrp_sort.check(R.id.rdbtn_popularity);
                break;
            case "plus_haut_niveau":
                rdgrp_sort.check(R.id.rdbtn_participation_level);
                break;
            default :
                rdgrp_sort.check(R.id.rdbtn_lastest);
                break;
        }
        //--init checkbox tag
        if (MainActivity.filter_tag_list!=null){
            for (String tag: MainActivity.filter_tag_list) {
                switch (tag){
                    case "#Informatique":
                        tag_info.setChecked(true);
                        break;
                    case "#Droit":
                        tag_droit.setChecked(true);
                        break;
                    case "#Médecine":
                        tag_medecine.setChecked(true);
                        break;
                    case "#Sciences":
                        tag_sciences.setChecked(true);
                        break;
                    case "#Economie":
                        tag_eco.setChecked(true);
                        break;
                    case "#Philo&Lettres":
                        tag_philo.setChecked(true);
                        break;
                    case "#AGE":
                        tag_age.setChecked(true);
                        break;
                    case "#Général":
                        tag_general.setChecked(true);
                        break;
                }
            }
        }

        //--init checkbox type
        if (MainActivity.filter_type_list!=null) {
            for (String type : MainActivity.filter_type_list) {
                switch (type) {
                    case "oui_non":
                        type_oui_non.setChecked(true);
                        break;
                    case "choix_multiple":
                        type_choix_multiples.setChecked(true);
                        break;
                    case "textuelle":
                        type_textuelle.setChecked(true);
                        break;
                }
            }
        }
        //--init checkbox role
        if (MainActivity.filter_role_list!=null) {
            for (String role : MainActivity.filter_role_list) {
                switch (role) {
                    case "Académique":
                        role_academic.setChecked(true);
                        break;
                    case "Scientifique":
                        role_sc.setChecked(true);
                        break;
                    case "ATG":
                        role_atg.setChecked(true);
                        break;
                    case "Etudiant":
                        role_student.setChecked(true);
                        break;
                }
            }
        }
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

        if (id==R.id.action_help){
            openHelpDialog(FilterActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openHelpDialog(Context context){

        //open info dialog
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_help, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        Button btn_cancel = mView.findViewById(R.id.btn_cancel);

        ToggleButton box = mView.findViewById(R.id.btn_box);
        ToggleButton tags = mView.findViewById(R.id.btn_tags);
        ToggleButton populars = mView.findViewById(R.id.btn_populars);
        ToggleButton archives = mView.findViewById(R.id.btn_archives);
        ToggleButton subscriptions = mView.findViewById(R.id.btn_subscriptions);
        ToggleButton interests = mView.findViewById(R.id.btn_interests);

        final LinearLayout layout_box = mView.findViewById(R.id.layout_box);
        final LinearLayout layout_tags = mView.findViewById(R.id.layout_tags);
        final LinearLayout layout_populars = mView.findViewById(R.id.layout_populars);
        final LinearLayout layout_archives = mView.findViewById(R.id.layout_archives);
        final LinearLayout layout_subscriptions = mView.findViewById(R.id.layout_subscriptions);
        final LinearLayout layout_interests = mView.findViewById(R.id.layout_interests);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout_box.getVisibility()==View.VISIBLE){
                    layout_box.setVisibility(View.GONE);
                } else {
                    layout_box.setVisibility(View.VISIBLE);
                }
            }
        });

        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout_tags.getVisibility()==View.VISIBLE){
                    layout_tags.setVisibility(View.GONE);
                } else {
                    layout_tags.setVisibility(View.VISIBLE);
                }
            }
        });


        populars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout_populars.getVisibility()==View.VISIBLE){
                    layout_populars.setVisibility(View.GONE);
                } else {
                    layout_populars.setVisibility(View.VISIBLE);
                }
            }
        });


        archives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout_archives.getVisibility()==View.VISIBLE){
                    layout_archives.setVisibility(View.GONE);
                } else {
                    layout_archives.setVisibility(View.VISIBLE);
                }
            }
        });


        subscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout_subscriptions.getVisibility()==View.VISIBLE){
                    layout_subscriptions.setVisibility(View.GONE);
                } else {
                    layout_subscriptions.setVisibility(View.VISIBLE);
                }
            }
        });


        interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout_interests.getVisibility()==View.VISIBLE){
                    layout_interests.setVisibility(View.GONE);
                } else {
                    layout_interests.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
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

        } else if (id == R.id.nav_deconnexion) {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_email_key), null);
            editor.putString(getString(R.string.saved_password_key), null);
            editor.commit();

            Intent runMain = new Intent(getApplicationContext(), LoginActivity.class);
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

        //setup user info
        View headerView =  navigationView.getHeaderView(0);
        ImageView image = (ImageView) headerView.findViewById(R.id.imageView);
        TextView fullname = (TextView)headerView.findViewById(R.id.txt_fullname);
        TextView level = (TextView)headerView.findViewById(R.id.txt_level);

        CurrentUser user = CurrentUser.getCurrentUser();

        String str_fullname = user.getFirstname() + " " + user.getLastname();
        fullname.setText(str_fullname);
        image.setImageBitmap(user.getImage());

        String str_level = getResources().getString(R.string.level) + " " + (user.getParticipation()/5);
        level.setText(str_level);
    }
}
