package com.sko.notesapp.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sko.notesapp.R

class NoteAddActivity : AppCompatActivity() {
    lateinit var editTextTital:EditText
    lateinit var editTextDescription: EditText
    lateinit var buttonSave:Button
    lateinit var buttonCancel:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)

        supportActionBar?.title="Add Notes"

        editTextTital=findViewById(R.id.editTextTital)
        editTextDescription=findViewById(R.id.editTextDescription)
        buttonCancel=findViewById(R.id.buttonCancel)
        buttonSave=findViewById(R.id.buttonSave)

        buttonCancel.setOnClickListener {
            Toast.makeText(applicationContext,"Nothing is saved",Toast.LENGTH_LONG).show()
            finish()
        }

        buttonSave.setOnClickListener {
            saveNote()
        }

    }
    fun saveNote(){
        val noteTital:String=editTextTital.text.toString()
        val noteDescription:String=editTextDescription.text.toString()

        val intent=Intent()
        intent.putExtra("title",noteTital)
        intent.putExtra("description",noteDescription)
        setResult(RESULT_OK,intent)
        finish()
    }
}