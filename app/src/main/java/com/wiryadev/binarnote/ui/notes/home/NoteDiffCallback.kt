package com.wiryadev.binarnote.ui.notes.home

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.wiryadev.binarnote.data.local.entity.NoteEntity

class NoteDiffCallback(
    private val oldNotes: List<NoteEntity>,
    private val newNotes: List<NoteEntity>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldNotes.size

    override fun getNewListSize(): Int = newNotes.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        Log.d(
            "NoteAdapter",
            "areItemsTheSame: ${oldNotes[oldItemPosition].id == newNotes[newItemPosition].id}"
        )
        return oldNotes[oldItemPosition].id == newNotes[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldNotes[oldItemPosition]
        val newItem = newNotes[newItemPosition]
        Log.d("NoteAdapter", "areContentsTheSame: ${oldItem.logbook == newItem.logbook}")
        return oldItem.hashCode() == newItem.hashCode() && oldItem.logbook == newItem.logbook
    }
}