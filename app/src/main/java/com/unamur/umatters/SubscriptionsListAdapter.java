package com.unamur.umatters;

import android.content.Intent;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.SubToUser;

import org.json.JSONException;
import org.json.JSONObject;

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

        final CurrentUser user = CurrentUser.getCurrentUser();
        LayoutInflater inflater=context.getLayoutInflater();

        if (rowView== null){
            rowView=inflater.inflate(R.layout.subscriptions_list_item, null,true);
        }

        View sub_list_view = inflater.inflate(R.layout.fragment_subscriptions_list, null,true);
        TextView msg_no_sub = sub_list_view.findViewById(R.id.txt_no_sub);

        //Aucun abonnements
        if (all_subscriptions_person.isEmpty()){
            msg_no_sub.setVisibility(View.VISIBLE);
        }
        //Au moins un abonnements
        else {

            msg_no_sub.setVisibility(View.GONE);

            ImageView image = (ImageView) rowView.findViewById(R.id.img);
            TextView name = (TextView) rowView.findViewById(R.id.txt_name);
            TextView faculty = (TextView) rowView.findViewById(R.id.txt_faculty);
            ToggleButton btn_subscription = (ToggleButton) rowView.findViewById(R.id.tgbtn_subscription);
            TextView level = (TextView) rowView.findViewById(R.id.txt_level);
            ImageView role = (ImageView) rowView.findViewById(R.id.img_role);

            final SubscriptionsPerson current_person = all_subscriptions_person.get(position);

            image.setImageBitmap(current_person.getImage());

            String full_name = current_person.getFirstname() + " " + current_person.getSurname();
            name.setText(full_name);
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

            //Sub button
            //--init value
            btn_subscription.setChecked(current_person.isSubscribed());
            //--change value
            btn_subscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //API
                    JSONObject subToUserJson = new JSONObject();
                    try {
                        subToUserJson.put("abonner", user.getEmail());
                        subToUserJson.put("user_ab", current_person.getEmail());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    SubToUser task = new SubToUser(context, SubscriptionsListAdapter.this);
                    task.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/abonnement", String.valueOf(subToUserJson));

                    //Adapter
                    if (user.getSubscriptions().contains(current_person.getEmail())) {
                        Toast.makeText(context, R.string.unsubscribed, Toast.LENGTH_SHORT).show();
                        user.getSubscriptions().remove(current_person.getEmail());
                        current_person.setSubscribed(false);
                    } else {
                        Toast.makeText(context, R.string.subscribed, Toast.LENGTH_SHORT).show();
                        user.getSubscriptions().add(current_person.getEmail());
                        current_person.setSubscribed(true);
                    }

                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent runUserProfile = new Intent(context, UsersProfileActivity.class);
                    runUserProfile.putExtra("user_email", current_person.getEmail());
                    context.startActivity(runUserProfile);

                }
            });

        }

        return rowView;

    }
}
