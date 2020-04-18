package com.unamur.umatters;

import android.content.res.Resources;
import android.widget.ArrayAdapter;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SubscriptionsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<SubscriptionsPerson> all_subscriptions_person;

    public SubscriptionsListAdapter(Activity context, ArrayList<SubscriptionsPerson> all_subscriptions_person) {
        super(context, R.layout.subscriptions_list_item);

        this.context=context;
        this.all_subscriptions_person = all_subscriptions_person;

    }

    public void addData(SubscriptionsPerson sub){
        all_subscriptions_person.add(sub);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return all_subscriptions_person.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position,View rowView,ViewGroup parent) {

        if (rowView== null){
            LayoutInflater inflater=context.getLayoutInflater();
            rowView=inflater.inflate(R.layout.subscriptions_list_item, null,true);
        }

        TextView msg_no_sub = rowView.findViewById(R.id.txt_no_sub);

        //Aucun abonnements
        if (all_subscriptions_person.isEmpty()){
            //msg_no_sub.setVisibility(View.VISIBLE);
        }
        //Au moins un abonnements
        else {

            //msg_no_sub.setVisibility(View.GONE);

            ImageView image = (ImageView) rowView.findViewById(R.id.img);
            TextView name = (TextView) rowView.findViewById(R.id.txt_name);
            TextView faculty = (TextView) rowView.findViewById(R.id.txt_faculty);
            ToggleButton subscription = (ToggleButton) rowView.findViewById(R.id.tgbtn_subscription);
            TextView level = (TextView) rowView.findViewById(R.id.txt_level);
            ImageView role = (ImageView) rowView.findViewById(R.id.img_role);

            SubscriptionsPerson current_person = all_subscriptions_person.get(position);

            //TODO set image
            //image.setImageBitmap(current_person.getImage());

            String full_name = current_person.getFirstname() + " " + current_person.getSurname();
            name.setText(full_name);
            subscription.setChecked(current_person.isSubscribed());
            String str_level = getContext().getResources().getString(R.string.level) + " " + current_person.getLevel();
            level.setText(str_level);
            faculty.setText(current_person.getFaculty());
            switch (current_person.getRole()){
                case "student":
                    role.setImageResource(R.drawable.ic_role_student);
                    break;
                case "academic":
                    role.setImageResource(R.drawable.ic_role_academic);
                    break;
                case "scientist":
                    role.setImageResource(R.drawable.ic_role_scientist);
                    break;
                case "ATG":
                    role.setImageResource(R.drawable.ic_role_personnel);
                    break;
            }
        }

        return rowView;

    }
}
