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
import com.sko.notesapp.Model.Note
import com.sko.notesapp.R

class UpdateActivity : AppCompatActivity() {
    lateinit var editTextTitalUpdate: EditText
    lateinit var editTextDescriptionUpdate: EditText
    lateinit var buttonUpdate: Button
    lateinit var buttonCancel: Button

    var currentId=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notes)

        supportActionBar?.title="Update Note"

        editTextTitalUpdate=findViewById(R.id.editTextTitalUpdate)
        editTextDescriptionUpdate=findViewById(R.id.editTextDescriptionUpdate)
        buttonCancel=findViewById(R.id.buttonCancelUpdate)
        buttonUpdate=findViewById(R.id.buttonUpdate)

        getAndSetData()

        buttonCancel.setOnClickListener {
            Toast.makeText(applicationContext,"Nothing is updated",Toast.LENGTH_LONG).show()
            finish()
        }

        buttonUpdate.setOnClickListener {
            updateNote()
        }
    }

    fun updateNote()
    {
        val updateTitle=editTextTitalUpdate.text.toString()
        val updateDescription=editTextDescriptionUpdate.text.toString()

        val intent=Intent()
        intent.putExtra("updateTitle",updateTitle)
        intent.putExtra("updateDescription",updateDescription)
        if(currentId!=-1){
            intent.putExtra("noteId",currentId)
            setResult(RESULT_OK,intent)
            finish()
        }
    }

    fun getAndSetData(){
        val currentTitle=intent.getStringExtra("currentTitle")
        val currentDescription=intent.getStringExtra("currentDescription")
        currentId=intent.getIntExtra("currentId",-1)

        editTextTitalUpdate.setText(currentTitle)
        editTextDescriptionUpdate.setText(currentDescription)
    }
}