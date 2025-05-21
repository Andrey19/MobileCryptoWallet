package com.ar11.mobilecryptowallet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.FragmentCryptoDetailBinding
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.util.CryptosDoubleArg
import com.ar11.mobilecryptowallet.util.CryptosStringArg
import com.ar11.mobilecryptowallet.viewmodel.CryptoDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class CryptoDetailFragment : Fragment() {

    companion object {
        var Bundle.cryptoName: String? by CryptosStringArg
        var Bundle.image: String? by CryptosStringArg
        var Bundle.imageUrl: String? by CryptosStringArg
        var Bundle.cryptoDescription: String? by CryptosStringArg
        var Bundle.cryptoAmount: Double? by CryptosDoubleArg
        var Bundle.cryptoCost: Double? by CryptosDoubleArg
    }

    private val viewModel: CryptoDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCryptoDetailBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.feedFragment
            )
        }


        val crypto = Cryptos(
            cryptoName = arguments?.cryptoName ?: "",
            image = arguments?.image ?: "",
            imageUrl = arguments?.imageUrl ?: "",
            cryptoDescription = arguments?.cryptoDescription ?: "",
            cryptoAmount = arguments?.cryptoAmount ?: 0.0,
            cryptoCost = arguments?.cryptoCost ?: 0.0
        )

        binding.cryptoName.text = crypto.cryptoName
        binding.cryptoDescription.text = crypto.cryptoDescription

        if (crypto.imageUrl.isNotEmpty()) {
            val options = RequestOptions()
            options.circleCrop()


            Glide.with(binding.image)
                .load(crypto.imageUrl)
                .apply(options)
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .into(binding.image)
        } else {
            binding.image.setImageResource(R.mipmap.ic_launcher_wallet)
        }


        binding.saveButton.setOnClickListener {
            viewModel.saveCryptoInfo(crypto)
        }


        return binding.root

    }
}