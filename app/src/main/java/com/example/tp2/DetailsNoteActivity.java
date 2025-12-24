package com.example.tp2;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp2.model.Note;

/**
 * Activité affichant les détails complets d'une note
 */
public class DetailsNoteActivity extends AppCompatActivity {

    private TextView tvDetailNom, tvDetailDescription, tvDetailDate, tvDetailPriorite;
    private Button btnRetour;
    private ImageView ivDetailPhoto;
    private View viewDetailColorIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_note);

        // Initialisation des vues
        tvDetailNom = findViewById(R.id.tvDetailNom);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailPriorite = findViewById(R.id.tvDetailPriorite);
        btnRetour = findViewById(R.id.btnRetour);
        ivDetailPhoto = findViewById(R.id.ivDetailPhoto);
        viewDetailColorIndicator = findViewById(R.id.viewDetailColorIndicator);

        // Récupération de la note passée via Intent
        Note note = getIntent().getParcelableExtra("note");

        if (note != null) {
            displayNoteDetails(note);
        }

        // Bouton Retour
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Affiche les détails de la note
     */
    private void displayNoteDetails(Note note) {
        tvDetailNom.setText(note.getNom());
        tvDetailDescription.setText(note.getDescription());
        tvDetailDate.setText("Date: " + note.getDate());
        tvDetailPriorite.setText("Priorité: " + note.getPriorite());

        // Application de la couleur selon la priorité
        int color = getPriorityColor(note.getPriorite());
        viewDetailColorIndicator.setBackgroundColor(color);
        tvDetailPriorite.setTextColor(color);

        // Affichage de la photo si disponible
        if (note.getPhotoPath() != null && !note.getPhotoPath().isEmpty()) {
            ivDetailPhoto.setVisibility(View.VISIBLE);
            try {
                Uri photoUri = Uri.parse(note.getPhotoPath());
                ivDetailPhoto.setImageURI(photoUri);
            } catch (Exception e) {
                ivDetailPhoto.setVisibility(View.GONE);
            }
        } else {
            ivDetailPhoto.setVisibility(View.GONE);
        }
    }

    /**
     * Retourne la couleur correspondant à la priorité
     */
    private int getPriorityColor(String priorite) {
        switch (priorite) {
            case "Haute":
                return Color.parseColor("#D32F2F"); // Rouge
            case "Moyenne":
                return Color.parseColor("#F57C00"); // Orange
            case "Basse":
                return Color.parseColor("#388E3C"); // Vert
            default:
                return Color.GRAY;
        }
    }

    /**
     * Gestion du bouton retour système
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

