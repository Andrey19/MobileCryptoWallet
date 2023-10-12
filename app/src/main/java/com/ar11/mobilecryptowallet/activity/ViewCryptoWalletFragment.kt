package com.ar11.mobilecryptowallet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.auth.AppAuth
import com.ar11.mobilecryptowallet.databinding.FragmentWalletCryptoViewBinding
import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.util.AndroidUtils
import com.ar11.mobilecryptowallet.util.StringArg
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.ar11.mobilecryptowallet.viewmodel.WalletsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ViewCryptoWalletFragment : Fragment() {

    var list: List<String> = emptyList()
    var selectedCrypto: Cryptos = Cryptos()
    var cryptoIndex: Int = -1
    var crypto = CryptoInWalletRequest("","")
    companion object {
        var Bundle.userIdViewCrypto: String? by StringArg
        var Bundle.walletNameViewCrypto: String? by StringArg
    }

    @Inject
    lateinit var auth: AppAuth

    private val viewModel: WalletsViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    private var fragmentBinding: FragmentWalletCryptoViewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletCryptoViewBinding.inflate(inflater, container, false)
        fragmentBinding = binding

        val context = this.context;

        crypto = viewModel.getViewCrypto()!!
        list = viewModelCrypto.getCryptoList()
        if (crypto.cryptoType.isNotEmpty()) {
            selectedCrypto = viewModelCrypto.getCrypto(crypto.cryptoType)
            cryptoIndex = viewModelCrypto.getCryptoIndex(selectedCrypto)
        } else {
            selectedCrypto = viewModelCrypto.getCrypto(list[0])
            cryptoIndex = 0
        }



        with(binding) {
            inputCryptoName.text = crypto.cryptoName
            inputCryptoType.text = crypto.cryptoType
            inputCryptoDescription.text = crypto.cryptoDescription
            inputCryptoAmount.text = crypto.cryptoAmount.toString()
            cryptoCostValue.text =
                (crypto.cryptoAmount * selectedCrypto.cryptoCost).toString()


            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(com.ar11.mobilecryptowallet.R.menu.one_wallet_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.save -> {
                                viewModel.setNewOrEditCrypto(
                                    CryptoInWalletRequest(
                                        crypto.userId, crypto.walletName,
                                        inputCryptoName.text.toString(), selectedCrypto.cryptoName,
                                        selectedCrypto.image,selectedCrypto.imageUrl,
                                        inputCryptoDescription.text.toString(),
                                        inputCryptoAmount.text.toString().toDouble(),
                                        cryptoCostValue.text.toString().toDouble()
                                    )
                                )
                                viewModel.updateCryptoInWallet()
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
        }


        return binding.root
    }
}
