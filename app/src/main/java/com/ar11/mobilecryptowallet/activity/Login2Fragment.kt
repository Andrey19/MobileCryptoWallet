package com.ar11.mobilecryptowallet.activity

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.viewmodel.Login2ViewModel
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.FragmentLogin2Binding
import com.ar11.mobilecryptowallet.util.AndroidUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class Login2Fragment : Fragment() {

    private val login2ViewModel: Login2ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLogin2Binding.inflate(inflater, container, false)

        binding.btnlogin.setOnClickListener {
            login2ViewModel.usersLogin2(binding.inputLogin.text.toString(), binding.inputPassword.text.toString())
            AndroidUtils.hideKeyboard(requireView())
        }

        login2ViewModel.userLogin2.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.userInfoFragment)
            } else {
                Snackbar.make(binding.root, R.string.auth_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok) { }
                    .show()
            }
        }

        binding.btnMoveToRegister.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)
        }

        return binding.root
    }

}