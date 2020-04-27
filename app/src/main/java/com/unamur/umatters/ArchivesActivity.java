package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unamur.umatters.API.GetArchives;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArchivesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String current_tab = "Accepté";
    private SpinnerWrapContent spn_council_choice;

    public static TextView msg_no_archives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);

        ArchivesAcceptedFragment.adapter.clearArchivesList();
        ArchivesPendingFragment.adapter.clearArchivesList();
        ArchivesRefusedFragment.adapter.clearArchivesList();

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

        //go back
        ImageView go_back = findViewById(R.id.pop_go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Init of the spinner
        spn_council_choice = (SpinnerWrapContent) findViewById(R.id.council_choice);
        List<String> data = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.council_choices)));
        SpinnerWrapContentAdapter spn_adapter = new SpinnerWrapContentAdapter(ArchivesActivity.this, data);
        spn_council_choice.setAdapter(spn_adapter);
        spn_council_choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getArchives(getTagFromSpinner(), current_tab);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //Init tabs
        initTabs();

        //init msg no archive visibility
        msg_no_archives = findViewById(R.id.txt_no_archives);
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

    public void initTabs(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.archives_accepted));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.archives_pending));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.archives_refused));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ArchivesPagerAdapter adapter = new ArchivesPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()){
                    case 0:
                        current_tab = "Accepté";
                        break;
                    case 1:
                        current_tab = "En suspend";
                        break;
                    case 2:
                        current_tab = "Refusé";
                        break;
                }

                getArchives(getTagFromSpinner(), current_tab);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public String getTagFromSpinner(){
        String selected_item = spn_council_choice.getSelectedItem().toString();
        String tag;

        switch (selected_item){
            case "Informatique":
                tag = "#Informatique";
                break;
            case "Droit":
                tag = "#Droit";
                break;
            case "Médecine":
                tag = "#Médecine";
                break;
            case "Sciences":
                tag = "#Sciences";
                break;
            case "Économie":
                tag = "#Economie";
                break;
            case "Philosophie et lettres":
                tag = "#Philo&Lettres";
                break;
            case "Chambre politique":
                tag = "#AGE";
                break;
            default:
                tag = "#Général";
                break;
        }

        return tag;
    }

    public void getArchives(String tag, String status){

        JSONObject archives_jsonObj = new JSONObject();
        try {
            archives_jsonObj.put("tag", tag);
            archives_jsonObj.put("status", status);
        } catch (JSONException e){
            e.printStackTrace();
        }

        switch (status){
            case "Accepté":
                GetArchives task = new GetArchives(ArchivesActivity.this, ArchivesAcceptedFragment.adapter);
                task.execute("http://mdl-std01.info.fundp.ac.be/api/v1/archives/filtrer", String.valueOf(archives_jsonObj));
                ArchivesPendingFragment.adapter.clearArchivesList();
                ArchivesRefusedFragment.adapter.clearArchivesList();

                break;
            case "En suspend":
                GetArchives task2 = new GetArchives(ArchivesActivity.this, ArchivesPendingFragment.adapter);
                task2.execute("http://mdl-std01.info.fundp.ac.be/api/v1/archives/filtrer", String.valueOf(archives_jsonObj));
                ArchivesAcceptedFragment.adapter.clearArchivesList();
                ArchivesRefusedFragment.adapter.clearArchivesList();

                break;
            case "Refusé":
                GetArchives task3 = new GetArchives(ArchivesActivity.this, ArchivesRefusedFragment.adapter);
                task3.execute("http://mdl-std01.info.fundp.ac.be/api/v1/archives/filtrer", String.valueOf(archives_jsonObj));
                ArchivesAcceptedFragment.adapter.clearArchivesList();
                ArchivesPendingFragment.adapter.clearArchivesList();

                break;
        }

        Log.d("ArchivesActivity", tag + " " + status);
    }
}
