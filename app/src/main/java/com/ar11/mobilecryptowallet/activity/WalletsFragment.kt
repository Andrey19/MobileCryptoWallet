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
import com.ar11.mobilecryptowallet.activity.AddWalletFragment.Companion.userId
import com.ar11.mobilecryptowallet.activity.EditWalletFragment.Companion.userIdEdit
import com.ar11.mobilecryptowallet.activity.EditWalletFragment.Companion.walletNameEdit
import com.ar11.mobilecryptowallet.adapter.WalletOnInteractionListener
import com.ar11.mobilecryptowallet.adapter.WalletsAdapter
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.databinding.FragmentWalletsBinding
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.ar11.mobilecryptowallet.viewmodel.WalletsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class WalletsFragment : Fragment() {

    @Inject
    lateinit var auth2: AppAuth2

    private val viewModel: WalletsViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletsBinding.inflate(inflater, container, false)

        val adapter = WalletsAdapter(object : WalletOnInteractionListener {
            override fun onEdit(wallet: WalletsModel) {
                viewModel.setNewOrEdit(wallet)

                findNavController().navigate(
                    R.id.editWalletFragment,
                    Bundle().apply {
                        userIdEdit = wallet.userId
                        walletNameEdit = wallet.walletName
                    }
                )
            }

            override fun onView(wallet: WalletsModel) {
                viewModel.setNewOrEdit(wallet)

                findNavController().navigate(
                    R.id.viewWalletFragment,
                    Bundle().apply {
                        userIdEdit = wallet.userId
                        walletNameEdit = wallet.walletName
                    }
                )
            }


            override fun onRemove(wallet: WalletsModel) {
                viewModel.deleteWallet(wallet)
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
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) {
                        if (auth2.authStateFlow2.value.email != null) {
                            viewModel.loadWallets(auth2.authStateFlow2.value.email!!)
                        }

                    }
                    .show()
            }

        }

        val theme = viewModelCrypto.getTheme()
        if (theme){
            binding.wFr.background = ContextCompat.getDrawable(requireContext(), R.color.colorPrimaryDark)
        } else {
            binding.wFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }

        viewModel.deletedWallet.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.data.value)
        }


        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state)
            binding.emptyText.isVisible = state.isEmpty()
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshWallets(auth2.authStateFlow2.value.email!!)
        }


        binding.fab.setOnClickListener {
            viewModel.setNewOrEdit(WalletsModel(userId = auth2.authStateFlow2.value.email!!))

            findNavController().navigate(
                R.id.addWalletFragment,
                Bundle().apply {
                    userId = auth2.authStateFlow2.value.email!!
                }
            )
        }


        return binding.root
    }
}
