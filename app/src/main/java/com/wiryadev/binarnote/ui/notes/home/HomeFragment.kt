package com.wiryadev.binarnote.ui.notes.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wiryadev.binarnote.R
import com.wiryadev.binarnote.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: HomeViewModel by viewModels()

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

        viewModel.userSession.observe(viewLifecycleOwner) {
            if (it.email.isBlank()) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                )
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        with(binding) {
            fabAddNote.setOnClickListener {
                bottomSheet.tvTitle.text = getString(R.string.add_new_note)
                bottomSheet.btnInput.text = getString(R.string.create_note)

                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    fabAddNote.setImageResource(R.drawable.ic_round_add_24)
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    fabAddNote.setImageResource(R.drawable.ic_round_close_24)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}