package com.example.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.NoteItemBinding
import com.example.notes.models.NoteResponse


class NoteAdapter(private val onNoteClicked: (NoteResponse) -> Unit) : ListAdapter<NoteResponse, NoteAdapter.NoteViewHolder>(ComparatorDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note : NoteResponse) {
            binding.title.text = note.title
            binding.description.text = note.description
            binding.root.setOnClickListener {// when any note is clicked
                onNoteClicked(note)
            }
        }
    }

    class ComparatorDiffUtil() : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }

    }
}