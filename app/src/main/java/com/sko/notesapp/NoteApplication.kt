package com.sko.notesapp

import android.app.Application
import androidx.room.Database
import com.sko.notesapp.Model.Repository.NoteRepository
import com.sko.notesapp.Room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NoteApplication :Application(){

    val applicationScope= CoroutineScope(SupervisorJob())
    val database by lazy { NoteDatabase.getDatabase(this,applicationScope)}
    val repository by lazy { NoteRepository(database.getNoteDao()) }
}