package com.ar11.mobilecryptowallet.activity

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.FragmentCryptoDetailBinding
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.util.CryptosBooleanArg
import com.ar11.mobilecryptowallet.util.CryptosDoubleArg
import com.ar11.mobilecryptowallet.util.CryptosStringArg
import com.ar11.mobilecryptowallet.viewmodel.CryptoDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

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
        var Bundle.viewType: String? by CryptosStringArg
        var Bundle.isDarkTheme: Boolean? by CryptosBooleanArg
    }

    private val viewModel: CryptoDetailViewModel by activityViewModels()
    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCryptoDetailBinding.inflate(inflater, container, false)
        val viewType = arguments?.viewType ?: "edit"
        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.feedFragment
            )
        }

        val isDarkTheme = arguments?.isDarkTheme ?: false
        if (isDarkTheme){
            binding.deCrFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
        } else{
            binding.deCrFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }




        val crypto = Cryptos(
            cryptoName = arguments?.cryptoName ?: "",
            image = arguments?.image ?: "",
            imageUrl = arguments?.imageUrl ?: "",
            cryptoDescription = arguments?.cryptoDescription ?: "",
            cryptoAmount = arguments?.cryptoAmount ?: 0.0,
            cryptoCost = arguments?.cryptoCost ?: 0.0
        )

        binding.cryptoName.setText(crypto.cryptoName)
        binding.cryptoDescription.setText(crypto.cryptoDescription)
        binding.cryptoCost.setText(crypto.cryptoCost.toString())
        imageFile = null


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

        if (viewModel.authState.value?.isAdmin == true) {
            binding.updateButton.visibility = View.VISIBLE
        } else {
            binding.updateButton.visibility = View.INVISIBLE
        }

        if (viewType == "edit") {
            binding.updateButton.setText("Update")
        } else {
            binding.updateButton.setText("Save")
        }


        viewModel.authState.observe(viewLifecycleOwner) { auth ->
            if (auth.isAdmin == true) {
                binding.updateButton.visibility = View.VISIBLE
            } else {
                binding.updateButton.visibility = View.INVISIBLE
            }
        }

        if (viewModel.authState.value?.isAdmin == true) {
            binding.deleteButton.visibility = View.VISIBLE
        } else {
            binding.deleteButton.visibility = View.INVISIBLE
        }


        val getImageFromGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding.image.setImageURI(uri)

                    // создание файла в памяти приложения, называемой cache (кэш)
                    // файла с именем temp_image.jpg
                    val file = File(context?.cacheDir, "temp_image.jpg")
                    // следующие два оператора загружают выбранный нами файл в кэш
                    // в файл temp_image.jpg
                    val inputStream = context?.contentResolver?.openInputStream(uri)
                    inputStream?.copyTo(file.outputStream())
                    // Сохраняем в переменную imageFile
                    // она теперь не null и при нажатии на кнопку Upload Button
                    // мы будем понимать что мы должны изменить картинку для пользователя
                    imageFile = file
                }
            }


        binding.image.setOnClickListener {
            getImageFromGallery.launch("image/*")
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteCrypto(crypto.cryptoName)
            findNavController().navigateUp()
        }


        binding.updateButton.setOnClickListener {
            val sendCrypto = Cryptos(
                cryptoName = binding.cryptoName.text.toString(),
                image = "",
                imageUrl = "",
                cryptoDescription = binding.cryptoDescription.text.toString(),
                cryptoAmount = crypto.cryptoAmount,
                cryptoCost = binding.cryptoCost.text.toString().toDouble()
            )
            if (viewType == "edit") {
                viewModel.updateCryptoInfo(sendCrypto, imageFile)
            } else {
                viewModel.saveCryptoInfo(sendCrypto, imageFile)
            }

        }



        return binding.root

    }
}