package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.APIKeys;
import com.unamur.umatters.API.ChangePwd;
import com.unamur.umatters.API.FilterBox;
import com.unamur.umatters.API.GetAllBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout btn_filter;
    private ImageView btn_popular;
    private ImageView btn_archives;
    private EditText search_input;
    private BoxListAdapter adapter;

    public static String filter_sort = "plus_recent";
    public static ArrayList<String> filter_tag_list = CurrentUser.getCurrentUser().getTag_pref();
    public static ArrayList<String> filter_type_list = null;
    public static ArrayList<String> filter_role_list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Init
        initFAB();
        initNavDrawer(toolbar);
        adapter = new BoxListAdapter();
        initRecyclerView(adapter);

        //Popular button
        btn_popular = findViewById(R.id.btn_popular);
        btn_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent runApp = new Intent(getApplicationContext(), PopularActivity.class);
                startActivity(runApp);
            }
        });

        //Filter button
        btn_filter = findViewById(R.id.btn_filter);
        TextView activate_icon = findViewById(R.id.activate_icon);
        //--setup activate state
        if (filter_sort.equals("plus_recent") && filter_tag_list==null && filter_type_list==null && filter_role_list==null){
            activate_icon.setVisibility(View.GONE);
        } else{
            activate_icon.setVisibility(View.VISIBLE);
        }
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent runApp = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(runApp);
            }
        });

        //Archives button
        btn_archives = findViewById(R.id.btn_archive);
        btn_archives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent runApp = new Intent(getApplicationContext(), ArchivesActivity.class);
                startActivity(runApp);
            }
        });

        //Search input
        search_input = findViewById(R.id.search_text);
        search_input.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchAndFilter(search_input.getText().toString());
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        searchAndFilter(search_input.getText().toString());
    }

    public void searchAndFilter(String search_text) {
        adapter.removeAllData();
        //get data
        //call api
        JSONObject filter_jobject = new JSONObject();
        try  {
            filter_jobject.put("tri", filter_sort);

            if (filter_tag_list==null){
                filter_jobject.put("tag", null);
            } else {
                JSONArray tag_array = new JSONArray();
                for (String tag:filter_tag_list) {
                    tag_array.put(tag);
                }
                filter_jobject.put("tag", tag_array);
            }

            if (filter_type_list==null){
                filter_jobject.put("type", null);
            } else {
                JSONArray type_array = new JSONArray();
                for (String type:filter_type_list) {
                    type_array.put(type);
                }
                filter_jobject.put("type", type_array);
            }

            if (filter_role_list==null){
                filter_jobject.put("role", null);
            } else {
                JSONArray role_array = new JSONArray();
                for (String role:filter_role_list) {
                    role_array.put(role);
                }
                filter_jobject.put("role", role_array);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        FilterBox task = new FilterBox( MainActivity.this, adapter, search_text);
        task.execute(APIKeys.getUrl() + "box/filtrer", String.valueOf(filter_jobject));

        Log.d("MainActivity box ", filter_jobject.toString());
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
            openHelpDialog(MainActivity.this);
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

        if (id == R.id.nav_profile) {

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

    private void initFAB(){
        //Floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent runApp = new Intent(getApplicationContext(), CreateBoxActivity.class);
                startActivity(runApp);
            }
        });
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
        navigationView.getMenu().getItem(0).setChecked(true);

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

    private void initRecyclerView(BoxListAdapter adapter){

        final RecyclerView rv = findViewById(R.id.box_list);

        //Init the layout and the adapter of the recycler view
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        final FloatingActionButton fab = findViewById(R.id.fab);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                try {
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();

                    if(position==0){
                        fab.show();
                    } else {
                        fab.hide();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}
