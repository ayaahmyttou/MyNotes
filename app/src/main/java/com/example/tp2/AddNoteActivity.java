package com.example.tp2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.example.tp2.model.Note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activité pour ajouter une nouvelle note
 */
public class AddNoteActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;

    private EditText etNom, etDescription, etDate;
    private Spinner spinnerPriorite;
    private Button btnEnregistrer, btnCapturePhoto, btnSelectGallery;
    private ImageView ivPreviewPhoto;
    private String currentPhotoPath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Initialisation des vues
        etNom = findViewById(R.id.etNom);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        spinnerPriorite = findViewById(R.id.spinnerPriorite);
        btnEnregistrer = findViewById(R.id.btnEnregistrer);
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto);
        btnSelectGallery = findViewById(R.id.btnSelectGallery);
        ivPreviewPhoto = findViewById(R.id.ivPreviewPhoto);

        // Configuration du Spinner de priorité
        setupPrioritySpinner();

        // Configuration du sélecteur de date
        setupDatePicker();

        // Bouton Capture Photo
        btnCapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto();
            }
        });

        // Bouton Sélectionner depuis la galerie
        btnSelectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromGallery();
            }
        });

        // Bouton Enregistrer
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enregistrerNote();
            }
        });
    }

    /**
     * Configure le Spinner avec les niveaux de priorité
     */
    private void setupPrioritySpinner() {
        String[] priorites = {"Basse", "Moyenne", "Haute"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                priorites
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriorite.setAdapter(adapter);
    }

    /**
     * Configure le DatePicker pour sélectionner une date
     */
    private void setupDatePicker() {
        // Date actuelle par défaut
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        etDate.setText(sdf.format(calendar.getTime()));

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddNoteActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                                etDate.setText(sdf.format(calendar.getTime()));
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
    }

    /**
     * Lance l'appareil photo pour capturer une photo
     */
    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erreur lors de la création du fichier", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.example.tp2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Ouvre la galerie pour sélectionner une image
     */
    private void selectFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    /**
     * Crée un fichier pour stocker la photo
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
        String imageFileName = "NOTE_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Gère le résultat de la capture photo ou sélection galerie
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                ivPreviewPhoto.setImageURI(photoUri);
                ivPreviewPhoto.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Photo capturée avec succès", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_GALLERY_IMAGE && data != null) {
                photoUri = data.getData();
                ivPreviewPhoto.setImageURI(photoUri);
                ivPreviewPhoto.setVisibility(View.VISIBLE);
                currentPhotoPath = photoUri.toString();
                Toast.makeText(this, "Image sélectionnée avec succès", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Valide et enregistre la note
     */
    private void enregistrerNote() {
        String nom = etNom.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String priorite = spinnerPriorite.getSelectedItem().toString();

        // Validation
        if (nom.isEmpty()) {
            etNom.setError("Le nom est requis");
            etNom.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            etDescription.setError("La description est requise");
            etDescription.requestFocus();
            return;
        }

        // Création de la note
        Note newNote = new Note(nom, description, date, priorite, currentPhotoPath);

        // Retour à l'activité principale avec la nouvelle note
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newNote", newNote);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

