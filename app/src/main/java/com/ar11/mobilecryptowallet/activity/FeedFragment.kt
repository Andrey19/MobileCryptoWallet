package com.ar11.mobilecryptowallet.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.adapter.CryptosAdapter
import com.ar11.mobilecryptowallet.adapter.OnInteractionListener
import com.ar11.mobilecryptowallet.databinding.FeedFragmentBinding
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FeedFragment: Fragment() {

    private val viewModel: CryptoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FeedFragmentBinding.inflate(inflater, container, false)

        val adapter = CryptosAdapter(object : OnInteractionListener {})
        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyText.isVisible = it.isEmpty()
        }


        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshCryptos()
        }

        viewModel.dataState.observe(viewLifecycleOwner){
            binding.progress.isVisible = it.loading || it.refreshing
        }

        return binding.root
    }
}
