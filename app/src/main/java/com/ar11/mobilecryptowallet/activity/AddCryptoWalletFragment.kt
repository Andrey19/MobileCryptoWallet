package com.ar11.mobilecryptowallet.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.adapter.CryptoListAdapter
import com.ar11.mobilecryptowallet.databinding.FragmentWalletCryptoAddBinding
import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.util.AndroidUtils
import com.ar11.mobilecryptowallet.util.StringArg
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.ar11.mobilecryptowallet.viewmodel.WalletsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AddCryptoWalletFragment : Fragment(), AdapterView.OnItemSelectedListener {
    var list: List<String> = emptyList()
    var selectedCrypto: Cryptos = Cryptos()
    var cryptoIndex: Int = -1
    var crypto = CryptoInWalletRequest("","")
    companion object {
        var Bundle.userId: String? by StringArg
        var Bundle.walletName: String? by StringArg
    }

    private val viewModel: WalletsViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    private var fragmentBinding: FragmentWalletCryptoAddBinding? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWalletCryptoAddBinding.inflate(inflater, container, false)
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

        val theme = viewModelCrypto.getTheme()
        if (theme){
            binding.crWalFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
        } else {
            binding.crWalFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }

//        var spinnerAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, list)
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        with(binding.inputCryptoType)
//        {
//            adapter = spinnerAdapter
//            setSelection(cryptoIndex, false)
//            onItemSelectedListener = this@AddCryptoWalletFragment
//        }
//        val adapter =
//            CryptoListAdapter(this.requireContext(), android.R.layout.simple_list_item_1, list)

//        val listener = View.OnTouchListener{v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                binding.inputCryptoType.showDropDown()
//            }
//
//            false
//        }

        binding.inputCryptoType.setOnTouchListener({v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                binding.inputCryptoType.showDropDown()
            }

            false
        })

        binding.inputCryptoType.setAdapter(CryptoListAdapter(this.requireContext(), android.R.layout.simple_list_item_1, list))


//
//        ArrayAdapter<String>(this.requireContext(), android.R.layout.simple_list_item_1, list).also { adapter ->
//            binding.inputCryptoType.setAdapter(adapter)
//        }

        with(binding) {
            inputCryptoName.setText(crypto.cryptoName)
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

            viewModel.addedCryptoToWallet.observe(viewLifecycleOwner) {
                findNavController().navigateUp()
            }

            viewModelCrypto.data.observe(viewLifecycleOwner) {

            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.one_wallet_menu)
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
                                viewModel.saveCryptoToWallet()
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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        selectedCrypto = viewModelCrypto.getCrypto(list[position])
        cryptoIndex = position
        try {
            val cost = fragmentBinding?.inputCryptoAmount?.text.toString().toDouble()
            fragmentBinding!!.cryptoCostValue.text = (cost * selectedCrypto.cryptoCost).toString()
        } catch (_: Exception) {

        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {

    }
}
