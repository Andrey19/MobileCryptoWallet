package com.ar11.mobilecryptowallet.activity

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.databinding.FragmentUserInfoBinding
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.ar11.mobilecryptowallet.viewmodel.UserInfoViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class UserInfoFragment : Fragment() {

    @Inject
    lateinit var auth2: AppAuth2

    private var imageFile: File? = null
    private val viewModel: UserInfoViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        val user = viewModel.userData

        imageFile = null

        viewModel.updateUserData()

        binding.userName.setText(user.value?.name)
        binding.userEmail.text = user.value?.email
        if (!user.value?.avatarUrl.isNullOrEmpty()) {
            val options = RequestOptions()
            options.circleCrop()


            Glide.with(binding.avatar)
                .load(user.value?.avatarUrl)
                .apply(options)
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .into(binding.avatar)
        } else {
            binding.avatar.setImageResource(R.mipmap.ic_launcher_wallet)
        }

       val theme = viewModelCrypto.getTheme()
        binding.mySwitch.isChecked = theme
        if (theme){
            binding.usFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
        } else {
            binding.usFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }


        binding.mySwitch.setOnCheckedChangeListener {  buttonView, isChecked ->
            viewModelCrypto.setTheme(isChecked)
            if (isChecked) {
                binding.usFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
            } else {
                binding.usFr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
            } }


        val getImageFromGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding.avatar.setImageURI(uri)

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

        binding.avatar.setOnClickListener {
            getImageFromGallery.launch("image/*")
        }


        binding.updateButton.setOnClickListener {
            viewModel.updateUserInfo(binding.userName.text.toString(), imageFile)
        }

        binding.logOutButton.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do you want to logout ?")
            builder.setTitle("Logout")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { _, _ ->
                run {
                    auth2.removeAuth2()
                    findNavController().navigate(R.id.login2Fragment)
                }
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.show()

        }



        viewModel.userData.observe(viewLifecycleOwner) { data ->
            binding.userName.setText(data.name)
            binding.userEmail.text = data.email
            if (!data.avatarUrl.isNullOrEmpty()) {
                val options = RequestOptions()
                options.circleCrop()


                Glide.with(binding.avatar)
                    .load(data.avatarUrl)
                    .apply(options)
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(binding.avatar)
            } else {
                binding.avatar.setImageResource(R.mipmap.ic_launcher_wallet)
            }
        }
        return binding.root

    }
}