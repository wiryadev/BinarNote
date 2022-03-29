package com.wiryadev.binarnote.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.wiryadev.binarnote.R
import com.wiryadev.binarnote.data.local.entity.UserEntity
import com.wiryadev.binarnote.databinding.FragmentRegisterBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.btnRegister.setOnClickListener {
            checkInputAndRegister()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkInputAndRegister() {
        with(binding) {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            when {
                username.isBlank() -> {
                    tilUsername.error = "Username tidak boleh kosong"
                }
                email.isBlank() -> {
                    tilEmail.error = "Email tidak boleh kosong"
                }
                password.isBlank() -> {
                    tilPassword.error = "Password tidak boleh kosong"
                }
                confirmPassword != password -> {
                    tilConfirmPassword.error = "Pastikan konfirmasi password sesuai"
                }
                else -> {
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