package com.unamur.umatters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final ArrayList<Notif> notificationsList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notifications_list_item, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsAdapter.ViewHolder holder, int position) {
        Notif notification = notificationsList.get(position);
        holder.display(notification);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Elements
        private final TextView notif_text;
        private final TextView notif_hour;
        private final TextView notif_date;

        private Context context;

        public ViewHolder(final View itemView) {
            super(itemView);

            //Recuperation des elements avec leur id
            notif_text = itemView.findViewById(R.id.notif_cell_text);
            notif_hour = itemView.findViewById(R.id.notif_cell_hour);
            notif_date = itemView.findViewById(R.id.notif_cell_date);

            context = itemView.getContext();

        }

        public void display(Notif notif){
            String hour = notif.getHour();
            String date = notif.getDate();
            String text = notif.getText();
            User linkUser = notif.getLinkUser();

            notif_text.setText(text);
            notif_hour.setText(hour);
            notif_date.setText(date);
        }
    }

    public void addData(Notif notif){
        notificationsList.add(notif);
        notifyDataSetChanged();
    }

}
