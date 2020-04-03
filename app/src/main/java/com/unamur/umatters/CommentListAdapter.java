package com.unamur.umatters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

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

        private Activity context;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            context = (Activity) itemView.getContext();

            ImageView arrow = itemView.findViewById(R.id.pop_go_back);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.onBackPressed();
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


        public void display(Comment comment) {
            name.setText(comment.getCreator().getName());
            date.setText(comment.getDate());
            text.setText(comment.getText());
            nb_likes.setText(String.valueOf(comment.getLikes().size()));
            nb_replies.setText(String.valueOf(comment.getReplies().size()));

            //Favorite button
            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Set bounce animation
                    final Animation anim_bounce = AnimationUtils.loadAnimation(context, R.anim.anim_button_bounce);
                    // Use bounce interpolator with amplitude 0.2 and frequency 20
                    ButtonBounceInterpolator interpolator = new ButtonBounceInterpolator(0.15, 20);
                    anim_bounce.setInterpolator(interpolator);
                    btn_favorite.startAnimation(anim_bounce);
                    //TODO : add/remove comment from favorite
                }
            });

            //TODO Changer l'image du role
        }
    }

}