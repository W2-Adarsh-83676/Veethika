package com.sko.notesapp.View

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sko.notesapp.Adapter.NoteAdapter
import com.sko.notesapp.Model.Note
import com.sko.notesapp.NoteApplication
import com.sko.notesapp.R
import com.sko.notesapp.ViewModel.NoteViewModel
import com.sko.notesapp.ViewModel.NoteViewModelFactory
import java.util.Objects

class MainActivity : AppCompatActivity() {
    lateinit var noteViewModel: NoteViewModel
    lateinit var addActivityResultLauncher:ActivityResultLauncher<Intent>
    lateinit var updateActivityResultLauncher:ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView:RecyclerView=findViewById(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this)
        val noteAdapter=NoteAdapter(this)
        recyclerView.adapter=noteAdapter

        registerActivityResultLauncher()

        val viewModelFactory=NoteViewModelFactory((application as NoteApplication).repository)
        noteViewModel=ViewModelProvider(this,viewModelFactory).get(NoteViewModel::class.java)
        noteViewModel.myAllNotes.observe(this, Observer {
            notes->noteAdapter.setNote(notes)
        })

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT
        or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(noteAdapter.getNote(viewHolder.adapterPosition))
            }

        }).attachToRecyclerView(recyclerView)
    }

    fun registerActivityResultLauncher(){
        addActivityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                resultAddNote->
                val resultCode=resultAddNote.resultCode
                val data=resultAddNote.data

                if(resultCode== RESULT_OK && data!= null){
                    val noteTital:String=data.getStringExtra("title").toString()
                    val noteDiscription:String=data.getStringExtra("description").toString()

                    val note=Note(noteTital,noteDiscription)
                    noteViewModel.insert(note)
                }
            })

        updateActivityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                    resultUpdateNote->
                val resultCode=resultUpdateNote.resultCode
                val data=resultUpdateNote.data

                if(resultCode== RESULT_OK && data!= null){
                    val updateTitle:String=data.getStringExtra("updateTitle").toString()
                    val updateDescription:String=data.getStringExtra("updateDescription").toString()
                    val noteId=data.getIntExtra("noteId",-1)

                    val newNote=Note(updateTitle,updateDescription)
                    newNote.id=noteId
                    noteViewModel.update(newNote)

                }
            })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_add_note->{
                val intent=Intent(this,NoteAddActivity::class.java)
                addActivityResultLauncher.launch(intent)
            }
            R.id.item_delete_all_notes->showDailog()

        }
        return true
    }

    fun showDailog(){
        val dailog=AlertDialog.Builder(this)

        dailog.setTitle("Delete all notes")
        dailog.setMessage("If click yes all notes delete")
        dailog.setNegativeButton("No",DialogInterface.OnClickListener{
            dialog,which->
            dialog.cancel()
        })
        dailog.setPositiveButton("Yes",DialogInterface.OnClickListener{
                dialog,which->noteViewModel.deleteAllNotes()
        })

        dailog.create().show()
    }
}