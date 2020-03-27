package com.unamur.umatters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private final ArrayList<Comment> commentList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void addData(Comment comment) {
        commentList.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment, parent, false);
        return new CommentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.display(comment);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        //Elements
        private final ImageView role;
        private final TextView name;
        private final TextView date;
        private TextView text;
        private TextView nb_likes;
        private TextView nb_replies;

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

            context = itemView.getContext();

        }


        public void display(Comment comment) {
            name.setText(comment.getCreator().getName());
            date.setText(comment.getDate());
            text.setText(comment.getText());
            nb_likes.setText(String.valueOf(comment.getLikes().size()));
            nb_replies.setText(String.valueOf(comment.getReplies().size()));

            //TODO Changer l'image du role
        }
    }

}