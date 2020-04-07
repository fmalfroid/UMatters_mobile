package com.unamur.umatters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.AddInterest;
import com.unamur.umatters.API.AddInterestComment;
import com.unamur.umatters.API.LikeBox;
import com.unamur.umatters.API.LikeBoxComment;
import com.unamur.umatters.API.LikeCom;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    //Variable with the box link to this comment list
    private static Box current_box;

    private final ArrayList<Comment> commentList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return commentList.size()+1;
    }

    public void addData(Comment comment) {
        commentList.add(comment);
        notifyDataSetChanged();
    }

    //Function to use to link the box
    public void linkBox(Box box){
        current_box = box;
        notifyDataSetChanged();
    }

    public void toggleFavorite(String email){
        if (current_box.getLikes().contains(email)){
            current_box.getLikes().remove(email);
        } else {
            current_box.getLikes().add(email);
        }
        notifyDataSetChanged();
    }

    public void removeAllData() {
        commentList.clear();
        notifyDataSetChanged();
    }

    public void toggleComFavorite(String id_comment, String email){
        for (Comment comment : commentList){
            if (comment.getId().equals(id_comment)){
                if (comment.getLikes().contains(email)){
                    comment.getLikes().remove(email);
                } else {
                    comment.getLikes().add(email);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.comment_list_item, parent, false);
            return new CommentViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.comment_header, parent, false);
            return new HeaderViewHolder(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CommentViewHolder) {
            CommentViewHolder CommentHolder = (CommentViewHolder) holder;
            Comment comment = commentList.get(position - 1);
            CommentHolder.display(comment);
        } else if (holder instanceof HeaderViewHolder) {
            //cast holder to VHHeader and set data for header.
            ((HeaderViewHolder) holder).display(current_box);
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
        private final ToggleButton btn_description;
        private final TextView description;

        private Activity context;

        //Elements sondage oui_non
        private LinearLayout ll_oui_non;
        private TextView percent_oui;
        private TextView percent_non;
        private View line_oui;
        private View line_non;
        private TextView votes_oui;
        private TextView votes_non;

        public HeaderViewHolder(View itemView) {
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
            btn_description = itemView.findViewById(R.id.description_btn);
            description = itemView.findViewById(R.id.box_description);

            context = (Activity) itemView.getContext();

            //Elements sondage oui_non
            ll_oui_non = itemView.findViewById(R.id.ll_oui_non);
            percent_oui = itemView.findViewById(R.id.percent_oui);
            percent_non = itemView.findViewById(R.id.percent_non);
            line_oui = itemView.findViewById(R.id.line_oui);
            line_non = itemView.findViewById(R.id.line_non);
            votes_oui = itemView.findViewById(R.id.votes_oui);
            votes_non = itemView.findViewById(R.id.votes_non);

            ImageView arrow = itemView.findViewById(R.id.pop_go_back);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onBackPressed();
                }
            });
        }

        public void display(final Box box) {

            //Remove all views because it launch this function when the box is too far in the scroll so it duplicates
            tagList.removeAllViews();
            poll.removeAllViews();
            ll_oui_non.setVisibility(View.GONE);

            //Current user
            final CurrentUser user = CurrentUser.getCurrentUser();
            final String email = user.getEmail();

            if (box.getCreator().getId().equals(email)) {
                box_menu.setVisibility(View.GONE);
            } else {
                box_menu.setVisibility(View.VISIBLE);
            }

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

                    LikeBoxComment likeBox = new LikeBoxComment(context, CommentListAdapter.this, box.getId(), email);
                    likeBox.execute("http://mdl-std01.info.fundp.ac.be/api/v1/box/like", String.valueOf(likeBoxJson));
                }
            });
            //Nombre de likes du com
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

                    AddInterestComment addInterest = new AddInterestComment(context, CommentListAdapter.this, box.getId(), email);
                    addInterest.execute("http://mdl-std01.info.fundp.ac.be/api/v1/users/box/interet", String.valueOf(interestBoxJson));
                }
            });
            //box menu
            box_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, box_menu);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.box_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.btn_unsubscribe:
                                    //handle menu1 click
                                    return true;
                                case R.id.btn_report:
                                    //handle menu2 click
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

                if (box.getChoices().get(0).getName().equals("oui")) {
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

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        //Elements
        private final ImageView role;
        private final TextView name;
        private final TextView date;
        private TextView text;
        private TextView nb_likes;
        private TextView nb_replies;
        private ToggleButton btn_favorite;

        private Context context;

        public CommentViewHolder(final View itemView) {
            super(itemView);

            //Recuperation des elements avec leur id
            role = itemView.findViewById(R.id.img_role);
            name = itemView.findViewById(R.id.cell_name);
            date = itemView.findViewById(R.id.cell_date);
            text = itemView.findViewById(R.id.cell_text);
            nb_likes = itemView.findViewById(R.id.cell_nb_like);
            nb_replies = itemView.findViewById(R.id.nb_replies);
            btn_favorite = itemView.findViewById(R.id.button_favorite);

            context = itemView.getContext();

        }


        public void display(final Comment comment) {

            //Current user
            final CurrentUser user = CurrentUser.getCurrentUser();
            final String email = user.getEmail();

            switch (comment.getCreator().getRole()){
                case "Etudiant":
                    role.setImageResource(R.drawable.ic_role_student);
                    break;
                case "ATG":
                    role.setImageResource(R.drawable.ic_role_personnel);
                    break;
                case "Scientifique":
                    role.setImageResource(R.drawable.ic_role_scientist);
                    break;
                case "Academique":
                    role.setImageResource(R.drawable.ic_role_academic);
                    break;
            }
            name.setText(comment.getCreator().getName());
            date.setText(comment.getDate());
            text.setText(comment.getText());
            nb_likes.setText(String.valueOf(comment.getLikes().size()));
            nb_replies.setText(String.valueOf(comment.getReplies().size()));

            //Favorite button
            //--init value
            if (comment.getLikes().contains(email)){
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
                    JSONObject likeComJson = new JSONObject();
                    try {
                        likeComJson.put("email", email);
                        likeComJson.put("id_box", current_box.getId());
                        likeComJson.put("id_msg", comment.getId());
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    LikeCom likeCom = new LikeCom(context, CommentListAdapter.this, comment.getId(),current_box.getId(), email);
                    likeCom.execute("http://mdl-std01.info.fundp.ac.be/api/v1/messages/like", String.valueOf(likeComJson));
                }
            });
        }
    }

}