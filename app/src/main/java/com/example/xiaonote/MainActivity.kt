package com.example.xiaonote

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xiaonote.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: Folder

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        val db = SQLite(this).writableDatabase
        adapter = Folder(db)
        val folderCallback = FolderTouch(adapter)
        val folderTouchHelper = ItemTouchHelper(folderCallback)
        binding.folders.adapter = adapter
        binding.folders.layoutManager = LinearLayoutManager(this)
        folderTouchHelper.attachToRecyclerView(binding.folders)
        binding.button.setOnClickListener(createNote())
    }

    private fun createNote() = View.OnClickListener {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Add Folder")
            .setView(editText)
            .setNegativeButton("Cancel") { a, _ ->
                a.dismiss()
            }
            .setPositiveButton("Add") { _, _ ->
                adapter.create(editText.text.toString())
            }
            .show()
    }
}

class FolderData(val name: String, val _id: String) {
    private val notes = mutableListOf<NotesData>()

    fun createNote(context: String) {
        notes.add(NotesData(context))
    }
}

data class NotesData(val context: String)

class Folder(private val db: SQLiteDatabase) : RecyclerView.Adapter<Folder.FolderHolder>() {
    private val folders = mutableListOf<FolderData>()

    init {
        val cr = db.query("FOLDER", arrayOf("_id", "name"), null, null, null, null, null)
        while(cr.moveToNext()) {
            val id = cr.getString(0)
            val name = cr.getString(1)
            folders.add(FolderData(name, id))
        }
    }

    class FolderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(folderName: String) {
            itemView.findViewById<TextView>(R.id.text).text = folderName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_item, null)
        view.setOnClickListener {

        }
        return FolderHolder(view)
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        holder.bind(folders[position].name)
    }

    override fun getItemCount(): Int = folders.size

    fun move(start: Int, end: Int) {
        val data = folders[start]
        folders.removeAt(start)
        folders.add(data)

        notifyItemMoved(start, end)
    }

    fun delete(pos: Int) {
        folders.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun create(name: String) {
        val uuid = UUID.randomUUID().toString()
        val value = ContentValues().apply {
            put("NAME", name)
            put("_id", uuid)
        }
        folders.add(FolderData(name, uuid))
        db.insert("FOLDER", null, value)
    }
}

class FolderTouch(private val adapter: Folder) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.move(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.delete(viewHolder.adapterPosition)
    }
}