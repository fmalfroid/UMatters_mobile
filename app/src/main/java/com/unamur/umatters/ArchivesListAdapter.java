package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.AddInterest;
import com.unamur.umatters.API.DeleteBox;
import com.unamur.umatters.API.LikeBox;
import com.unamur.umatters.API.SubToUser;
import com.unamur.umatters.API.VoteChoice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ArchivesListAdapter extends RecyclerView.Adapter<ArchivesListAdapter.ArchivesViewHolder> {

    private ArrayList<Archive> archivesList = new ArrayList<>();
    private String list_status;

    @Override
    public int getItemCount() {
        return archivesList.size();
    }

    public void addData(Archive archive){
        archivesList.add(archive);
        notifyDataSetChanged();
    }

    public void clearArchivesList(){
        archivesList.clear();
        notifyDataSetChanged();
    }

    public void setArchivesListStatus(String status){
        list_status = status;
    }

    @Override
    public ArchivesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.archive_cell, parent, false);
        return new ArchivesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArchivesViewHolder holder, int position) {
        Archive archive = archivesList.get(position);
        holder.display(archive);
    }

    public class ArchivesViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private LinearLayout main_layout;

        //Elements box
        private final ImageView role;
        private final TextView name;
        private final TextView date;
        private final LinearLayout tagList;
        private final TextView text;
        private final TextView nb_likes;
        private final LinearLayout poll;
        private final ToggleButton btn_favorite;
        private final ToggleButton btn_description;
        private final TextView description;

        //Elements sondage oui_non
        private LinearLayout ll_oui_non;
        private TextView percent_oui;
        private TextView percent_non;
        private View line_oui;
        private View line_non;
        private TextView votes_oui;
        private TextView votes_non;
        private ToggleButton btn_oui;
        private ToggleButton btn_non;

        private ArrayList<View> all_response_views = new ArrayList<>();


        public ArchivesViewHolder(final View itemView) {
            super(itemView);

            context = itemView.getContext();

            main_layout = itemView.findViewById(R.id.main_layout);

            //Recuperation box
            role = itemView.findViewById(R.id.img_role);
            name = itemView.findViewById(R.id.box_cell_name);
            date = itemView.findViewById(R.id.box_cell_date);
            tagList = itemView.findViewById(R.id.box_cell_tag_list);
            text = itemView.findViewById(R.id.box_cell_poll_text);
            nb_likes = itemView.findViewById(R.id.box_cell_nb_like);
            poll = itemView.findViewById(R.id.box_cell_poll);
            btn_favorite = itemView.findViewById(R.id.button_favorite);
            btn_description = itemView.findViewById(R.id.description_btn);
            description = itemView.findViewById(R.id.box_description);

            //Recuperation sondage oui-non
            ll_oui_non = itemView.findViewById(R.id.ll_oui_non);
            percent_oui = itemView.findViewById(R.id.percent_oui);
            percent_non = itemView.findViewById(R.id.percent_non);
            line_oui = itemView.findViewById(R.id.line_oui);
            line_non = itemView.findViewById(R.id.line_non);
            votes_oui = itemView.findViewById(R.id.votes_oui);
            votes_non = itemView.findViewById(R.id.votes_non);
            btn_oui = itemView.findViewById(R.id.btn_oui);
            btn_non = itemView.findViewById(R.id.btn_non);

        }


        public void display(final Archive archive) {

            final CurrentUser user = CurrentUser.getCurrentUser();

            final Box box = archive.getBox();

            //Remove all views because it launch this function when the box is too far in the scroll so it duplicates
            tagList.removeAllViews();
            poll.removeAllViews();
            ll_oui_non.setVisibility(View.GONE);

            for (View v: all_response_views) {
                ((ViewGroup)v.getParent()).removeView(v);
            }
            all_response_views.clear();


            List<String> typesTags = Arrays.asList(
                    "#Général",
                    "#Informatique",
                    "#Droit",
                    "#Médecine",
                    "#Sciences",
                    "#Economie",
                    "#Philo&Lettres",
                    "#AGE"
            );
            //init favorite button
            if (box.getLikes().contains(user.getEmail())){
                btn_favorite.setChecked(true);
            }
            //Nombre de likes de la box
            nb_likes.setText(String.valueOf(archive.getBox().getLikes().size()));

            switch (box.getRole()){
                case "Etudiant":
                    role.setImageResource(R.drawable.ic_role_student);
                    break;
                case "Académique":
                    role.setImageResource(R.drawable.ic_role_academic);
                    break;
                case "Scientifique":
                    role.setImageResource(R.drawable.ic_role_scientist);
                    break;
                case "ATG":
                    role.setImageResource(R.drawable.ic_role_personnel);
                    break;
            }

            //Nom de l'utilisateur ayant créé la box
            name.setText(box.getName());

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!box.getCreator().getId().equals(user.getEmail())) {
                        Intent runUserProfile = new Intent(context, UsersProfileActivity.class);
                        runUserProfile.putExtra("user_email", box.getCreator().getId());
                        context.startActivity(runUserProfile);
                    } else {
                        Intent runProfile = new Intent(context, ProfileActivity.class);
                        context.startActivity(runProfile);
                    }

                }
            });

            //Date à laquelle la box a été crée
            date.setText(box.getDate());

            //Crée un Textview pour chaque tag et l'ajoute à un LinearLayout
            for (int i=0; i<box.getTags().size(); i++) {
                TextView tag = new TextView(context);
                tag.setTextSize(12);
                tag.setText(box.getTags().get(i));
                tag.setPadding(0,0,10,0);
                if (typesTags.contains(box.getTags().get(i))) {
                    tag.setTextColor(0xFFff6600);
                    tag.setText(box.getTags().get(i));
                } else {
                    tag.setTextColor(0xFF0066ff);
                }

                tagList.addView(tag);
                tagList.invalidate();
            }

            //Texte de la box ou question du sondage
            text.setText(box.getTitle());
            //Description de la box
            description.setText(box.getDescription());
            //handle description visibility
            btn_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btn_description.isChecked()){
                        description.setVisibility(View.VISIBLE);
                    } else {
                        description.setVisibility(View.GONE);
                    }
                }
            });

            if (box.getType().equals("choix_multiple")) {

                //Horizontal ll containing nbr votes on the left and radio group on the right
                LinearLayout ll = new LinearLayout(context);
                TableRow.LayoutParams params_ll = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
                params_ll.gravity = Gravity.CENTER_HORIZONTAL;
                ll.setLayoutParams(params_ll);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                //Linear layout of nbr votes
                LinearLayout ll_nbr_votes = new LinearLayout(context);
                ll_nbr_votes.setOrientation(LinearLayout.VERTICAL);
                ll.addView(ll_nbr_votes);

                //Radio group of radio buttons
                final RadioGroup radiogroup = new RadioGroup(context);
                radiogroup.clearCheck();

                //--Pour tous les choix possibles du sondage
                for (int i=0; i<box.getChoices().size(); i++) {

                    final RadioButton choice = new RadioButton(context);
                    choice.setClickable(false);
                    choice.setText(box.getChoices().get(i).getName());
                    choice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));

                    radiogroup.addView(choice);
                    radiogroup.invalidate();

                    TextView nb_votes = new TextView(context);
                    nb_votes.setText(String.valueOf(box.getChoices().get(i).getUsers().size()));
                    nb_votes.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));
                    nb_votes.setGravity(Gravity.END);
                    nb_votes.setPadding(0,19,0,20);
                    ll_nbr_votes.addView(nb_votes);

                    //init user choice
                    Choice box_choice = box.getChoices().get(i);
                    if (box_choice.getUsers().contains(user.getEmail())){
                        choice.setChecked(true);
                    }

                }

                ll.addView(radiogroup);

                poll.addView(ll);
                poll.invalidate();

            }

            else if (box.getType().equals("oui_non")) {

                float pct_yes;
                float pct_no;

                float nb_yes;
                float nb_no;

                if (box.getChoices().get(0).getName().equals("Oui")) {
                    nb_yes = (box.getChoices().get(0).getUsers().size());
                    nb_no = (box.getChoices().get(1).getUsers().size());
                } else {
                    nb_yes = (box.getChoices().get(1).getUsers().size());
                    nb_no = (box.getChoices().get(0).getUsers().size());
                }

                //Calcule le pourcentage de Oui et de Non
                pct_yes = (nb_yes/(nb_yes + nb_no))*100;
                pct_no = (nb_no/(nb_yes + nb_no))*100;

                ll_oui_non.setVisibility(View.VISIBLE);

                float rounded_pct_yes = (float) (Math.round(pct_yes * 10.0) / 10.0);
                float rounded_pct_no = (float) (Math.round(pct_no * 10.0) / 10.0);
                String str_percent_oui = (rounded_pct_yes) + "%";
                String str_percent_non = (rounded_pct_no) + "%";
                percent_oui.setText(str_percent_oui);
                percent_non.setText(str_percent_non);

                LinearLayout.LayoutParams layoutParams_oui = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, pct_yes);
                LinearLayout.LayoutParams layoutParams_non = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, pct_no);
                line_oui.setLayoutParams(layoutParams_oui);
                line_non.setLayoutParams(layoutParams_non);

                String str_votes_oui = (int) nb_yes + " votes";
                String str_votes_non = (int) nb_no + " votes";
                votes_oui.setText(str_votes_oui);
                votes_non.setText(str_votes_non);

                btn_oui.setClickable(false);
                btn_non.setClickable(false);

                for (Choice choice : box.getChoices()){
                    String str_choice = choice.getName();
                    if (choice.getUsers().contains(user.getEmail())){
                        if (str_choice.equals("Oui")){
                            btn_oui.setChecked(true);
                        }
                        if (str_choice.equals("Non")){
                            btn_non.setChecked(true);
                        }
                    }
                }

            }

            //REPONSE
            //--for every response
            for (int i=0; i<archive.getResponses().size(); i++) {

                Response response = archive.getResponses().get(i);
                String response_status = response.getStatus();
                String response_date = response.getDate();
                String response_txt = response.getTexte();

                //separator
                View separator = new View(context);
                separator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                separator.setBackgroundColor(Color.parseColor("#AFAFAF"));
                main_layout.addView(separator);

                all_response_views.add(separator);

                //response layout
                LinearLayout ll_response= new LinearLayout(context);
                ll_response.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ll_response.setOrientation(LinearLayout.HORIZONTAL);

                all_response_views.add(ll_response);

                //--response status
                View view_response_status = new View(context);
                view_response_status.setLayoutParams(new ViewGroup.LayoutParams(25, ViewGroup.LayoutParams.MATCH_PARENT));
                switch (response_status){
                    case "Accepté":
                        view_response_status.setBackgroundColor(ContextCompat.getColor(context, R.color.pale_green));
                        break;
                    case "En suspend":
                        view_response_status.setBackgroundColor(ContextCompat.getColor(context, R.color.pale_orange));
                        break;
                    case "En suspens":
                        view_response_status.setBackgroundColor(ContextCompat.getColor(context, R.color.pale_orange));
                        break;
                    case "Refusé":
                        view_response_status.setBackgroundColor(ContextCompat.getColor(context, R.color.pale_red));
                        break;
                }
                ll_response.addView(view_response_status);

                //--response
                LinearLayout ll_vertical = new LinearLayout(context);
                ll_vertical.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ll_vertical.setOrientation(LinearLayout.VERTICAL);
                ll_vertical.setPadding(30,30,30,30);

                //----header with response title and response date
                LinearLayout ll_horizontal = new LinearLayout(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 15);
                ll_horizontal.setLayoutParams(params);
                ll_horizontal.setOrientation(LinearLayout.HORIZONTAL);

                TextView txt_response_title = new TextView(context);
                txt_response_title.setText(R.string.response);
                txt_response_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                Space space = new Space(context);
                space.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                TextView txt_response_date = new TextView(context);
                txt_response_date.setText(response_date);
                txt_response_date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                ll_horizontal.addView(txt_response_title);
                ll_horizontal.addView(space);
                ll_horizontal.addView(txt_response_date);
                ll_vertical.addView(ll_horizontal);

                //----text response
                TextView txt_response = new TextView(context);
                txt_response.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                txt_response.setTextColor(ContextCompat.getColor(context,R.color.black));
                txt_response.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                txt_response.setText(response_txt);

                ll_vertical.addView(txt_response);

                ll_response.addView(ll_vertical);
                main_layout.addView(ll_response);

            }
        }
    }
}