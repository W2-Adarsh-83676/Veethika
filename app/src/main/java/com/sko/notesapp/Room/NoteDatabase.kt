package com.sko.notesapp.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sko.notesapp.Model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase:RoomDatabase() {

    abstract fun getNoteDao():NoteDao

    //singleton
    companion object{
        @Volatile
        private var INSTANCE:NoteDatabase?=null

        fun getDatabase(context: Context,scope: CoroutineScope): NoteDatabase{
            return INSTANCE?: synchronized(this){
                val instances = Room.databaseBuilder(context.applicationContext,   // Pass the application context
                    NoteDatabase::class.java,     // The class of the database
                    "note_database"               // Name of the database file
                ).addCallback(NoteDatabaseCallback(scope))
                    .build()

                INSTANCE=instances

                instances
            }

        }
    }

    private class NoteDatabaseCallback(private val scope:CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE.let { database ->
               scope.launch {
                   val noteDao=database?.getNoteDao()
                   noteDao?.insert(Note("Title 1","Description 1"))
                   noteDao?.insert(Note("Title 2","Description 2"))
                   noteDao?.insert(Note("Title 3","Description 3"))
               }
            }
        }
    }
}