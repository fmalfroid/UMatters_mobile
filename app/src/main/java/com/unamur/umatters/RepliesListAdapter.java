package com.unamur.umatters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.unamur.umatters.API.APIKeys;
import com.unamur.umatters.API.LikeReply;
import com.unamur.umatters.API.Signalement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RepliesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    //Variable with the box link to this comment list
    private static Comment current_comment;
    private static Box current_box;

    private ArrayList<Comment> commentList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return commentList.size()+1;
    }

    public void linkCommentAndBox(Comment comment, Box box){
        current_comment = comment;
        current_box = box;
        commentList = comment.getReplies();
        notifyDataSetChanged();
    }

    public void addData(Comment com) {
        commentList.add(com);
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
        if (current_comment.getId().equals(id_comment)) {
            if (current_comment.getLikes().contains(email)){
                current_comment.getLikes().remove(email);
            } else {
                current_comment.getLikes().add(email);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.reply_list_item, parent, false);
            return new RepliesListAdapter.CommentViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View view = inflater.inflate(R.layout.reply_header, parent, false);
            return new RepliesListAdapter.HeaderViewHolder(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RepliesListAdapter.CommentViewHolder) {
            RepliesListAdapter.CommentViewHolder CommentHolder = (RepliesListAdapter.CommentViewHolder) holder;
            Comment comment = commentList.get(position - 1);
            CommentHolder.display(comment);
        } else if (holder instanceof RepliesListAdapter.HeaderViewHolder) {
            //cast holder to VHHeader and set data for header.
            ((RepliesListAdapter.HeaderViewHolder) holder).display(current_comment);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_HEADER) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        //Elements
        private final ImageView role;
        private final TextView name;
        private final TextView date;
        private TextView text;
        private TextView nb_likes;
        private ToggleButton btn_favorite;
        private TextView comment_menu;

        private Activity context;

        public HeaderViewHolder(final View itemView) {
            super(itemView);

            //Recuperation des elements avec leur id
            role = itemView.findViewById(R.id.img_role);
            name = itemView.findViewById(R.id.cell_name);
            date = itemView.findViewById(R.id.cell_date);
            text = itemView.findViewById(R.id.cell_text);
            nb_likes = itemView.findViewById(R.id.cell_nb_like);
            btn_favorite = itemView.findViewById(R.id.button_favorite);
            comment_menu = itemView.findViewById(R.id.comment_menu);

            context = (Activity) itemView.getContext();

            ImageView arrow = itemView.findViewById(R.id.pop_go_back);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onBackPressed();
                }
            });

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

                    LikeReply likeReply = new LikeReply(context, RepliesListAdapter.this, comment.getId(),current_box.getId(), email);
                    likeReply.execute(APIKeys.getUrl() + "messages/like", String.valueOf(likeComJson));
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent runUserProfile = new Intent(context, UsersProfileActivity.class);
                    runUserProfile.putExtra("user_email", comment.getCreator().getId());
                    context.startActivity(runUserProfile);
                }
            });

            comment_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //creating a popup menu
                    final PopupMenu popup = new PopupMenu(context, comment_menu);
                    popup.inflate(R.menu.comment_menu);

                    final Menu menu = popup.getMenu();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
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

                                                    signalementJson.put("id_box", current_box.getId());
                                                    signalementJson.put("id_message", comment.getId());
                                                    signalementJson.put("signale", comment.getCreator().getId());
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

                    popup.show();
                }
            });

        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        //Elements
        private final ImageView role;
        private final TextView name;
        private final TextView date;
        private TextView text;
        private TextView nb_likes;
        private ToggleButton btn_favorite;
        private TextView comment_menu;

        private Context context;

        public CommentViewHolder(final View itemView) {
            super(itemView);

            //Recuperation des elements avec leur id
            role = itemView.findViewById(R.id.img_role);
            name = itemView.findViewById(R.id.cell_name);
            date = itemView.findViewById(R.id.cell_date);
            text = itemView.findViewById(R.id.cell_text);
            nb_likes = itemView.findViewById(R.id.cell_nb_like);
            btn_favorite = itemView.findViewById(R.id.button_favorite);
            comment_menu = itemView.findViewById(R.id.comment_menu);

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

                    LikeReply likeReply = new LikeReply(context, RepliesListAdapter.this, comment.getId(),current_box.getId(), email);
                    likeReply.execute(APIKeys.getUrl() + "messages/like", String.valueOf(likeComJson));
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent runUserProfile = new Intent(context, UsersProfileActivity.class);
                    runUserProfile.putExtra("user_email", comment.getCreator().getId());
                    context.startActivity(runUserProfile);
                }
            });

            comment_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //creating a popup menu
                    final PopupMenu popup = new PopupMenu(context, comment_menu);
                    popup.inflate(R.menu.comment_menu);

                    final Menu menu = popup.getMenu();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
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

                                                    signalementJson.put("id_box", current_box.getId());
                                                    signalementJson.put("id_message", comment.getId());
                                                    signalementJson.put("signale", comment.getCreator().getId());
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

                    popup.show();
                }
            });

        }
    }
}
