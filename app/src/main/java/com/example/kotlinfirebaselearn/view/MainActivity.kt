package com.example.kotlinfirebaselearn.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinfirebaselearn.Notes
import com.example.kotlinfirebaselearn.R
import com.example.kotlinfirebaselearn.User
import com.example.kotlinfirebaselearn.adapter.NotesAdapter
import com.example.kotlinfirebaselearn.databinding.ActivityMainBinding
import com.example.kotlinfirebaselearn.interfaces.OnNoteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), NoteDialogFragment.DialogAddNoteListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersData: DatabaseReference
    private lateinit var notesData: DatabaseReference
    private val adapter = NotesAdapter()
    private lateinit var context: Context
    private lateinit var emailSemPonto: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        firebaseAuth = FirebaseAuth.getInstance()
        emailSemPonto = firebaseAuth.currentUser?.email.toString().replace(".", ",")

        database = FirebaseDatabase.getInstance()
        usersData = database.getReference("DatasUsers")
        notesData = usersData.child(emailSemPonto).child("Notes")

        binding.recyclerviewMainNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewMainNotes.adapter = adapter
        binding.recyclerviewMainNotes.setHasFixedSize(true)

        val noteListener = object : OnNoteListener {
            override fun onClick() {
                Toast.makeText(context, "Click clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onDelete(note: Notes, position: Int) {
                note.noteId?.let {
                    notesData.child(it).removeValue().addOnCompleteListener {
                        adapter.remove(position)
                    }
                }
            }
        }
        adapter.setListener(noteListener)

        retrieveNotesData()
        registerEvents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun registerEvents() {
        setSupportActionBar(findViewById(R.id.toolbar))

        binding.floatingbuttonMainAddNote.setOnClickListener {
            val dialog = NoteDialogFragment()
            dialog.show((this as AppCompatActivity).supportFragmentManager, "AddNotes")
        }

        val userReference = usersData.child(emailSemPonto)
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        val firstname = user.firstName
                        binding.textviewMainactivityTitle.text = firstname + getString(R.string.title_notes)
                    }
                } else {
                    //The node does not exist or there is no data in it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MAIN", "Error retrieving data")
            }
        })
    }

    override fun onDialogNotesDismissed() {
        retrieveNotesData()
    }

    private fun retrieveNotesData() {
        notesData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notesList: MutableList<Notes> = mutableListOf()

                for (childSnapshot in snapshot.children) {
                    val chave = childSnapshot.key
                    val objeto: Notes? = childSnapshot.getValue(Notes::class.java)
                    objeto?.noteId = chave ?: ""
                    objeto?.let { notesList.add(it) }
                }

                adapter.updatedNotes(notesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERRO", error.message)
            }
        })
    }
}