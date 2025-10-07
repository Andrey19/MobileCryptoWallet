package com.ar11.mobilecryptowallet.activity

import android.graphics.drawable.Drawable
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
import com.ar11.mobilecryptowallet.activity.AddCryptoWalletFragment.Companion.userId
import com.ar11.mobilecryptowallet.activity.AddCryptoWalletFragment.Companion.walletName
import com.ar11.mobilecryptowallet.activity.EditCryptoWalletFragment.Companion.userIdEditCrypto
import com.ar11.mobilecryptowallet.activity.EditCryptoWalletFragment.Companion.walletNameEditCrypto
import com.ar11.mobilecryptowallet.activity.ViewCryptoWalletFragment.Companion.userIdViewCrypto
import com.ar11.mobilecryptowallet.activity.ViewCryptoWalletFragment.Companion.walletNameViewCrypto
import com.ar11.mobilecryptowallet.adapter.CryptoInWalletAdapter
import com.ar11.mobilecryptowallet.adapter.OnCryptoInteractionListener
import com.ar11.mobilecryptowallet.databinding.FragmentWalletEditBinding
import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.CryptosModel
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.util.AndroidUtils
import com.ar11.mobilecryptowallet.util.StringArg
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.ar11.mobilecryptowallet.viewmodel.WalletsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class EditWalletFragment : Fragment() {

    companion object {
        var Bundle.userIdEdit: String? by StringArg
        var Bundle.walletNameEdit: String? by StringArg
    }

    private val viewModel: WalletsViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    private var fragmentBinding: FragmentWalletEditBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletEditBinding.inflate(inflater, container, false)
        fragmentBinding = binding

        val context = this.context

        var wallet = viewModel.getNewOrEdit()

        binding.inputWalletName.text = wallet?.walletName
        binding.inputWalletDescription.setText(wallet?.walletDescription)
        binding.cryptosAmountValue.text = wallet?.cryptosCount.toString()
        binding.cryptosCostValue.text = wallet?.cryptosCost.toString()

        val theme = viewModelCrypto.getTheme()
        if (theme){
            binding.edWalFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
        } else {
            binding.edWalFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }


        val adapter = CryptoInWalletAdapter(object : OnCryptoInteractionListener {
            override fun onEdit(crypto: CryptosModel) {
                viewModel.setNewOrEditCrypto(
                    CryptoInWalletRequest(
                    userId = wallet!!.userId,
                    walletName = wallet!!.walletName,
                    cryptoName = crypto.cryptoName,
                    cryptoType = crypto.cryptoType,
                    image = crypto.image,
                    imageUrl = crypto.imageUrl,
                    cryptoDescription = crypto.cryptoDescription,
                    cryptoAmount = crypto.cryptoAmount,
                    cryptoCost = crypto.cryptoCost
                )
                )

                findNavController().navigate(
                    R.id.editCryptoWalletFragment,
                    Bundle().apply {
                        userIdEditCrypto = wallet!!.userId
                        walletNameEditCrypto = wallet!!.walletName
                    }
                )
            }
            override fun onRemove(crypto: CryptosModel) {
                viewModel.deleteCryptoInWallet(CryptoInWalletRequest(
                    userId = wallet!!.userId,
                    walletName = wallet!!.walletName,
                    cryptoName = crypto.cryptoName,
                    cryptoType = crypto.cryptoType,
                    image = crypto.image,
                    imageUrl = crypto.imageUrl,
                    cryptoDescription = crypto.cryptoDescription,
                    cryptoAmount = crypto.cryptoAmount,
                    cryptoCost = crypto.cryptoCost
                ))
            }
            override fun onView(crypto: CryptosModel) {
                viewModel.setViewCrypto(CryptoInWalletRequest(
                    userId = wallet!!.userId,
                    walletName = wallet!!.walletName,
                    cryptoName = crypto.cryptoName,
                    cryptoType = crypto.cryptoType,
                    image = crypto.image,
                    imageUrl = crypto.imageUrl,
                    cryptoDescription = crypto.cryptoDescription,
                    cryptoAmount = crypto.cryptoAmount,
                    cryptoCost = crypto.cryptoCost
                ))

                findNavController().navigate(
                    R.id.viewCryptoWalletFragment,
                    Bundle().apply {
                        userIdViewCrypto = wallet!!.userId
                        walletNameViewCrypto = wallet!!.walletName
                    }
                )
            }

            override fun getTheme(): Drawable? {
                if (viewModelCrypto.getTheme()){
                    return ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
                } else{
                    return ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
                }
            }

            override fun isMenuActive(): Boolean {
               return true
            }

        })
        binding.list.adapter = adapter

        adapter.submitList(wallet?.cryptocurrenciesList)

        viewModel.data.observe(viewLifecycleOwner) {

            viewModel.updateNewOrEdit()
            wallet = viewModel.getNewOrEdit()

            binding.inputWalletName.text = wallet?.walletName
            binding.inputWalletDescription.setText(wallet?.walletDescription)
            binding.cryptosAmountValue.text = wallet?.cryptosCount.toString()
            binding.cryptosCostValue.text = wallet?.cryptosCost.toString()
            adapter.submitList(wallet?.cryptocurrenciesList)
        }

        viewModel.updatedWallet.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.one_wallet_menu_edit)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.addCrypto -> {
                            viewModel.setNewOrEditCrypto(CryptoInWalletRequest(
                                userId = wallet!!.userId,
                                walletName = wallet!!.walletName
                            ))

                            findNavController().navigate(
                                R.id.addCryptoWalletFragment,
                                Bundle().apply {
                                    userId = wallet!!.userId
                                    walletName = wallet!!.walletName
                                }
                            )

                            true
                        }
                        R.id.update -> {
                            viewModel.setNewOrEdit(
                                WalletsModel(wallet!!.userId, binding.inputWalletName.text.toString(),
                                binding.inputWalletDescription.text.toString(), wallet!!.cryptosCount, wallet!!.cryptosCost,
                                wallet!!.cryptocurrenciesList
                            )
                            )
                            viewModel.updateWallet()
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
