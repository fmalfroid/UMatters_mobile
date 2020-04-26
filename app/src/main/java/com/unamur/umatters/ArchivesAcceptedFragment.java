package com.unamur.umatters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class ArchivesAcceptedFragment extends Fragment {

    public static ArchivesListAdapter adapter;

    public ArchivesAcceptedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_archives_accepted, container, false);

        //init recyclerview
        adapter = new ArchivesListAdapter();
        final RecyclerView rv = v.findViewById(R.id.archives_box_list);
        //Init the layout and the adapter of the recycler view
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        //init list status
        adapter.setArchivesListStatus("Accepté");

        //test data
        List list_choice = new ArrayList();
        ArrayList<String> likes = new ArrayList<>();
        likes.add("email");
        List<String> tags = new ArrayList<String>();
        tags.add("#Général");
        User user = new User("-1", "René", "Lataupe", "Académique", null, "Informatique", 2);
        Box box = new Box("-1", list_choice, user, "17-11-98", likes, tags, "Ceci est une archive test avec 1 partout.", "textuelle", "Petite description.");

        ArrayList<String> list_response = new ArrayList<>();
        list_response.add("1ere reponse");

        ArrayList<String> list_response_date = new ArrayList<>();
        list_response_date.add("25-04-2020");

        Archive test = new Archive(box,list_response, list_response_date);

        adapter.addData(test);

        return v;
    }

}
