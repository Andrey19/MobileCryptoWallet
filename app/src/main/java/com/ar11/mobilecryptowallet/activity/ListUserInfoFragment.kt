package com.ar11.mobilecryptowallet.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoAmount
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoCost
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoDescription
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoName
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.image
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.imageUrl
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.viewType
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.avatar
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.avatarUrl
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.email
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.isAdmin
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.name
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.password
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.role
import com.ar11.mobilecryptowallet.adapter.CryptosAdapter
import com.ar11.mobilecryptowallet.adapter.OnInteractionListener
import com.ar11.mobilecryptowallet.adapter.OnInteractionListenerUser
import com.ar11.mobilecryptowallet.adapter.UsersAdapter
import com.ar11.mobilecryptowallet.databinding.FragmentUserInfoListBinding
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.model.UserModel2
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.ar11.mobilecryptowallet.viewmodel.ListUserInfoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ListUserInfoFragment : Fragment() {

    private val viewModel: ListUserInfoViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserInfoListBinding.inflate(inflater, container, false)

        val adapter = UsersAdapter(object : OnInteractionListenerUser {
            override fun onView(user: UserModel2) {
                findNavController().navigate(
                    R.id.userDetailFragment,
                    Bundle().apply {
                        email = user.email
                        name = user.name
                        avatar = user.avatar
                        avatarUrl = user.avatarUrl
                        password = user.name
                        isAdmin = user.isAdmin
                        role = user.role
                    }
                )
            }

            override fun getTheme(): Drawable? {
                if (viewModelCrypto.getTheme()){
                    return ContextCompat.getDrawable(requireContext(), R.color.colorPrimaryDark)
                } else{
                    return ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
                }
            }
        })



        binding.list.adapter = adapter

        viewModel.refreshUsers()

        val theme = viewModelCrypto.getTheme()
        if (theme){
            binding.listUsFr.background = ContextCompat.getDrawable(requireContext(), R.color.colorPrimaryDark)
        } else {
            binding.listUsFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }

        viewModel.usersListData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshUsers()
        }

        viewModel.dataState.observe(viewLifecycleOwner){
            if (it.loading){
                binding.swiperefresh.isRefreshing = true
            } else if(!it.refreshing){
                binding.swiperefresh.isRefreshing = false
            }
        }

        return binding.root
    }
}