package com.unamur.umatters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.unamur.umatters.API.GetAllBox;


public class SubscriptionsBoxFragment extends Fragment {

    public SubscriptionsBoxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscriptions_box, container, false);

        //TODO : get every box of my subscriptions and sort them by date
        //test data (j'ai juste affiché l'ensemble des box que l'on a)
        BoxListAdapter adapter = new BoxListAdapter();
        RecyclerView rv = v.findViewById(R.id.subscription_box_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        GetAllBox task = new GetAllBox(adapter);
        task.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box");

        return v;
    }

}
