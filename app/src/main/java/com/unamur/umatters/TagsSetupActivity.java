package com.unamur.umatters;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.unamur.umatters.API.SetPrefTags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TagsSetupActivity extends AppCompatActivity {

    public static HashMap<Integer, Integer> selectedItems = new HashMap<>();
    public Spinner spinner_choice_1;
    public Spinner spinner_choice_2;
    public Spinner spinner_choice_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tags_setup);

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

        //Go to main activity
        FloatingActionButton tag_validation_button = findViewById(R.id.tag_validation_button);
        tag_validation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected tags

                HashMap<String, String> tagHashMap = new HashMap<String, String>();
                tagHashMap.put("Général", "#Général");
                tagHashMap.put("Informatique", "#Informatique");
                tagHashMap.put("Droit", "#Droit");
                tagHashMap.put("Médecine", "#Médecine");
                tagHashMap.put("Sciences", "#Sciences");
                tagHashMap.put("Économie", "#Economie");
                tagHashMap.put("Philosophie et lettres", "#Philo&Lettres");
                tagHashMap.put("AGE", "#AGE");

                JSONArray selectedTags = new JSONArray();
                selectedTags.put(tagHashMap.get(spinner_choice_1.getSelectedItem().toString()));
                selectedTags.put(tagHashMap.get(spinner_choice_2.getSelectedItem().toString()));
                selectedTags.put(tagHashMap.get(spinner_choice_3.getSelectedItem().toString()));

                ArrayList<String> alSelectedTags = new ArrayList<>();
                alSelectedTags.add(tagHashMap.get(spinner_choice_1.getSelectedItem().toString()));
                alSelectedTags.add(tagHashMap.get(spinner_choice_2.getSelectedItem().toString()));
                alSelectedTags.add(tagHashMap.get(spinner_choice_3.getSelectedItem().toString()));

                JSONObject tagJson = new JSONObject();
                try {
                    tagJson.put("email", CurrentUser.getCurrentUser().getEmail());
                    tagJson.put("tag_pref", selectedTags);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Save selected tags into BD
                Toast.makeText(TagsSetupActivity.this, selectedTags.toString(), Toast.LENGTH_SHORT).show();

                SetPrefTags setTags = new SetPrefTags(TagsSetupActivity.this, alSelectedTags);
                setTags.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/tagpref", String.valueOf(tagJson));

            }
        });
    }

    private void initSpinners(List<Spinner> spinners, List<String> data){

        for (final Spinner spinner : spinners){

            //Create the adapter and set it
            XORSpinnerAdapter adapter = new XORSpinnerAdapter(this, spinner, data);
            spinner.setAdapter(adapter);
            //Init the default value for each spinner
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
