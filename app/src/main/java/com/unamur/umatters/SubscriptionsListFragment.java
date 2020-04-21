package com.unamur.umatters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.unamur.umatters.API.GetAllSubscriptions;

import java.util.ArrayList;


public class SubscriptionsListFragment extends Fragment {

    private ListView subscriptions_list_view;
    private TextView msg_no_sub;

    public SubscriptionsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscriptions_list, container, false);
        msg_no_sub = v.findViewById(R.id.txt_no_sub);

        //Set adapter
        SubscriptionsListAdapter adapter=new SubscriptionsListAdapter(getActivity(), new ArrayList<SubscriptionsPerson>());
        subscriptions_list_view =(ListView) v.findViewById(R.id.subscriptions_list_view);
        subscriptions_list_view.setAdapter(adapter);

        CurrentUser user = CurrentUser.getCurrentUser();
        String email = user.getEmail();

        Log.d("SubListFragment :", "GET http://mdl-std01.info.fundp.ac.be/api/v1/users/abonnement/"+ email);
        GetAllSubscriptions task = new GetAllSubscriptions(adapter, msg_no_sub);
        String url = "http://mdl-std01.info.fundp.ac.be/api/v1/users/abonnement/" + email;
        task.execute(url);

        return v;
    }

}
