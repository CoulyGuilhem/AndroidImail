package com.example.imail;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import DataBase.AppDataBase;
import DataBase.Email;
import DataBase.EmailDAO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_imail);

        Intent newEmailRedirect = new Intent(this, CreateEmail.class);
        RecyclerView recyclerEmail = findViewById(R.id.liste_imail_recyclerView);
        Button newEmailButton = findViewById(R.id.liste_imail_new_email);
        ImageButton searchBarVocal = findViewById(R.id.liste_imail_vocal_bouton);
        EditText searchBar = findViewById(R.id.liste_imail_search_edittext);

        ArrayList<Email> emails = loadEmailList();
        EmailAdapter adapter = new EmailAdapter(emails);
        recyclerEmail.setAdapter(adapter);
        recyclerEmail.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        /**
         * Permet de faire appel a la fonction onTextChanged afin d'actualiser la barre de recherche à chaque caracteres saisis
         */
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBar();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        searchBar.addTextChangedListener(textWatcher);
        newEmailButton.setOnClickListener(v -> startActivity(newEmailRedirect));
        searchBarVocal.setOnClickListener(v -> syntheseVocale());
    }

    /**
     * Permet de recuperer les mails sauvegardés dans la base de donnée
     * @return
     */

    private ArrayList<Email> loadEmailList()
    {
        AppDataBase db = AppDataBase.getDbInstance(this.getApplicationContext());
        EmailDAO emailDao = db.emailDAO();
        return (ArrayList<Email>) emailDao.getAllEmail();
    }

    /**
     * searchBar est l'algorythme permetant de garder que les mails ayant le contenu de la barre de recherche dans leur titre
     */

    public void searchBar(){
        EditText searchBar = findViewById(R.id.liste_imail_search_edittext);
        String pattern = searchBar.getText().toString();
        ArrayList<Email> emails = loadEmailList();
        RecyclerView recyclerEmail = findViewById(R.id.liste_imail_recyclerView);

        ArrayList<Email> emailsMatchs = new ArrayList<>();
        for(int i = 0; i < emails.size();i++){
            if(emails.get(i).Object.contains(pattern)){
                if(emailsMatchs.size() == 0){
                    emailsMatchs.add(emails.get(i));
                } else {
                    emailsMatchs.add(emailsMatchs.size()-1,emails.get(i));
                }
            }
        }
        EmailAdapter adapter = new EmailAdapter(emailsMatchs);
        recyclerEmail.setAdapter(adapter);
        recyclerEmail.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        EditText searchBar = findViewById(R.id.liste_imail_search_edittext);
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            List<String> texteListe = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            StringBuilder texte = new StringBuilder();
            for(int i = 0;i<texteListe.size();i++){
                texte.append(texteListe.get(i)).append(" ");
            }
            searchBar.setText(texte.toString());
        }
    }

}