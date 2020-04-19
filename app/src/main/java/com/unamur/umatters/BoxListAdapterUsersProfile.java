package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.DeleteBox;
import com.unamur.umatters.API.LikeBox;
import com.unamur.umatters.API.LikeBoxProfile;
import com.unamur.umatters.API.SubToUser;
import com.unamur.umatters.API.VoteChoice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxListAdapterUsersProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final ArrayList<Box> boxList = new ArrayList<>();
    private static User user_profile = new User();

    @Override
    public int getItemCount() {
        return boxList.size()+1;
    }

    public void addData(Box box) {
        boxList.add(box);
        notifyDataSetChanged();
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

    //Pour sondage oui-non et sondage avec plusieurs choix possibles
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

    public void setUser(User user){
        user_profile = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.box_list_cell_profile, parent, false);
            return new BoxViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.profile_users_header, parent, false);
            return new HeaderViewHolder(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BoxViewHolder) {
            BoxViewHolder boxHolder = (BoxViewHolder) holder;
            Box box = boxList.get(position - 1);
            boxHolder.display(box);
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.display();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_HEADER) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private ImageView img_picture;
        private ImageView img_role;
        private TextView txt_name;
        private TextView txt_faculty;
        private TextView txt_level;
        private ToggleButton btn_subscription;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            //get elements
            img_picture = itemView.findViewById(R.id.img_picture);
            img_role = itemView.findViewById(R.id.img_role);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_faculty = itemView.findViewById(R.id.txt_faculty);
            txt_level = itemView.findViewById(R.id.txt_level);
            btn_subscription = itemView.findViewById(R.id.tgbtn_subscriptions);

            context = itemView.getContext();
        }

        public void display(){

            final CurrentUser user = CurrentUser.getCurrentUser();

            //TODO: get user picture, nbr followers and nbr following

            String firstname = user_profile.getFirstname();
            String lastname = user_profile.getLastname();
            String role = user_profile.getRole();
            int level = user_profile.getParticipation();
            String faculte = user.getFaculty();

            //Sub button
            //--init value
            if (user.getSubscriptions().contains(user_profile.getId())) {
                btn_subscription.setChecked(true);
            } else {
                btn_subscription.setChecked(false);
            }
            //--change value
            btn_subscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject subToUserJson = new JSONObject();
                    try {
                        subToUserJson.put("abonner", user.getEmail());
                        subToUserJson.put("user_ab", user_profile.getId());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    SubToUser task = new SubToUser(context, BoxListAdapterUsersProfile.this);
                    task.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/abonnement", String.valueOf(subToUserJson));

                    if (user.getSubscriptions().contains(user_profile.getId())) {
                        user.getSubscriptions().remove(user_profile.getId());
                    } else {
                        user.getSubscriptions().add(user_profile.getId());
                    }

                }
            });

            //Set values
            String fullname = firstname + " " + lastname;
            txt_name.setText(fullname);
            switch (role){
                case "Etudiant":
                    img_role.setImageResource(R.drawable.ic_role_student);
                    break;
                case "Académique":
                    img_role.setImageResource(R.drawable.ic_role_academic);
                    break;
                case "ATG":
                    img_role.setImageResource(R.drawable.ic_role_personnel);
                    break;
                case "Scientifique":
                    img_role.setImageResource(R.drawable.ic_role_scientist);
                    break;
            }
            String full_level = context.getResources().getString(R.string.level) + " " + level;
            txt_level.setText(full_level);

            txt_faculty.setText(faculte);
        }
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
        private final ImageView btn_comment;
        private final ToggleButton btn_description;
        private final TextView description;
        private ToggleButton btn_oui;
        private ToggleButton btn_non;

        private Context context;

        //Elements sondage oui_non
        private LinearLayout ll_oui_non;
        private TextView percent_oui;
        private TextView percent_non;
        private View line_oui;
        private View line_non;
        private TextView votes_oui;
        private TextView votes_non;

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
            box_menu = itemView.findViewById(R.id.box_menu_profile);
            btn_favorite = itemView.findViewById(R.id.button_favorite);
            btn_comment = itemView.findViewById(R.id.box_cell_comment_btn);
            btn_description = itemView.findViewById(R.id.description_btn);
            description = itemView.findViewById(R.id.box_description);

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

                    LikeBoxProfile likeBoxProfile = new LikeBoxProfile(context, BoxListAdapterUsersProfile.this, box.getId(), email);
                    likeBoxProfile.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box/like", String.valueOf(likeBoxJson));

                }
            });
            //Nombre de likes de la box
            nb_likes.setText(String.valueOf(box.getLikes().size()));

            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent runApp = new Intent(context, CommentActivity.class);
                    runApp.putExtra("box", box);
                    context.startActivity(runApp);
                }
            });

            //box menu
            box_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, box_menu);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.box_menu_user_profile);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.btn_report:
                                    //report
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


            //Nombre de likes de la box
            nb_likes.setText(String.valueOf(box.getLikes().size()));

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
                            task.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box/voter", String.valueOf(voteJson));

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
                        task.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box/voter", String.valueOf(voteJson));
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
                        task.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box/voter", String.valueOf(voteJson));
                    }
                });

            }

        }
    }

}