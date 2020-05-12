package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.unamur.umatters.API.APIKeys;
import com.unamur.umatters.API.GetUser;
import com.unamur.umatters.API.GetUserAllBox;

public class UsersProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Init of the recyclerView
        BoxListAdapterUsersProfile adapter = new BoxListAdapterUsersProfile();
        final RecyclerView rv = findViewById(R.id.profle_box_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

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

        //get user email
        String user_email = (String) getIntent().getSerializableExtra("user_email");

        //Get user info
        GetUser task = new GetUser(UsersProfileActivity.this, adapter);
        task.execute(APIKeys.getUrl() + "users/" + user_email);

        //Get all box from user
        GetUserAllBox task2 = new GetUserAllBox(UsersProfileActivity.this, adapter);
        task2.execute(APIKeys.getUrl() + "box/user/" + user_email);
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
            openHelpDialog(UsersProfileActivity.this);
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
