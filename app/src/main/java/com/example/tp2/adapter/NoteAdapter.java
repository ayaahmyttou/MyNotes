package com.example.tp2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tp2.R;
import com.example.tp2.model.Note;

import java.util.List;

/**
 * Adaptateur personnalisé pour afficher les notes dans la ListView
 * Affiche le nom, la date et applique une couleur selon la priorité
 */
public class NoteAdapter extends BaseAdapter {
    private Context context;
    private List<Note> notesList;
    private LayoutInflater inflater;

    public NoteAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //appelée pour chaque élément visible dans la ListView, initialise le viewHolder
    //récupère les données et affiche les informations
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_note, parent, false);
            holder = new ViewHolder();
            holder.tvNom = convertView.findViewById(R.id.tvNoteNom);
            holder.tvDate = convertView.findViewById(R.id.tvNoteDate);
            holder.tvPriorite = convertView.findViewById(R.id.tvNotePriorite);
            holder.viewColorIndicator = convertView.findViewById(R.id.viewColorIndicator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = notesList.get(position);

        // Affichage des données
        holder.tvNom.setText(note.getNom());
        holder.tvDate.setText(note.getDate());
        holder.tvPriorite.setText(note.getPriorite());

        // Application de la couleur selon la priorité
        int color = getPriorityColor(note.getPriorite());
        holder.viewColorIndicator.setBackgroundColor(color);
        holder.tvPriorite.setTextColor(color);

        return convertView;
    }

    /**
     * Retourne la couleur correspondant à la priorité
     * @param priorite La priorité de la note (Basse, Moyenne, Haute)
     * @return La couleur correspondante
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
     * Pattern ViewHolder pour faire références aux composants UI
     */
    static class ViewHolder {
        TextView tvNom;
        TextView tvDate;
        TextView tvPriorite;
        View viewColorIndicator;
    }
}

