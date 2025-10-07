package com.ar11.mobilecryptowallet.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.viewmodel.ProjectViewModel
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.FragmentProjectBinding
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProjectFragment : Fragment() {

    private val viewModel: ProjectViewModel by activityViewModels()

    private val viewModelCrypto: CryptoViewModel by activityViewModels()
    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProjectBinding.inflate(inflater, container, false)
        imageFile = null

        binding.updateAboutButton.setOnClickListener {
            viewModel.updateAboutInfo(
                binding.projectName.text.toString(),
                binding.projectDescription.text.toString(),
                imageFile,
            )
            findNavController().navigate(R.id.feedFragment)
        }



        if (!viewModel.projectDataFromDb.value.isNullOrEmpty()) {
            val project = viewModel.projectDataFromDb.value?.get(0)

            viewModel.updateProjectFromDb()

            binding.projectName.setText(project?.projectName)
            binding.projectDescription.setText(project?.projectDescription)

            if (!project?.imageUrl.isNullOrEmpty()) {
                val options = RequestOptions()
                options.circleCrop()


                Glide.with(binding.imageView)
                    .load(project?.imageUrl)
                    .apply(options)
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(binding.imageView)
            }
        }

        val getImageFromGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding.imageView.setImageURI(uri)

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

        val theme = viewModelCrypto.getTheme()
        if (theme){
            binding.frPr.background = ContextCompat.getDrawable(requireContext(), R.drawable.black_theme)
        } else {
            binding.frPr.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_info_white)
        }


        binding.imageView.setOnClickListener {
            getImageFromGallery.launch("image/*")
        }

        viewModel.projectDataFromDb.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {

                binding.projectName.setText(data[0].projectName)
                binding.projectDescription.setText(data[0].projectDescription)

                if (data[0].imageUrl.isNotEmpty()) {
                    val options = RequestOptions()
                    options.circleCrop()


                    Glide.with(binding.imageView)
                        .load(data[0].imageUrl)
                        .apply(options)
                        .placeholder(R.drawable.ic_loading_100dp)
                        .error(R.drawable.ic_error_100dp)
                        .timeout(10_000)
                        .into(binding.imageView)
                }
            }
        }


        return binding.root
    }
}