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

        saisieVocaleTitre.setOnClickListener(view -> {
            edittextChoix = "Titre";
            syntheseVocale();
        });

        saisieVocaleContent.setOnClickListener(view -> {
            edittextChoix = "Content";
            syntheseVocale();
        });
    }

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

    private void saveNewEmail(String titre , String destinataire , String contenue)
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
            AppDataBase db = AppDataBase.getDbInstance(this.getApplicationContext());
            Email email = new Email();
            email.Destinataire = destinataire;
            email.Content = contenue;
            email.Object = titre;
            db.emailDAO().insertEmail(email);
            startActivity(new Intent(this, MainActivity.class));
        }

    }

}
