package com.unamur.umatters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unamur.umatters.API.GetAllBox;


public class SubscriptionsBoxFragment extends Fragment {

    private BoxListAdapter adapter;
    private TextView msg_no_sub;

    public SubscriptionsBoxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscriptions_box, container, false);
        msg_no_sub = v.findViewById(R.id.txt_no_sub);

        //test data (j'ai juste affiché l'ensemble des box que l'on a)
        adapter = new BoxListAdapter();
        RecyclerView rv = v.findViewById(R.id.subscription_box_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        getSubscriptionsBox();

        return v;
    }

    public void getSubscriptionsBox() {
        CurrentUser user = CurrentUser.getCurrentUser();

        if (user.getSubscriptions().size() > 0) {
            msg_no_sub.setVisibility(View.GONE);
        } else {
            msg_no_sub.setVisibility(View.VISIBLE);
        }

        for (String email : user.getSubscriptions()) {
            GetAllBox getBox = new GetAllBox(adapter);
            getBox.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box/user/" + email);
        }
    }

}
