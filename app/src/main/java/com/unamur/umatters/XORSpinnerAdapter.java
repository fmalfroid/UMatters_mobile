package com.unamur.umatters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

public class XORSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private List<String> tags;
    private Spinner spinner;
    private Context context;

    public XORSpinnerAdapter(Context context, Spinner spinner, List<String> tags) {
        this.tags = tags;
        this.spinner = spinner;
        this.context = context;
    }

    private static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  View.inflate(context, R.layout.spinner_color_main, null);
        TextView textView = view.findViewById(R.id.main);
        textView.setText(tags.get(position));
        return view;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {

        View view;
        view =  View.inflate(context, R.layout.spinner_color_dropdown, null);
        final TextView textView = view.findViewById(R.id.dropdown);
        textView.setText(tags.get(position));

        //If the item is already selected, change its color to grey
        if(TagsSetupActivity.selectedItems.containsValue(position)){
            textView.setTextColor(ContextCompat.getColor(context, R.color.lightGrey));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TagsSetupActivity.selectedItems.containsValue(position)){
                    spinner.setSelection(position);
                    hideSpinnerDropDown(spinner);
                }
            }
        });


        return view;
    }
}
