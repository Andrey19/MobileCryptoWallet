package com.ar11.mobilecryptowallet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.RegisterFragmentBinding
import com.ar11.mobilecryptowallet.util.AndroidUtils
import com.ar11.mobilecryptowallet.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class RegisterFragment: Fragment() {
    private val registerViewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RegisterFragmentBinding.inflate(inflater, container, false)

        binding.btnRegister.setOnClickListener {
            registerViewModel.usersRegister(binding.inputLogin.text.toString(),
                binding.inputPassword.text.toString(), binding.inputUsername.text.toString())
            AndroidUtils.hideKeyboard(requireView())
        }

        registerViewModel.userRegister.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.userInfoFragment)
            } else {
                Snackbar.make(binding.root, R.string.register_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok) { }
                    .show()
            }
        }


        return binding.root
    }
}
