package com.example.kotlinfirebaselearn.interfaces

import com.example.kotlinfirebaselearn.Notes

interface OnNoteListener {

    fun onClick()
    fun onDelete(note : Notes, position : Int)
}