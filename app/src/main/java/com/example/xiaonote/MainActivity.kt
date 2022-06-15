package com.example.xiaonote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xiaonote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.button.setOnClickListener(createNote())
    }

    private fun createNote() = View.OnClickListener {

    }
}

class FolderData(val name: String) {
    private val notes = mutableListOf<NotesData>()

    fun createNote(context: String) {
        notes.add(NotesData(context))
    }
}

data class NotesData(val context: String)

class Folder : RecyclerView.Adapter<Folder.FolderHolder>() {
    val folders = mutableListOf<FolderData>()

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
}