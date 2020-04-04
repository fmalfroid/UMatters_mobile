package com.unamur.umatters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.unamur.umatters.API.GetAllSubscriptions;

import java.util.ArrayList;


public class SubscriptionsListFragment extends Fragment {

    private ListView subscriptions_list_view;

    public SubscriptionsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscriptions_list, container, false);

        //TODO : get subscriptions info
        //test data
        ArrayList<SubscriptionsPerson> all_subscriptions_person = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.student_pic);
        SubscriptionsPerson person1 = new SubscriptionsPerson(bitmap, "email","René", "Dupuis", "Droit", true, "student", 3);
        SubscriptionsPerson person2 = new SubscriptionsPerson(bitmap, "email","Maxence", "Fermier", "Sciences", true, "ATG", 5);
        SubscriptionsPerson person3 = new SubscriptionsPerson(bitmap, "email","Matthieu", "Louis", "Philosophie et lettres", true, "academic", 2);
        SubscriptionsPerson person4 = new SubscriptionsPerson(bitmap, "email","Florian", "Malfroid", "Informatique", true, "scientist", 1);
        all_subscriptions_person.add(person1);
        all_subscriptions_person.add(person2);
        all_subscriptions_person.add(person3);
        all_subscriptions_person.add(person4);

        //Set adapter
        SubscriptionsListAdapter adapter=new SubscriptionsListAdapter(getActivity(), all_subscriptions_person);
        subscriptions_list_view =(ListView) v.findViewById(R.id.subscriptions_list_view);
        subscriptions_list_view.setAdapter(adapter);

        CurrentUser user = CurrentUser.getCurrentUser();
        String email = user.getEmail();
        GetAllSubscriptions task = new GetAllSubscriptions(adapter);
        String url = "http://mdl-std01.info.fundp.ac.be/api/v1/users/abonnement/" + email;
        task.execute(url);

        return v;
    }

}
