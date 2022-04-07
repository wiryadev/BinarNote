package com.wiryadev.binarnote.ui.notes.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import com.wiryadev.binarnote.databinding.ItemNoteBinding
import com.wiryadev.binarnote.ui.formatDisplayDate

class NoteAdapter(
    private val onEditClickListener: (NoteEntity) -> Unit,
    private val onDeleteClickListener: (NoteEntity) -> Unit,
) : ListAdapter<NoteEntity, NoteAdapter.NoteViewHolder>(NOTE_COMPARATOR) {

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
        val note = getItem(position)
        holder.bind(note)
    }

    companion object {
        private val NOTE_COMPARATOR = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(
                oldItem: NoteEntity,
                newItem: NoteEntity,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NoteEntity,
                newItem: NoteEntity
            ): Boolean {
                return oldItem.logbook == newItem.logbook
            }
        }
    }

}