package com.example.imail;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import DataBase.AppDataBase;
import DataBase.Email;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder>{
    private ArrayList<Email> mail;

    // RecyclerView recyclerView;
    public EmailAdapter(ArrayList<Email> listdata) {
        mail = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.liste_mail_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    /**
     * Permet d'attribuer des fonctions ainsi que des valeurs pour chaque widget de chaque cellules
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String titre = mail.get(position).Object;
        final String destinataire = mail.get(position).Destinataire;
        holder.titre.setText(titre);
        holder.destinataire.setText(destinataire);
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReadEmail.class);
            intent.putExtra("Destinataire",mail.get(position).Destinataire);
            intent.putExtra("Titre",mail.get(position).Object);
            intent.putExtra("Contenu",mail.get(position).Content);
            v.getContext().startActivity(intent);
        });
        holder.button.setOnClickListener(v -> {
            AppDataBase db = AppDataBase.getDbInstance(v.getContext());
            db.emailDAO().delete(mail.get(position));
            mail.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,getItemCount());

        });
    }

    @Override
    public int getItemCount() {
        return mail.size();
    }

    /**
     * Permet d'intialiser tous les widget
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titre;
        public TextView destinataire;
        public ImageButton button;
        public ConstraintLayout item;
        public ViewHolder(View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.recycler_layout);
            this.button = itemView.findViewById(R.id.button_item);
            this.titre = itemView.findViewById(R.id.Titre);
            this.destinataire = itemView.findViewById(R.id.destinataire);
        }
    }
}