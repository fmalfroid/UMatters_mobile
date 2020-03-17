package com.unamur.umatters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BoxListAdapterProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

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
        new Box("poll", "Anthony Etienne", "28-02-2020", "Etudiant", 60, (List<String>) Arrays.asList("#Computer Science", "#Matériel"), "Où ajouter une machine à café?", choixTmp),
        new Box("yes_no", "Anthony Etienne", "28-02-2020", "Etudiant", 23, (List<String>) Arrays.asList("#General", "#BUMP", "#Horaire"), "Laisser la BUMP ouverte jusque 18h le vendredi?", choixOuiNon),
        new Box("text", "Anthony Etienne", "28-02-2020", "Etudiant", 42, (List<String>) Arrays.asList("#Computer Science", "#Matériel"), "Changer les souris du i21", null),
        new Box("text", "Anthony Etienne", "27-02-2020", "Etudiant", 56, (List<String>) Arrays.asList("#General", "#Arsenal"), "Je propose de rajouter du bouillon au poulet avec le riz de jeudi. Vous en pensez quoi?", null)
    );

    @Override
    public int getItemCount() {
        return boxList.size()+1;
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
            View view = inflater.inflate(R.layout.header_profile, parent, false);
            return new HeaderViewHolder(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BoxViewHolder) {
            BoxViewHolder BoxHolder = (BoxViewHolder) holder;
            Box box = boxList.get(position - 1);
            BoxHolder.display(box);
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
        public HeaderViewHolder(View itemView) {
            super(itemView);
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

        private Context context;

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
            context = itemView.getContext();

        }


        public void display(Box box) {

            List<String> typesTags = Arrays.asList(
                "#General",
                "#Computer Science",
                "#Law",
                "#Medicine",
                "#Sciences",
                "#Economics",
                "#Arts",
                "#AGE"
            );

            HashMap<String, String> tagTranslation = new HashMap<String, String>();
            tagTranslation.put("#General", context.getString(R.string.generalTag));
            tagTranslation.put("#Computer Science", context.getString(R.string.CSTag));
            tagTranslation.put("#Law", context.getString(R.string.LawTag));
            tagTranslation.put("#Medicine", context.getString(R.string.medicineTag));
            tagTranslation.put("#Sciences", context.getString(R.string.scienceTag));
            tagTranslation.put("#Economics", context.getString(R.string.EconomicsTag));
            tagTranslation.put("#Arts", context.getString(R.string.artsTag));
            tagTranslation.put("#AGE", context.getString(R.string.AGETag));

            //box menu
            box_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, box_menu);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.box_menu_profile);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.btn_delete:

                                    //open delete dialog
                                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                    View mView = inflater.inflate(R.layout.dialog_delete_box, null);
                                    mBuilder.setView(mView);
                                    final AlertDialog dialog = mBuilder.create();

                                    Button btn_cancel = mView.findViewById(R.id.btn_cancel);
                                    Button btn_delete = mView.findViewById(R.id.btn_delete);

                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    btn_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //TODO : Delete the box
                                            Toast.makeText(context, "This box has been deleted.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
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
                    role.setImageResource(R.drawable.role_etudiant);
                    break;
                case "Academique":
                    role.setImageResource(R.drawable.role_academic);
                    break;
                case "Recteur":
                    role.setImageResource(R.drawable.role_recteur);
                    break;
                case "Personnel":
                    role.setImageResource(R.drawable.role_personnel);
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
                    tag.setText(tagTranslation.get(box.getTags().get(i)));
                } else {
                    tag.setTextColor(0xFF0066ff);
                }

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