package com.example.tp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp2.adapter.NoteAdapter;
import com.example.tp2.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Activité principale affichant la liste des notes
 */
public class NoteListActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_NOTE = 1;

    private ListView listViewNotes;
    private FloatingActionButton fabAddNote;
    private NoteAdapter noteAdapter;
    private List<Note> notesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // Initialisation des vues
        listViewNotes = findViewById(R.id.listViewNotes);
        fabAddNote = findViewById(R.id.fabAddNote);

        // Initialisation de la liste des notes avec quelques exemples
        notesList = new ArrayList<>();
        initializeDefaultNotes();

        // Configuration de l'adaptateur
        noteAdapter = new NoteAdapter(this, notesList);
        listViewNotes.setAdapter(noteAdapter);

        // Gestion du clic sur le bouton Ajouter
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, REQUEST_ADD_NOTE);
            }
        });

        // Gestion du clic sur une note pour voir les détails
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = notesList.get(position);
                Intent intent = new Intent(NoteListActivity.this, DetailsNoteActivity.class);
                intent.putExtra("note", selectedNote);
                startActivity(intent);
            }
        });
    }

    /**
     * Initialise quelques notes par défaut pour la démonstration
     */
    private void initializeDefaultNotes() {
        notesList.add(new Note(
                "Réunion équipe",
                "Réunion hebdomadaire avec l'équipe de développement pour discuter de l'avancement du projet MyNotes",
                "25/12/2024",
                "Haute"
        ));

        notesList.add(new Note(
                "Courses",
                "Acheter du lait, pain, œufs, fromage et légumes pour la semaine",
                "24/12/2024",
                "Moyenne"
        ));

        notesList.add(new Note(
                "Idée projet",
                "Développer une fonctionnalité de synchronisation cloud pour l'application",
                "26/12/2024",
                "Basse"
        ));
    }

    /**
     * Récupère la nouvelle note depuis AddNoteActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_NOTE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("newNote")) {
                Note newNote = data.getParcelableExtra("newNote");
                notesList.add(newNote);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Note ajoutée avec succès", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

