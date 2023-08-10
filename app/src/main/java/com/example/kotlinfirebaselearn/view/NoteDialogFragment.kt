package com.example.kotlinfirebaselearn.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.kotlinfirebaselearn.Notes
import com.example.kotlinfirebaselearn.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NoteDialogFragment : DialogFragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var buttonAdd: MaterialButton
    private lateinit var buttonClose: ImageButton
    private lateinit var edittextTitle: EditText
    private lateinit var edittextDescription: EditText
    private var addListener : DialogAddNoteListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val emailSemPonto = firebaseAuth.currentUser?.email.toString().replace(".", ",")
        database = FirebaseDatabase.getInstance().getReference("DatasUsers").
        child(emailSemPonto).child("Notes")

        buttonAdd = view.findViewById(R.id.button_addnote)
        buttonClose = view.findViewById(R.id.imagebutton_dialognotes_close)
        edittextTitle = view.findViewById(R.id.edittext_dialognotes_title)
        edittextDescription = view.findViewById(R.id.edittext_dialognotes_description)

        registerEvents()
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DialogAddNoteListener) {
            addListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        addListener = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        addListener?.onDialogNotesDismissed()
    }

    private fun registerEvents() {
        buttonAdd.setOnClickListener {
            val title: String = edittextTitle.text.toString()
            val description: String = edittextDescription.text.toString()
            val fragmentContext = context

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val note = Notes("", title, description)

                val noteRef = database.push()
                noteRef.setValue(note).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(fragmentContext, "Note saved sucessfully!!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(fragmentContext, "Erro to save note", Toast.LENGTH_SHORT).show()
                    }
                }
                dismiss()
            } else {
                Toast.makeText(fragmentContext, R.string.warning_empty_fields, Toast.LENGTH_SHORT).show()
            }
        }

        buttonClose.setOnClickListener {
            dismiss()
        }
    }


    interface DialogAddNoteListener {
        fun onDialogNotesDismissed()
    }
}