package com.unamur.umatters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class BoxListAdapter extends RecyclerView.Adapter<BoxListAdapter.BoxViewHolder> {

    //Exemple de choix de sondage
    private final List<Pair<String, Integer>> choixTmp = Arrays.asList(
        Pair.create("3e étage", 10),
        Pair.create("4e étage", 7)
    );

    //Exemple de choix oui/non
    private final List<Pair<String, Integer>> choixOuiNon = Arrays.asList(
        Pair.create("yes", 85),
        Pair.create("no", 15)
    );

    //Exemples de box
    private final List<Box> boxList = Arrays.asList(
        new Box("poll", "Patrick Heymans", "28-02-2020", "Professeur", 60, (List<String>) Arrays.asList("#info", "#Matériel"), "Où ajouter une machine à café?", choixTmp),
        new Box("yes_no", "Anonyme", "28-02-2020", "Etudiant", 23, (List<String>) Arrays.asList("#Général", "#BUMP", "#Horaire"), "Laisser la BUMP ouverte jusque 18h le vendredi?", choixOuiNon),
        new Box("text", "Florian Malfroid", "28-02-2020", "Etudiant", 42, (List<String>) Arrays.asList("#info", "#Matériel"), "Changer les souris du i21", null)
    );

    @Override
    public int getItemCount() {
        return boxList.size();
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
        private final TextView name;
        private final TextView date;
        private final LinearLayout tagList;
        private final TextView text;
        private final TextView nb_likes;
        private final LinearLayout poll;

        private Context context;

        private Box currentBox;

        public BoxViewHolder(final View itemView) {
            super(itemView);

            //Recuperation des elements avec leur id
            name = itemView.findViewById(R.id.box_cell_name);
            date = itemView.findViewById(R.id.box_cell_date);
            tagList = itemView.findViewById(R.id.box_cell_tag_list);
            text = itemView.findViewById(R.id.box_cell_poll_text);
            nb_likes = itemView.findViewById(R.id.box_cell_nb_like);
            poll = itemView.findViewById(R.id.box_cell_poll);
            context = itemView.getContext();

        }


        public void display(Box box) {
            currentBox = box;

            //Nom de l'utilisateur ayant créé la box
            name.setText(box.getName());
            //Date à laquelle la box a été crée
            date.setText(box.getDate());

            //Crée un Textview pour chaque tag et l'ajoute à un LinearLayout
            for (int i=0; i<box.getTags().size(); i++) {
                TextView tag = new TextView(context);
                tag.setTextSize(12);
                tag.setText(box.getTags().get(i));
                tag.setPadding(0,0,5,0);

                tagList.addView(tag);
                tagList.invalidate();
            }

            //Texte de la box ou question du sondage
            text.setText(box.getText());
            //Nombre de likes de la box
            nb_likes.setText(String.valueOf(box.getNb_likes()));

            if (box.getType().equals("poll")) {
                //Pour tous les choix possibles du sondage
                for (int i=0; i<box.getChoices().size(); i++) {
                    LinearLayout ll = new LinearLayout(context);
                    ll.setGravity(Gravity.CENTER_HORIZONTAL);

                    //Nombre de vote pour le choix
                    TextView nb_votes = new TextView(context);
                    nb_votes.setText(String.valueOf(box.getChoices().get(i).second));
                    nb_votes.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4));
                    nb_votes.setGravity(Gravity.END);

                    //Checkbox et Texte représentant le choix
                    CheckBox choice = new CheckBox(context);
                    choice.setText(box.getChoices().get(i).first);
                    choice.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 5));

                    //Ajout du nombre de vote et de la checkbox à un LinearLayout
                    ll.addView(nb_votes);
                    ll.addView(choice);
                    ll.invalidate();

                    //Ajout du choix à la box
                    poll.addView(ll);
                    poll.invalidate();
                }
            } else if (box.getType().equals("yes_no")) {
                float pct_yes;
                float pct_no;

                float nb_yes;
                float nb_no;

                if (box.getChoices().get(0).first.equals("yes")) {
                    nb_yes = (box.getChoices().get(0).second);
                    nb_no = (box.getChoices().get(1).second);
                } else {
                    nb_yes = (box.getChoices().get(1).second);
                    nb_no = (box.getChoices().get(0).second);
                }

                //Calcule le pourcentage de Oui et de Non
                pct_yes = (nb_yes/(nb_yes + nb_no))*100;
                pct_no = (nb_no/(nb_yes + nb_no))*100;

                LinearLayout ll = new LinearLayout(context);
                ll.setGravity(Gravity.CENTER_HORIZONTAL);

                //Initialisation des paramètres des vues (width, height, weight et margins)
                TableRow.LayoutParams paramsllyes = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
                paramsllyes.setMargins(0, 0, 2, 0);
                TableRow.LayoutParams paramsllno = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
                paramsllno.setMargins(2, 0, 0, 0);
                TableRow.LayoutParams paramsnbvoteyes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 10);
                paramsnbvoteyes.setMargins(0, 0, 5, 0);
                TableRow.LayoutParams paramsnbvoteno = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 10);
                paramsnbvoteno.setMargins(5, 0, 0, 0);
                TableRow.LayoutParams paramstextyes = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, pct_yes);
                paramstextyes.setMargins(0, 0, 0, 0);
                TableRow.LayoutParams paramstextno = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, pct_no);
                paramstextno.setMargins(0, 0, 0, 0);

                //LinearLayout pour le choix "OUI"
                LinearLayout llyes = new LinearLayout(context);
                llyes.setGravity(Gravity.END);
                llyes.setWeightSum(110);
                llyes.setLayoutParams(paramsllyes);

                //LinearLayout pour le choix "NON"
                LinearLayout llno = new LinearLayout(context);
                llno.setGravity(Gravity.START);
                llno.setWeightSum(110);
                llno.setLayoutParams(paramsllno);

                //TextView pour le pourcentage de OUI
                TextView nb_votes_yes = new TextView(context);
                nb_votes_yes.setText(String.valueOf((int) pct_yes) + "%");
                nb_votes_yes.setLayoutParams(paramsnbvoteyes);

                //TextView pour le pourcentage de NON
                TextView nb_votes_no = new TextView(context);
                nb_votes_no.setText(String.valueOf((int) pct_no) + "%");
                nb_votes_no.setLayoutParams(paramsnbvoteno);

                //Barre du oui (la taille représente le pourcentage)
                TextView textYes = new TextView(context);
                textYes.setText(R.string.yes);
                textYes.setTextColor(0xFF000000);
                textYes.setBackgroundColor(0xFFAFAFAF);
                textYes.setGravity(Gravity.CENTER);
                textYes.setLayoutParams(paramstextyes);

                //Barre du non (la taille représente le pourcentage)
                TextView textNo = new TextView(context);
                textNo.setText(R.string.no);
                textNo.setTextColor(0xFF000000);
                textNo.setBackgroundColor(0xFFAFAFAF);
                textNo.setGravity(Gravity.CENTER);
                textNo.setLayoutParams(paramstextno);

                //Ajout des éléments à leur LinearLayout respectif
                llyes.addView(nb_votes_yes);
                llyes.addView(textYes);
                llyes.invalidate();
                llno.addView(textNo);
                llno.addView(nb_votes_no);
                llno.invalidate();

                ll.addView(llyes);
                ll.addView(llno);

                //ajout du sondage à la box
                poll.addView(ll);
                poll.invalidate();

            }

        }
    }

}