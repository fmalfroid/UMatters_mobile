package com.unamur.umatters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.DeleteBox;
import com.unamur.umatters.API.LikeBox;
import com.unamur.umatters.API.LikeBoxProfile;
import com.unamur.umatters.API.SubToUser;

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
                //Pour tous les choix possibles du sondage
                for (int i=0; i<box.getChoices().size(); i++) {
                    LinearLayout ll = new LinearLayout(context);
                    ll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));

                    //Nombre de vote pour le choix
                    TextView nb_votes = new TextView(context);
                    nb_votes.setText(String.valueOf(box.getChoices().get(i).getUsers().size()));
                    nb_votes.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));
                    nb_votes.setGravity(Gravity.END);

                    //Checkbox et Texte représentant le choix
                    CheckBox choice = new CheckBox(context);
                    choice.setText(box.getChoices().get(i).getName());
                    choice.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));

                    //Ajout du nombre de vote et de la checkbox à un LinearLayout
                    ll.addView(nb_votes);
                    ll.addView(choice);
                    ll.invalidate();

                    //Ajout du choix à la box
                    poll.addView(ll);
                    poll.invalidate();
                }
            } else if (box.getType().equals("oui_non")) {
                int pct_yes;
                int pct_no;

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
                pct_yes = (int)( (nb_yes/(nb_yes + nb_no))*100);
                pct_no = (int)( (nb_no/(nb_yes + nb_no))*100);

                ll_oui_non.setVisibility(View.VISIBLE);

                String str_percent_oui = (pct_yes) + "%";
                String str_percent_non = (pct_no) + "%";
                percent_oui.setText(str_percent_oui);
                percent_non.setText(str_percent_non);

                LinearLayout.LayoutParams layoutParams_oui = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, pct_yes);
                LinearLayout.LayoutParams layoutParams_non = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, pct_no);
                line_oui.setLayoutParams(layoutParams_oui);
                line_non.setLayoutParams(layoutParams_non);

                String str_votes_oui = pct_yes + " votes";
                String str_votes_non = pct_no + " votes";
                votes_oui.setText(str_votes_oui);
                votes_non.setText(str_votes_non);

            }

        }
    }

}