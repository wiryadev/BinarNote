package com.wiryadev.binarnote.ui.login

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wiryadev.binarnote.R
import com.wiryadev.binarnote.databinding.FragmentLoginBinding
import com.wiryadev.binarnote.ui.animateAlphaToVisible
import com.wiryadev.binarnote.ui.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val args: LoginFragmentArgs by navArgs()
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        args.email?.let {
            binding.etEmail.setText(it)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.errorMessage?.let {
                binding.root.showSnackbar(it)
            }

            binding.btnLogin.isVisible = !uiState.isLoading

            if (uiState.isLoggedIn) {
                if (findNavController().currentDestination?.id == R.id.loginFragment) {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    )
                }
            } else {
                animateVisibility()
            }
        }

        binding.btnLogin.setOnClickListener {
            checkInputAndRegister()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        with(binding) {
            imageView.alpha = 0f
            tilEmail.alpha = 0f
            tvEmail.alpha = 0f
            tvPassword.alpha = 0f
            tilPassword.alpha = 0f
            btnLogin.alpha = 0f
            btnRegister.alpha = 0f
        }
    }

    private fun animateVisibility() {
        with(binding) {
            AnimatorSet().apply {
                playSequentially(
                    textView.animateAlphaToVisible(),
                    imageView.animateAlphaToVisible(),
                    tilEmail.animateAlphaToVisible(),
                    tvEmail.animateAlphaToVisible(),
                    tvPassword.animateAlphaToVisible(),
                    tilPassword.animateAlphaToVisible(),
                    btnLogin.animateAlphaToVisible(),
                    btnRegister.animateAlphaToVisible(),
                )
                startDelay = 500L
            }.start()
        }
    }

    private fun checkInputAndRegister() {
        with(binding) {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                tilEmail.error != null -> {
                    root.showSnackbar("Email tidak boleh kosong")
                }
                tilPassword.error != null -> {
                    root.showSnackbar("Password tidak boleh kosong")
                }
                else -> {
                    viewModel.login(
                        email = email,
                        password = password,
                    )
                }
            }
        }
    }

}