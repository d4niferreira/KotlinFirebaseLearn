package com.example.kotlinfirebaselearn.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfirebaselearn.Notes
import com.example.kotlinfirebaselearn.databinding.RowNotesBinding
import com.example.kotlinfirebaselearn.interfaces.OnNoteListener

class NotesHolder(private val bind: RowNotesBinding, private val listener: OnNoteListener) :
    RecyclerView.ViewHolder(bind.root) {

    fun bind(note: Notes, position : Int) {
        bind.textviewRowTitle.text = note.title
        bind.textviewRowDescription.text = note.description
        bind.imagebuttonRowDelete.setOnClickListener {
            listener.onDelete(note, position)
        }
    }

}