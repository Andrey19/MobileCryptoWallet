package com.ar11.mobilecryptowallet.activity

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.avatar
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.avatarUrl
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.email
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.isAdmin
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.name
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.password
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.role
import com.ar11.mobilecryptowallet.databinding.FragmentCryptoDetailBinding
import com.ar11.mobilecryptowallet.databinding.FragmentUserDetailBinding
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.model.UserModel2
import com.ar11.mobilecryptowallet.util.CryptosBooleanArg
import com.ar11.mobilecryptowallet.util.CryptosDoubleArg
import com.ar11.mobilecryptowallet.util.CryptosStringArg
import com.ar11.mobilecryptowallet.viewmodel.CryptoDetailViewModel
import com.ar11.mobilecryptowallet.viewmodel.UserDetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class UserDetailFragment : Fragment() {

    companion object {
        var Bundle.email: String? by CryptosStringArg
        var Bundle.name: String? by CryptosStringArg
        var Bundle.avatar: String? by CryptosStringArg
        var Bundle.avatarUrl: String? by CryptosStringArg
        var Bundle.password: String? by CryptosStringArg
        var Bundle.isAdmin: Boolean? by CryptosBooleanArg
        var Bundle.role: String? by CryptosStringArg
    }

    private val viewModel: UserDetailViewModel by activityViewModels()
    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserDetailBinding.inflate(inflater, container, false)
//        val viewType = arguments?.viewType ?: "edit"
        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.listUserInfoFragment
            )
        }


        val user = UserModel2(
            email = arguments?.email ?: "",
            name = arguments?.name ?: "",
            avatar = arguments?.avatar ?: "",
            avatarUrl = arguments?.avatarUrl ?: "",
            password = arguments?.password ?: "",
            isAdmin = arguments?.isAdmin ?: false,
            role = arguments?.role ?: ""
        )

        binding.userEmail.text = user.email.toString()
        binding.userName.setText(user.name)

        imageFile = null


        if (user.avatarUrl.isNotEmpty()) {
            val options = RequestOptions()
            options.circleCrop()


            Glide.with(binding.image)
                .load(user.avatarUrl)
                .apply(options)
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .into(binding.image)
        } else {
            binding.image.setImageResource(R.mipmap.ic_launcher_wallet)
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

        binding.updateButton.setOnClickListener {
            val sendUser = UserModel2(
                email = user.email,
                name = binding.userName.text.toString(),
            )
            viewModel.updateUserName(sendUser, imageFile)
        }

        binding.image.setOnClickListener {
            getImageFromGallery.launch("image/*")
        }

        return binding.root

    }
}