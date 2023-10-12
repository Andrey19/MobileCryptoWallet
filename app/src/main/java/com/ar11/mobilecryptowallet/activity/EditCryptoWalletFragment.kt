package com.ar11.mobilecryptowallet.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.auth.AppAuth
import com.ar11.mobilecryptowallet.databinding.FragmentWalletCryptoEditBinding
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
class EditCryptoWalletFragment : Fragment() {

    var list: List<String> = emptyList()
    var selectedCrypto: Cryptos = Cryptos()
    var cryptoIndex: Int = -1
    var crypto = CryptoInWalletRequest("","")
    companion object {
        var Bundle.userIdEditCrypto: String? by StringArg
        var Bundle.walletNameEditCrypto: String? by StringArg
    }

    @Inject
    lateinit var auth: AppAuth

    private val viewModel: WalletsViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    private var fragmentBinding: FragmentWalletCryptoEditBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletCryptoEditBinding.inflate(inflater, container, false)
        fragmentBinding = binding

        val context = this.context;

        crypto = viewModel.getNewOrEditCrypto()!!
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
            inputCryptoDescription.setText(crypto.cryptoDescription)
            inputCryptoAmount.setText(crypto.cryptoAmount.toString())
            cryptoCostValue.text =
                (crypto.cryptoAmount * selectedCrypto.cryptoCost).toString()

            inputCryptoAmount.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {

                }

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    try {
                        val cost = inputCryptoAmount?.text.toString().toDouble()
                        cryptoCostValue.text = (cost * selectedCrypto.cryptoCost).toString()
                    } catch (_: Exception) {

                    }
                }
            })

            viewModel.updatedCryptoInWallet.observe(viewLifecycleOwner) {
                findNavController().navigateUp()
            }

            viewModelCrypto.data.observe(viewLifecycleOwner) {

            }

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
