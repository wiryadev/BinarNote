package com.wiryadev.binarnote.ui.notes.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import com.wiryadev.binarnote.databinding.ItemNoteBinding
import com.wiryadev.binarnote.ui.formatDisplayDate

class NoteAdapter(
    private val onEditClickListener: (NoteEntity) -> Unit,
    private val onDeleteClickListener: (NoteEntity) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(data: List<NoteEntity>) = differ.submitList(data)

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
        val note = differ.currentList[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = differ.currentList.size

}