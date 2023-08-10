package com.example.kotlinfirebaselearn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfirebaselearn.Notes
import com.example.kotlinfirebaselearn.databinding.RowNotesBinding
import com.example.kotlinfirebaselearn.holder.NotesHolder
import com.example.kotlinfirebaselearn.interfaces.OnNoteListener

class NotesAdapter : RecyclerView.Adapter<NotesHolder>() {

    private lateinit var listener : OnNoteListener
    private var notesList: MutableList<Notes> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val item = RowNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesHolder(item, listener)
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.bind(notesList[position], position)
    }

    override fun getItemCount(): Int {
        return notesList.count()
    }

    fun setListener (listener : OnNoteListener){
        this.listener = listener
    }

    fun updatedNotes(list: MutableList<Notes>) {
        notesList = list
        notifyDataSetChanged()
    }

    fun remove (position : Int){
        notesList.removeAt(position)
        notifyItemRemoved(position)
    }

}