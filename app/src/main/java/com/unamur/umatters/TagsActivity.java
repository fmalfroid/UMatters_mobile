package com.unamur.umatters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.unamur.umatters.TagsSetupActivity.selectedItems;

public class TagsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public Spinner spinner_choice_1;
    public Spinner spinner_choice_2;
    public Spinner spinner_choice_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

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

        //Init hashmap and get spinners
        //< spinner number, selected item number >
        selectedItems.put(1,0);
        selectedItems.put(2,1);
        selectedItems.put(3,2);

        spinner_choice_1 = findViewById(R.id.spinner_choice_1);
        spinner_choice_2 = findViewById(R.id.spinner_choice_2);
        spinner_choice_3 = findViewById(R.id.spinner_choice_3);

        //get data to populate the spinners
        List<String> data = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.tags_choices)));

        //Init spinners and their adapters
        List<Spinner> spinners = new ArrayList<>();
        spinners.add(spinner_choice_1);
        spinners.add(spinner_choice_2);
        spinners.add(spinner_choice_3);
        initSpinners(spinners, data);

        ImageView tag_validation_button = findViewById(R.id.ok_btn);
        tag_validation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected tags
                ArrayList<String> selectedTags = new ArrayList<>();
                selectedTags.add(spinner_choice_1.getSelectedItem().toString());
                selectedTags.add(spinner_choice_2.getSelectedItem().toString());
                selectedTags.add(spinner_choice_3.getSelectedItem().toString());
                //Save selected tags into BD
                //TODO : save tags in DB
                Toast.makeText(TagsActivity.this, selectedTags.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent runMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(runMain);
            finish();
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
        navigationView.getMenu().getItem(2).setChecked(true);

        //setup user info
        View headerView =  navigationView.getHeaderView(0);
        ImageView image = (ImageView) headerView.findViewById(R.id.imageView);
        TextView fullname = (TextView)headerView.findViewById(R.id.txt_fullname);
        TextView level = (TextView)headerView.findViewById(R.id.txt_level);

        CurrentUser user = CurrentUser.getCurrentUser();

        //TODO set current user pic in nav drawer

        String str_fullname = user.getFirstname() + " " + user.getLastname();
        fullname.setText(str_fullname);

        String str_level = getResources().getString(R.string.level) + " " + user.getParticipation();
        level.setText(str_level);
    }

    private void initSpinners(List<Spinner> spinners, List<String> data){

        for (final Spinner spinner : spinners){

            //Create the adapter and set it
            XORSpinnerAdapter adapter = new XORSpinnerAdapter(this, spinner, data);
            spinner.setAdapter(adapter);
            //Init the default value for each spinner
            //TODO : init tags value
            if (spinner == spinner_choice_1){
                spinner_choice_1.setSelection(0);
            } else if (spinner == spinner_choice_2){
                spinner_choice_2.setSelection(1);
            } else {
                spinner_choice_3.setSelection(2);
            }

            //For each spinner on item selected :
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Update the selected item of the spinner (saved in the hash map)
                    if (spinner == spinner_choice_1){
                        selectedItems.put(1,position);
                    } else if (spinner == spinner_choice_2){
                        selectedItems.put(2,position);
                    } else {
                        selectedItems.put(3,position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
