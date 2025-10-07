package com.ar11.mobilecryptowallet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.isDarkTheme
import com.ar11.mobilecryptowallet.adapter.CryptoInWalletAdapter
import com.ar11.mobilecryptowallet.adapter.OnCryptoInteractionListener
import com.ar11.mobilecryptowallet.databinding.FragmentWalletViewBinding
import com.ar11.mobilecryptowallet.dto.CryptosModel
import com.ar11.mobilecryptowallet.util.CryptosBooleanArg
import com.ar11.mobilecryptowallet.util.CryptosBooleanArg.getValue
import com.ar11.mobilecryptowallet.util.CryptosBooleanArg.setValue
import com.ar11.mobilecryptowallet.util.StringArg
import com.ar11.mobilecryptowallet.viewmodel.WalletsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ViewWalletFragment : Fragment() {

    companion object {
        var Bundle.userIdView: String? by StringArg
        var Bundle.walletNameView: String? by StringArg
    }

    private val viewModel: WalletsViewModel by activityViewModels()

    private var fragmentBinding: FragmentWalletViewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletViewBinding.inflate(inflater, container, false)
        fragmentBinding = binding

        val context = this.context;

        var wallet = viewModel.getNewOrEdit()

        binding.inputWalletName.text = wallet?.walletName
        binding.inputWalletDescription.text = wallet?.walletDescription
        binding.cryptosAmountValue.text =  wallet?.cryptosCount.toString()
        binding.cryptosCostValue.text = wallet?.cryptosCost.toString()



        val adapter = CryptoInWalletAdapter(object : OnCryptoInteractionListener {
            override fun onEdit(crypto: CryptosModel) {

            }
            override fun onRemove(crypto: CryptosModel) {

            }
            override fun onView(crypto: CryptosModel) {

            }

            override fun isMenuActive(): Boolean {
                return false
            }

        })
        binding.list.adapter = adapter

        adapter.submitList(wallet?.cryptocurrenciesList)

        viewModel.updatedWallet.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.dataState.observe(viewLifecycleOwner) {
            if (it.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.ok) {

                    }
                    .show()
            }
        }


        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.one_wallet_menu_view)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.back -> {
                            findNavController().navigateUp()
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }


        return binding.root
    }
}
