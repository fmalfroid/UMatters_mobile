package com.unamur.umatters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.unamur.umatters.API.APIKeys;
import com.unamur.umatters.API.AddInterest;
import com.unamur.umatters.API.DeleteBox;
import com.unamur.umatters.API.LikeBox;
import com.unamur.umatters.API.Signalement;
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

public class BoxListAdapter extends RecyclerView.Adapter<BoxListAdapter.BoxViewHolder> {

    private ArrayList<Box> boxList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return boxList.size();
    }

    public void addData(Box box) {
        boxList.add(box);
        notifyDataSetChanged();
    }

    public void removeAllData() {
        boxList.clear();
        notifyDataSetChanged();
    }

    public void showShareDialog(Context context, String id_box) {
        ShareLinkContent content = new ShareLinkContent.Builder()
            .setContentUrl(Uri.parse(APIKeys.getWebUrl() + "box?id=" + id_box))
            .build();

        ShareDialog shareDialog = new ShareDialog((Activity) context);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    public void toggleFavorite(String id_box, String email){
        for (Box box : boxList){
            if (box.getId().equals(id_box)){
                if (box.getLikes().contains(email)){
                    box.getLikes().remove(email);
                } else {
                    box.getLikes().add(email);
                }
            }
        }
        notifyDataSetChanged();
    }

    //toggleChoice dans le cas d'un sondage choix multiple avec plusieurs choix possibles. (checkbox)
    //utilisé aussi pour les sondages oui-non
    public void toggleChoice(String id_box, String email, String choice_name){
        for (Box box : boxList){
            if (box.getId().equals(id_box)){
                for (Choice choice: box.getChoices()) {
                    if (choice.getName().equals(choice_name)){
                        //Si le joueur avait voter ce choix, l'enlever
                        if (choice.getUsers().contains(email)){
                            choice.getUsers().remove(email);
                        }
                        //S'il n'avait pas voter ce choix, l'ajouter
                        else {
                            choice.getUsers().add(email);
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    //changeChoice dans le cas d'un saondage choix multiples avec un seul choix possible. (radiobutton)
    public void changeChoice(String id_box, String email, String choice_name){
        for (Box box : boxList){
            if (box.getId().equals(id_box)){
                for (Choice choice: box.getChoices()) {

                    //Get all other choice
                    ArrayList<Choice> otherChoice = new ArrayList<>();
                    for (Choice choice2: box.getChoices()) {
                        if (!choice.equals(choice2)){
                            otherChoice.add(choice2);
                        }
                    }

                    //If current choice is the one clicked
                    if (choice.getName().equals(choice_name)){

                        //Si le joueur avait voter ce choix, l'enlever
                        if (choice.getUsers().contains(email)){
                            choice.getUsers().remove(email);
                        }
                        //S'il n'avait pas voter ce choix, l'ajouter et enlever le vote sur tout les autres
                        else {
                            choice.getUsers().add(email);
                            for (Choice other_choice : otherChoice){
                                other_choice.getUsers().remove(email);
                            }
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void toggleInterest(String id_box, String email){
        CurrentUser user = CurrentUser.getCurrentUser();
        if (user.getInterest().contains(id_box)) {
            user.getInterest().remove(id_box);
        } else {
            user.getInterest().add(id_box);
        }
        notifyDataSetChanged();
    }

    @Override
    public BoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.box_list_cell, parent, false);
        return new BoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoxViewHolder holder, int position) {
        Box box = boxList.get(position);
        holder.display(box);
    }

    public class BoxViewHolder extends RecyclerView.ViewHolder {

        //Elements
        private final ImageView role;
        private final TextView name;
        private final TextView date;
        private final LinearLayout tagList;
        private final TextView text;
        private final TextView nb_likes;
        private final LinearLayout poll;
        private final TextView box_menu;
        private final ToggleButton btn_favorite;
        private final ToggleButton btn_interested;
        private final ImageView btn_comment;
        private final ToggleButton btn_description;
        private final TextView description;
        private final Button btn_share;

        private Context context;

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

        public BoxViewHolder(final View itemView) {
            super(itemView);

            //Recuperation des elements avec leur id
            role = itemView.findViewById(R.id.img_role);
            name = itemView.findViewById(R.id.box_cell_name);
            date = itemView.findViewById(R.id.box_cell_date);
            tagList = itemView.findViewById(R.id.box_cell_tag_list);
            text = itemView.findViewById(R.id.box_cell_poll_text);
            nb_likes = itemView.findViewById(R.id.box_cell_nb_like);
            poll = itemView.findViewById(R.id.box_cell_poll);
            box_menu = itemView.findViewById(R.id.box_menu);
            btn_favorite = itemView.findViewById(R.id.button_favorite);
            btn_interested = itemView.findViewById(R.id.button_interested);
            btn_comment = itemView.findViewById(R.id.box_cell_comment_btn);
            btn_description = itemView.findViewById(R.id.description_btn);
            description = itemView.findViewById(R.id.box_description);
            btn_share = itemView.findViewById(R.id.btn_share);

            context = itemView.getContext();


            //Elements sondage oui_non
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


        public void display(final Box box) {

            //Remove all views because it launch this function when the box is too far in the scroll so it duplicates
            tagList.removeAllViews();
            poll.removeAllViews();
            ll_oui_non.setVisibility(View.GONE);

            //Current user
            final CurrentUser user = CurrentUser.getCurrentUser();
            final String email = user.getEmail();

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

            //Favorite button
            //--init value
            if (box.getLikes().contains(email)){
                btn_favorite.setChecked(true);
            } else {
                btn_favorite.setChecked(false);
            }
            //--change value
            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set bounce animation
                    final Animation anim_bounce = AnimationUtils.loadAnimation(context, R.anim.anim_button_bounce);
                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    ButtonBounceInterpolator interpolator = new ButtonBounceInterpolator(0.15, 20);
                    anim_bounce.setInterpolator(interpolator);
                    btn_favorite.startAnimation(anim_bounce);

                    //Add / remove to favorite in API (works like a toggle)
                    JSONObject likeBoxJson = new JSONObject();
                    try {
                        likeBoxJson.put("email", email);
                        likeBoxJson.put("id_box", box.getId());
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    LikeBox likeBox = new LikeBox(context, BoxListAdapter.this, box.getId(), email);
                    likeBox.execute(APIKeys.getUrl() + "box/like", String.valueOf(likeBoxJson));
                }
            });
            //Nombre de likes de la box
            nb_likes.setText(String.valueOf(box.getLikes().size()));

            //Interested button
            //--init value
            if (user.getInterest().contains(box.getId())){
                btn_interested.setChecked(true);
            } else {
                btn_interested.setChecked(false);
            }
            //--change value
            btn_interested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set bounce animation
                    final Animation anim_bounce = AnimationUtils.loadAnimation(context, R.anim.anim_button_bounce);
                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    ButtonBounceInterpolator interpolator = new ButtonBounceInterpolator(0.15, 20);
                    anim_bounce.setInterpolator(interpolator);
                    btn_interested.startAnimation(anim_bounce);

                    JSONObject interestBoxJson = new JSONObject();
                    try {
                        interestBoxJson.put("email", email);
                        interestBoxJson.put("id_box", box.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AddInterest addInterest = new AddInterest(context, BoxListAdapter.this, box.getId(), email);
                    addInterest.execute(APIKeys.getUrl() + "users/box/interet", String.valueOf(interestBoxJson));
                }
            });

            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent runApp = new Intent(context, CommentActivity.class);
                    runApp.putExtra("box", box);
                    context.startActivity(runApp);
                }
            });

            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShareDialog(context, box.getId());
                }
            });

            //box menu
            //--The creator of the box is the current user (remove the menu)
            if (box.getCreator().getId().equals(user.getEmail())){
                box_menu.setVisibility(View.GONE);
            } else {
                box_menu.setVisibility(View.VISIBLE);
            }
            //--on menu click
            box_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    final PopupMenu popup = new PopupMenu(context, box_menu);
                    popup.inflate(R.menu.box_menu);

                    final Menu menu = popup.getMenu();

                    //init menu
                    //--Subscribed to user
                    if (user.getSubscriptions().contains(box.getCreator().getId())){
                        menu.findItem(R.id.btn_menu_subscribe).setVisible(false);
                        menu.findItem(R.id.btn_menu_unsubscribe).setVisible(true);
                    }
                    //Not subscribed to user
                    else {
                        menu.findItem(R.id.btn_menu_unsubscribe).setVisible(false);
                        menu.findItem(R.id.btn_menu_subscribe).setVisible(true);
                    }

                    //--On menu item click
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.btn_menu_subscribe:

                                    //s'abonner
                                    JSONObject subToUserJson = new JSONObject();
                                    try {
                                        subToUserJson.put("abonner", email);
                                        subToUserJson.put("user_ab", box.getCreator().getId());
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                    SubToUser task = new SubToUser(context, menu, menu.findItem(R.id.btn_menu_subscribe), menu.findItem(R.id.btn_menu_unsubscribe));
                                    task.execute(APIKeys.getUrl() + "users/abonnement", String.valueOf(subToUserJson));

                                    user.getSubscriptions().add(box.getCreator().getId());

                                    return true;

                                case R.id.btn_menu_unsubscribe:

                                    //se désabonner

                                    JSONObject subToUserJson2 = new JSONObject();
                                    try {
                                        subToUserJson2.put("abonner", email);
                                        subToUserJson2.put("user_ab", box.getCreator().getId());
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                    SubToUser task2 = new SubToUser(context, menu, menu.findItem(R.id.btn_menu_unsubscribe), menu.findItem(R.id.btn_menu_subscribe));
                                    task2.execute(APIKeys.getUrl() + "users/abonnement", String.valueOf(subToUserJson2));

                                    user.getSubscriptions().remove(box.getCreator().getId());

                                    return true;

                                case R.id.btn_report:

                                    //open report dialog
                                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                    View mView = inflater.inflate(R.layout.dialog_report_box, null);
                                    mBuilder.setView(mView);
                                    final AlertDialog dialog = mBuilder.create();

                                    Button btn_cancel = mView.findViewById(R.id.btn_cancel);
                                    Button btn_report = mView.findViewById(R.id.btn_report);
                                    final EditText description = mView.findViewById(R.id.edtxt_description);

                                    //init spinner
                                    final SpinnerWrapContent report_type = mView.findViewById(R.id.spinner_report_type);
                                    List<String> data = new LinkedList<>(Arrays.asList(context.getResources().getStringArray(R.array.report_types)));
                                    SpinnerWrapContentAdapter adapter = new SpinnerWrapContentAdapter(context, data);
                                    report_type.setAdapter(adapter);

                                    //button cancel
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    //button report
                                    btn_report.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (description.length() < 10){
                                                Toast.makeText(context, R.string.error_box_too_small_description, Toast.LENGTH_SHORT).show();
                                            } else {

                                                JSONObject signalementJson = new JSONObject();
                                                try {

                                                    signalementJson.put("id_box", box.getId());
                                                    signalementJson.put("signale", box.getCreator().getId());
                                                    signalementJson.put("signaleur", user.getEmail());
                                                    signalementJson.put("type", report_type.getSelectedItem().toString());
                                                    signalementJson.put("explication", description.getText().toString());

                                                } catch (JSONException e){
                                                    e.printStackTrace();
                                                }
                                                Signalement task = new Signalement(context);
                                                task.execute(APIKeys.getUrl() + "signalements", String.valueOf(signalementJson));

                                                dialog.dismiss();
                                            }

                                        }
                                    });

                                    dialog.show();

                                    return true;

                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });

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

                    //Si le createur de la box n'est pas le current user.
                    if (!box.getCreator().getId().equals(user.getEmail())){
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

                    final int index = i;


                    final RadioButton choice = new RadioButton(context);
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


                    //CHECKBOX MULTIPLE CHOICES
                    /*
                    LinearLayout ll = new LinearLayout(context);
                    ll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));

                    //Nombre de vote pour le choix
                    TextView nb_votes = new TextView(context);
                    nb_votes.setText(String.valueOf(box.getChoices().get(i).getUsers().size()));
                    nb_votes.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));
                    nb_votes.setGravity(Gravity.END);

                    //Checkbox et Texte représentant le choix
                    RadioButton choice = new RadioButton(context);
                    choice.setText(box.getChoices().get(i).getName());
                    choice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));

                    radiogroup.addView(choice);

                    //Ajout du nombre de vote et de la checkbox à un LinearLayout
                    ll.addView(nb_votes);
                    ll.addView(choice);
                    ll.invalidate();

                    //Ajout du choix à la box
                    poll.addView(ll);
                    poll.invalidate();
                    */



                    //Gestion des votes
                    //--init value
                    //User voted for this choice
                    if (box.getChoices().get(i).getUsers().contains(user.getEmail())){
                        choice.setChecked(true);
                    }
                    //User didn't vote for this choice
                    else {
                        choice.setChecked(false);
                    }
                    //--Change value
                    choice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            JSONObject voteJson = new JSONObject();
                            try {
                                voteJson.put("email", user.getEmail());
                                voteJson.put("id_box", box.getId());
                                voteJson.put("key", box.getChoices().get(index).getName());
                            } catch (JSONException e){
                                e.printStackTrace();
                            }

                            //toggleChoice(box.getId(), user.getEmail(), box.getChoices().get(index).getName());    checkbox (plusieurs choix)
                            changeChoice(box.getId(), user.getEmail(), box.getChoices().get(index).getName());      //radiobutton (un seul choix)

                            Log.d("BoxListAdapter :", "POST http://mdl-std01.info.fundp.ac.be/api/v1/box/voter with json : " + voteJson.toString());
                            VoteChoice task = new VoteChoice(context);
                            task.execute(APIKeys.getUrl() + "box/voter", String.valueOf(voteJson));

                        }
                    });
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

                //Gestion des votes oui-non
                //--init value
                //User voted for this choice
                final Choice vote_non = box.getChoices().get(0);
                final Choice vote_oui = box.getChoices().get(1);
                //Pas voter oui ni non
                if ((!vote_non.getUsers().contains(user.getEmail()) && !vote_oui.getUsers().contains(user.getEmail()))){
                    btn_oui.setChecked(false);
                    btn_non.setChecked(false);
                }
                //voter juste non
                else if (vote_non.getUsers().contains(user.getEmail())){
                    btn_non.setChecked(true);
                    btn_oui.setChecked(false);
                }
                //voter juste oui
                else if (vote_oui.getUsers().contains(user.getEmail())){
                    btn_oui.setChecked(true);
                    btn_non.setChecked(false);
                }

                //--change values
                final JSONObject voteJson = new JSONObject();
                try {
                    voteJson.put("email", user.getEmail());
                    voteJson.put("id_box", box.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                btn_oui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (vote_non.getUsers().contains(user.getEmail())){
                            toggleChoice(box.getId(), user.getEmail(), "Non");
                        }
                        toggleChoice(box.getId(), user.getEmail(), "Oui");

                        try{
                            voteJson.put("key", "Oui");
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        //Vote oui (garde le non pour l'instant car l'api est mal faite, faudra attendre un changement dans l'api pour enlever le non)
                        Log.d("BoxListAdapter :", "POST http://mdl-std01.info.fundp.ac.be/api/v1/box/voter with json : " + voteJson.toString());
                        VoteChoice task = new VoteChoice(context);
                        task.execute(APIKeys.getUrl() + "box/voter", String.valueOf(voteJson));
                    }
                });
                btn_non.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            voteJson.put("key", "Non");
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        if (vote_oui.getUsers().contains(user.getEmail())){
                            toggleChoice(box.getId(), user.getEmail(), "Oui");
                        }
                        toggleChoice(box.getId(), user.getEmail(), "Non");

                        //Vote non (garde le oui pour l'instant car l'api est mal faite, faudra attendre un changement dans l'api pour enlever le oui)
                        Log.d("BoxListAdapter :", "POST http://mdl-std01.info.fundp.ac.be/api/v1/box/voter with json : " + voteJson.toString());
                        VoteChoice task = new VoteChoice(context);
                        task.execute(APIKeys.getUrl() + "box/voter", String.valueOf(voteJson));
                    }
                });

            }

        }
    }
}