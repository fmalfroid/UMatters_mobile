package com.unamur.umatters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerWrapContentAdapter extends BaseAdapter implements SpinnerAdapter {

    private List<String> choices;
    private Context context;

    public SpinnerWrapContentAdapter(Context context, List<String> choices) {
        this.choices = choices;
        this.context = context;
    }

    @Override
    public int getCount() {
        return choices.size();
    }

    @Override
    public Object getItem(int position) {
        return choices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        //ME DEMANDE PAS CE QU'IL SE PASSE J'AI RIEN COMPRIS
        //Mais en gros ça modifie la taille du spinner en fonction de son contenu

        int selectedItemPosition = position;
        if (parent instanceof AdapterView) {
            selectedItemPosition = ((AdapterView) parent).getSelectedItemPosition();
        }

        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_color_main, parent, false);
        }

        final TextView textView = view.findViewById(R.id.main);
        textView.setText(choices.get(selectedItemPosition));

        return view;
    }

    @Override
    public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {

        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_color_dropdown, parent, false);
        }

        final TextView textView = view.findViewById(R.id.dropdown);
        textView.setText(choices.get(position));

        return view;
    }
}