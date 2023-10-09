package com.ar11.mobilecryptowallet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.activity.AddWalletFragment.Companion.userId
import com.ar11.mobilecryptowallet.adapter.WalletOnInteractionListener
import com.ar11.mobilecryptowallet.adapter.WalletsAdapter
import com.ar11.mobilecryptowallet.auth.AppAuth
import com.ar11.mobilecryptowallet.databinding.FragmentWalletsBinding
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.viewmodel.WalletsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class WalletsFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    private val viewModel: WalletsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletsBinding.inflate(inflater, container, false)

        val adapter = WalletsAdapter(object : WalletOnInteractionListener {
            override fun onEdit(wallet: WalletsModel) {
                viewModel.setNewOrEdit(wallet)

//                findNavController().navigate(
//                    R.id.editWalletFragment,
//                    Bundle().apply {
//                        userIdEdit = wallet.userId
//                        walletNameEdit = wallet.walletName
//                    }
//                )
            }

            override fun onView(wallet: WalletsModel) {
                viewModel.setNewOrEdit(wallet)

                findNavController().navigate(
                    R.id.viewWalletFragment,
                    Bundle().apply {
//                        userIdEdit = wallet.userId
//                        walletNameEdit = wallet.walletName
                    }
                )
            }


            override fun onRemove(wallet: WalletsModel) {
                viewModel.deleteWallet(wallet)
            }

        })
        binding.list.adapter = adapter
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) {
                        if (auth.authStateFlow.value.email != null) {
                            viewModel.loadWallets(auth.authStateFlow.value.email!!)
                        }

                    }
                    .show()
            }
        }

        viewModel.deletedWallet.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.data.value)
        }


        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state)
            binding.emptyText.isVisible = state.isEmpty()
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshWallets(auth.authStateFlow.value.email!!)
        }

        binding.fab.setOnClickListener {
            viewModel.setNewOrEdit(WalletsModel(userId = auth.authStateFlow.value.email!!))

            findNavController().navigate(
                R.id.addWalletFragment,
                Bundle().apply {
                    userId = auth.authStateFlow.value.email!!
                }
            )
        }

        return binding.root
    }
}
