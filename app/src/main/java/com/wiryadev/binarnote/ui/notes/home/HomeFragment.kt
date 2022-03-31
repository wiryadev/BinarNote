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
import java.util.Date


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

    private var email = ""
    private var action = DbAction.READ

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

            if (uiState.notes.isNotEmpty()) {
                noteAdapter.submitData(uiState.notes)
            }

            if (uiState.isSuccess and (action == DbAction.CREATE)) {
                collapseBottomSheet()
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        initBottomSheetListener()

        with(binding) {
            fabAddNote.setOnClickListener {
                bottomSheet.tvTitle.text = getString(R.string.add_new_note)
                bottomSheet.btnInput.text = getString(R.string.create_note)

                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    collapseBottomSheet()
                    DbAction.READ
                } else {
                    expandBottomSheet(DbAction.CREATE)
                }
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

    private fun expandBottomSheet(dbAction: DbAction) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.fabAddNote.setImageResource(R.drawable.ic_round_close_24)
        action = dbAction
    }

    private fun collapseBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.fabAddNote.setImageResource(R.drawable.ic_round_add_24)
        action = DbAction.READ
    }

    private fun prepareRecyclerView() {
        noteAdapter = NoteAdapter()
        val noteLayoutManager = LinearLayoutManager(context)
        binding.rvNotes.apply {
            layoutManager = noteLayoutManager
            adapter = noteAdapter
        }
    }

    private fun initBottomSheetListener() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())

        var dateForDatabase = ""

        with(binding.bottomSheet) {
            etDate.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build()
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
                        when (action) {
                            DbAction.CREATE -> {
                                // add note
                                viewModel.createNote(
                                    note = NoteEntity(
                                        date = dateForDatabase,
                                        logbook = logbook,
                                        owner = email,
                                    )
                                )
                            }
                            DbAction.UPDATE -> {
                                // update
                            }
                            DbAction.DELETE -> {
                                // delete
                            }
                            else -> {
                                // nothing
                            }
                        }
                    }
                }
            }
        }
    }

}

enum class DbAction {
    CREATE, READ, UPDATE, DELETE,
}