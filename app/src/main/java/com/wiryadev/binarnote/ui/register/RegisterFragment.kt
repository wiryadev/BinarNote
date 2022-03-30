package com.wiryadev.binarnote.ui.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wiryadev.binarnote.data.local.entity.UserEntity
import com.wiryadev.binarnote.databinding.FragmentRegisterBinding
import com.wiryadev.binarnote.ui.addErrorListener
import com.wiryadev.binarnote.ui.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()
    private var emailToBeSent = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()

        binding.etEmail.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                text?.let {
                    viewModel.queryChannel.value = it.toString()
                }
            }
        )

        viewModel.checkUserExist.observe(viewLifecycleOwner) {
            if (it > 0) {
                binding.tilEmail.error = "Email sudah ada"
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.errorMessage?.let {
                binding.root.showSnackbar(it)
            }

            binding.btnRegister.isVisible = !uiState.isLoading

            if (uiState.isSuccess) {
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToRegisterSuccessFragment(
                        email = emailToBeSent
                    )
                )
            }
        }

        binding.btnRegister.setOnClickListener {
            checkInputAndRegister()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        with(binding) {
            etUsername.addErrorListener("Username")
            etEmail.addErrorListener("Email") {
                Patterns.EMAIL_ADDRESS.matcher(it).matches()
            }
            etPassword.addErrorListener("Password")
            etConfirmPassword.addErrorListener("Confirm Password") {
                it == etPassword.text.toString()
            }
        }
    }

    private fun checkInputAndRegister() {
        with(binding) {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                tilUsername.error != null -> {
                    root.showSnackbar("Username tidak boleh kosong")
                }
                tilEmail.error != null -> {
                    root.showSnackbar("Email tidak boleh kosong")
                }
                tilPassword.error != null -> {
                    root.showSnackbar("Password tidak boleh kosong")
                }
                tilConfirmPassword.error != null -> {
                    root.showSnackbar("Pastikan konfirmasi password sesuai")
                }
                else -> {
                    emailToBeSent = email
                    viewModel.register(
                        user = UserEntity(
                            email = email,
                            username = username,
                            password = password,
                        )
                    )
                }
            }
        }
    }

}