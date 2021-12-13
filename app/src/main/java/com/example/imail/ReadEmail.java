package com.example.imail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReadEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_email);

        Intent newEmailRedirect = new Intent(this,CreateEmail.class);
        Button newEmailButton = findViewById(R.id.read_create_email);
        Intent intent = getIntent();
        TextView destinataire = findViewById(R.id.read_email_destinataire);
        TextView objet = findViewById(R.id.read_email_titre);
        TextView contenu = findViewById(R.id.read_email_message);

        // On initalise les textes avec les extra saisis dans la classe EmailAdapter
        destinataire.setText(intent.getStringExtra("Destinataire"));
        objet.setText(intent.getStringExtra("Titre"));
        contenu.setText(intent.getStringExtra("Contenu"));
        newEmailButton.setOnClickListener(view -> startActivity(newEmailRedirect));
    }
}
