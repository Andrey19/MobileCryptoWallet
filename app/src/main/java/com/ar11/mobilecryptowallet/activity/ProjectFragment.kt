package com.ar11.mobilecryptowallet.activity

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ar11.mobilecryptowallet.viewmodel.ProjectViewModel
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.FragmentProjectBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProjectFragment : Fragment() {

    private val viewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProjectBinding.inflate(inflater, container, false)

        if (!viewModel.isFromDb) {
            val project = viewModel.projectData

            viewModel.updateProject()

            binding.projectName.text = project.value?.projectName
            binding.projectDescription.text = project.value?.projectDescription

            if (!project.value?.imageUrl.isNullOrEmpty()) {
                val options = RequestOptions()
                options.circleCrop()


                Glide.with(binding.imageView)
                    .load(project.value?.imageUrl)
                    .apply(options)
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(binding.imageView)
            }

            binding.imageView.setOnClickListener {
                binding.imageView.setImageResource(R.drawable.ic_loading_100dp)
                viewModel.updateProject()
            }

            viewModel.projectData.observe(viewLifecycleOwner) { data ->
                binding.projectName.text = data.projectName
                binding.projectDescription.text = data.projectDescription
                if (data.imageUrl.isNotEmpty()) {
                    val options = RequestOptions()
                    options.circleCrop()


                    Glide.with(binding.imageView)
                        .load(data.imageUrl)
                        .apply(options)
                        .placeholder(R.drawable.ic_loading_100dp)
                        .error(R.drawable.ic_error_100dp)
                        .timeout(10_000)
                        .into(binding.imageView)
                }
            }
        }
        else {
            if (!viewModel.projectDataFromDb.value.isNullOrEmpty()) {
                val project = viewModel.projectDataFromDb.value?.get(0)

                viewModel.updateProjectFromDb()

                binding.projectName.text = project?.projectName
                binding.projectDescription.text = project?.projectDescription

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

            binding.imageView.setOnClickListener {
                binding.imageView.setImageResource(R.drawable.ic_loading_100dp)
                viewModel.updateProjectFromDb()
            }

            viewModel.projectDataFromDb.observe(viewLifecycleOwner) { data ->
                if (data.isNotEmpty()) {
                    binding.projectName.text = data[0].projectName
                    binding.projectDescription.text = data[0].projectDescription
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
        }

        return binding.root
    }
}