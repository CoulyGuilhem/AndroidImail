package com.example.imail;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import DataBase.AppDataBase;
import DataBase.Email;

public class CreateEmail extends AppCompatActivity {

    private String texte;
    public String edittextChoix;
    public EditText titre;
    public EditText destinataire;
    public EditText content ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_email);

        ImageButton saisieVocaleTitre = findViewById(R.id.new_email_titre_vocal);
        ImageButton saisieVocaleContent = findViewById(R.id.new_email_message_vocal);
        ImageButton saveEmail = findViewById(R.id.addNewEmail);

        titre = findViewById(R.id.new_email_titre);
        destinataire = findViewById(R.id.new_email_destinataire_edittext);
        content = findViewById(R.id.new_email_message);

        saveEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewEmail(titre.getText().toString(),destinataire.getText().toString(),content.getText().toString());
            }
        });

        // Bouton lié à la saisie vocale du titre du mail
        saisieVocaleTitre.setOnClickListener(view -> {
            edittextChoix = "Titre";
            syntheseVocale();
        });
        // bouton lié à la saisie vocale du contenu du mail
        saisieVocaleContent.setOnClickListener(view -> {
            edittextChoix = "Content";
            syntheseVocale();
        });
    }

    /**
     * syntheseVocale permet d'intialiser l'intent qui gere l'affichage lié à la saisie vocale de google
     * elle fait appel startActivity for result qui va recuperer le texte dicté.
     */
    public void syntheseVocale(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Parlez pour écrire..."
        );
        startActivityForResult(intent,10);

    }

    /**
     * setTexte est une fonction qui permet d'identifier quel zone de texte doit etre modifiée lors de la fonction saisieVocale
     * la valeur edittextChoix est initialisée au moment ou on appui sur un des deux boutons de la saisie vocale
     */
    public void setTexte(){
        if(edittextChoix == "Titre"){
            titre.setText(texte);
        } else if (edittextChoix == "Content"){
            content.setText(texte);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            List<String> texteListe = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            texte = "";
            for (int i = 0; i < texteListe.size(); i++) {
                texte = texte + texteListe.get(i) + " ";
            }
            setTexte();
        }
    }

    /**
     * saveNewEmail permet d'enregistrer un email dans la base de donnée
     * elle verifie cependant que l'ensemble des adresse mail saisie respectent un paterne correspondant aux adresse mail actuelle
     * @param titre titre du mail
     * @param destinataire destinataires du mail
     * @param contenu contenu du mail
     */

    private void saveNewEmail(String titre , String destinataire , String contenu)
    {
        boolean emailValide = true;
        String[] listeEmail = destinataire.split(",");
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        for(int i = 0; i<listeEmail.length;i++){
            if(!pattern.matcher(listeEmail[i]).matches()){
                Toast.makeText(this, "Erreur de saisie de l'adresse mail", Toast.LENGTH_SHORT).show();
                emailValide = false;
            }
        }

        if(emailValide) {
            AppDataBase db = AppDataBase.getDbInstance(this.getApplicationContext()); // initialisation de la connexion à la base de donnée
            Email email = new Email();
            email.Destinataire = destinataire;
            email.Content = contenu;
            email.Object = titre;
            db.emailDAO().insertEmail(email);
            startActivity(new Intent(this, MainActivity.class));
        }

    }

}
