package com.unamur.umatters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class ArchivesRefusedFragment extends Fragment {


    public ArchivesRefusedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_archives_refused, container, false);

        //Init of the spinner
        SpinnerWrapContent council_choice = (SpinnerWrapContent) v.findViewById(R.id.council_choice);
        List<String> data = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.council_choices)));
        SpinnerWrapContentAdapter adapter = new SpinnerWrapContentAdapter(getContext(), data);
        council_choice.setAdapter(adapter);

        //Go back button
        ImageView img_arrowback = v.findViewById(R.id.pop_go_back);
        img_arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

}
