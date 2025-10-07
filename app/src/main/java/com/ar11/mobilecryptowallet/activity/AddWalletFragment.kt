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
import com.ar11.mobilecryptowallet.databinding.FragmentWalletAddBinding
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.util.AndroidUtils
import com.ar11.mobilecryptowallet.util.StringArg
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.ar11.mobilecryptowallet.viewmodel.WalletsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AddWalletFragment : Fragment() {

    companion object {
        var Bundle.userId: String? by StringArg
    }


    private val viewModel: WalletsViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    private var fragmentBinding: FragmentWalletAddBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletAddBinding.inflate(inflater, container, false)
        fragmentBinding = binding

        val context = this.context;

        var wallet = viewModel.getNewOrEdit()

        binding.inputWalletName.setText(wallet?.walletName)
        binding.inputWalletDescription.setText(wallet?.walletDescription)
        binding.cryptosAmountValue.text = wallet?.cryptosCount.toString()
        binding.cryptosCostValue.text = wallet?.cryptosCost.toString()

        val theme = viewModelCrypto.getTheme()
        if (theme){
            binding.adWalFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
        } else {
            binding.adWalFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }


//        val adapter = CryptoInWalletAdapter(object : OnCryptoInteractionListener {
//            override fun onEdit(crypto: CryptosModel) {
//
//            }
//            override fun onRemove(crypto: CryptosModel) {
//
//            }
//            override fun onView(crypto: CryptosModel) {
//
//            }
//
//            override fun isMenuActive(): Boolean {
//                return false
//            }
//
//        })
//        binding.list.adapter = adapter
//
//        adapter.submitList(wallet?.cryptocurrenciesList)

        viewModel.addedWallet.observe(viewLifecycleOwner) {
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
                inflate(R.menu.one_wallet_menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.save -> {
                            viewModel.setNewOrEdit(
                                WalletsModel(wallet!!.userId, binding.inputWalletName.text.toString(),
                                binding.inputWalletDescription.text.toString(), wallet.cryptosCount, wallet.cryptosCost,
                                wallet.cryptocurrenciesList
                            )
                            )
                            viewModel.saveWallet()
                            AndroidUtils.hideKeyboard(requireView())
                            true
                        }
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
