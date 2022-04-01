package com.wiryadev.binarnote.ui.notes.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.wiryadev.binarnote.R
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import com.wiryadev.binarnote.databinding.FragmentHomeBinding
import com.wiryadev.binarnote.ui.formatDisplayDate
import com.wiryadev.binarnote.ui.showSnackbar
import com.wiryadev.binarnote.ui.simpleDateFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        prepareRecyclerView()
        handleBackPress()

        viewModel.userSession.observe(viewLifecycleOwner) {
            if (it.email.isBlank()) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                )
            } else {
                binding.tvUsername.text = getString(R.string.hi_username, it.username)
                if (it.email != email) {
                    email = it.email
                    viewModel.getNoteByEmail(email = email)
                }
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            with(binding) {
                progressBar.isVisible = uiState.isLoading
            }

            binding.emptyState.root.visibility =
                if (uiState.notes.isEmpty() && !uiState.isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            if (!uiState.isLoading) {
                noteAdapter.submitData(uiState.notes)
            }

            if (uiState.result > 0) {
                when (uiState.action) {
                    Action.CREATE -> {
                        binding.root.showSnackbar(getString(R.string.create_note_success))
                        collapseBottomSheet()
                    }
                    Action.UPDATE -> {
                        binding.root.showSnackbar(getString(R.string.update_note_success))
                        collapseBottomSheet()
                        noteAdapter.notifyDataSetChanged()
                    }
                    Action.DELETE -> {
                        binding.root.showSnackbar(getString(R.string.delete_note_success))
                        collapseBottomSheet()
                    }
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        with(binding) {
            fabAddNote.setOnClickListener {
                bottomSheet.tvTitle.text = getString(R.string.add_new_note)
                bottomSheet.btnInput.text = getString(R.string.create)

                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    collapseBottomSheet()
                } else {
                    expandBottomSheet()
                }
                handleCreateBottomSheet()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        collapseBottomSheet()
                    } else {
                        activity?.finish()
                    }
                }
            }
        )
    }

    private fun expandBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.fabAddNote.setImageResource(R.drawable.ic_round_close_24)
    }

    private fun collapseBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.fabAddNote.setImageResource(R.drawable.ic_round_add_24)
    }

    private fun prepareRecyclerView() {
        noteAdapter = NoteAdapter(
            onEditClickListener = {
                expandBottomSheet()
                handleUpdateBottomSheet(note = it)
            },
            onDeleteClickListener = {
                expandBottomSheet()
                handleDeleteBottomSheet(note = it)
            }
        )
        val noteLayoutManager = LinearLayoutManager(context)
        binding.rvNotes.apply {
            layoutManager = noteLayoutManager
            adapter = noteAdapter
            setHasFixedSize(true)
        }
    }

    private fun handleCreateBottomSheet() {
        var dateForDatabase = ""
        with(binding.bottomSheet) {
            etDate.text?.clear()
            etLogbook.text?.clear()

            etDate.isEnabled = true
            etLogbook.isEnabled = true

            etDate.setOnClickListener {
                val datePicker = buildDatePicker()
                datePicker.addOnPositiveButtonClickListener { selection ->
                    val date = Date(selection)
                    dateForDatabase = simpleDateFormat.format(date)
                    etDate.setText(dateForDatabase.formatDisplayDate())
                }
                datePicker.show(childFragmentManager, "date")
            }

            btnInput.setOnClickListener {
                val logbook = etLogbook.text.toString().trim()
                when {
                    dateForDatabase.trim().isBlank() -> {
                        this.root.showSnackbar("Date must be filled")
                    }
                    logbook.isBlank() -> {
                        this.root.showSnackbar("Logbook must be filled")
                    }
                    else -> {
                        val newNote = NoteEntity(
                            date = dateForDatabase,
                            logbook = logbook,
                            owner = email,
                        )
                        viewModel.createNote(note = newNote)
                    }
                }
            }
        }
    }

    private fun handleUpdateBottomSheet(note: NoteEntity) {
        with(binding.bottomSheet) {
            etDate.isEnabled = false
            etLogbook.isEnabled = true

            tvTitle.text = getString(R.string.update_note)
            btnInput.text = getString(R.string.update)

            etDate.setText(note.date.formatDisplayDate())
            etLogbook.setText(note.logbook)

            btnInput.setOnClickListener {
                val logbook = etLogbook.text.toString().trim()
                when {
                    etDate.text.toString().trim().isBlank() -> {
                        this.root.showSnackbar("Date must be filled")
                    }
                    logbook.isBlank() -> {
                        this.root.showSnackbar("Logbook must be filled")
                    }
                    else -> {
                        note.logbook = logbook
                        viewModel.updateNote(note)
                    }
                }
            }
        }
    }

    private fun handleDeleteBottomSheet(note: NoteEntity) {
        with(binding.bottomSheet) {
            etDate.isEnabled = false
            etLogbook.isEnabled = false

            tvTitle.text = getString(R.string.delete_note)
            btnInput.text = getString(R.string.delete)

            etDate.setText(note.date.formatDisplayDate())
            etLogbook.setText(note.logbook)

            btnInput.setOnClickListener {
                viewModel.deleteNote(note)
            }
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())

        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

}