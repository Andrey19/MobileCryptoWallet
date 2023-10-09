package com.ar11.mobilecryptowallet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.LoginFragmentBinding
import com.ar11.mobilecryptowallet.util.AndroidUtils
import com.ar11.mobilecryptowallet.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LoginFragmentBinding.inflate(inflater, container, false)

        binding.btnlogin.setOnClickListener {
            loginViewModel.usersLogin(binding.inputLogin.text.toString(), binding.inputPassword.text.toString())
            AndroidUtils.hideKeyboard(requireView())
        }

        loginViewModel.userLogin.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            } else {
                Snackbar.make(binding.root, R.string.auth_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok) { }
                    .show()
            }
        }

        return binding.root
    }
}


