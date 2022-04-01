package com.wiryadev.binarnote.ui.notes.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import com.wiryadev.binarnote.databinding.ItemNoteBinding
import com.wiryadev.binarnote.ui.formatDisplayDate

class NoteAdapter(
    private val onEditClickListener: (NoteEntity) -> Unit,
    private val onDeleteClickListener: (NoteEntity) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val notes = mutableListOf<NoteEntity>()

    fun submitData(newNotes: List<NoteEntity>) {
        Log.d("NoteAdapter", "submitData: $newNotes")
        val diffCallback = NoteDiffCallback(this.notes, newNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.notes.clear()
        this.notes.addAll(newNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteEntity) {
            with(binding) {
                tvTitle.text = note.date.formatDisplayDate()
                tvDesc.text = note.logbook
                btnEdit.setOnClickListener {
                    onEditClickListener.invoke(note)
                }
                btnDelete.setOnClickListener {
                    onDeleteClickListener.invoke(note)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = notes.size

}