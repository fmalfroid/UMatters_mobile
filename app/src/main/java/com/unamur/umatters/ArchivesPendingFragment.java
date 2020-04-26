package com.unamur.umatters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class ArchivesPendingFragment extends Fragment {

    public static ArchivesListAdapter adapter;

    public ArchivesPendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_archives_pending, container, false);

        //init recyclerview
        adapter = new ArchivesListAdapter();
        final RecyclerView rv = v.findViewById(R.id.archives_box_list);
        //Init the layout and the adapter of the recycler view
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        //init list status
        adapter.setArchivesListStatus("En suspend");

        //test data
        List list_choice_2 = new ArrayList();
        ArrayList<String> likes_2 = new ArrayList<>();
        likes_2.add("email");
        likes_2.add("email");
        List<String> tags_2 = new ArrayList<String>();
        tags_2.add("#Général");
        tags_2.add("#BUMP");
        User user_2 = new User("-2", "René", "Lataupe", "Académique", null, "Informatique", 2);
        Box box_2 = new Box("-2", list_choice_2, user_2, "17-11-98", likes_2, tags_2, "Ceci est une archive test avec 2 partout.", "textuelle", "Petite description.");

        ArrayList<String> list_response_2 = new ArrayList<>();
        list_response_2.add("1ere reponse");
        list_response_2.add("2e reponse");

        ArrayList<String> list_response_date_2 = new ArrayList<>();
        list_response_date_2.add("25-04-2020");
        list_response_date_2.add("26-04-2020");

        Archive test2 = new Archive(box_2,list_response_2, list_response_date_2);

        adapter.addData(test2);

        return v;
    }

}
