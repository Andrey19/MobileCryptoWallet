package com.ar11.mobilecryptowallet.activity

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoAmount
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoCost
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoDescription
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.cryptoName
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.image
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.imageUrl
import com.ar11.mobilecryptowallet.activity.CryptoDetailFragment.Companion.viewType
import com.ar11.mobilecryptowallet.adapter.CryptosAdapter
import com.ar11.mobilecryptowallet.adapter.OnInteractionListener
import com.ar11.mobilecryptowallet.databinding.FeedFragmentBinding
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.viewmodel.Auth2ViewModel
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FeedFragment: Fragment() {

    private val viewModel: CryptoViewModel by activityViewModels()
    private val authViewModel: Auth2ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FeedFragmentBinding.inflate(inflater, container, false)

        val adapter = CryptosAdapter(object : OnInteractionListener {
            override fun onView(crypto: Cryptos) {
                findNavController().navigate(
                    R.id.cryptoDetailFragment,
                    Bundle().apply {
                        cryptoName = crypto.cryptoName
                        image = crypto.image
                        imageUrl = crypto.imageUrl
                        cryptoDescription = crypto.cryptoDescription
                        cryptoAmount = crypto.cryptoAmount
                        cryptoCost = crypto.cryptoCost
                        viewType = "edit"
                    }
                )
            }


        })

        if (authViewModel.data2.value?.isAdmin == true) {
            binding.fab.visibility = View.VISIBLE
            binding.updatePriceNew.visibility = View.VISIBLE
        } else {
            binding.fab.visibility = View.INVISIBLE
            binding.updatePriceNew.visibility = View.INVISIBLE
        }

        authViewModel.data2.observe(viewLifecycleOwner)  { data ->
            if (authViewModel.data2.value?.isAdmin == true) {
                binding.fab.visibility = View.VISIBLE
            } else {
                binding.fab.visibility = View.INVISIBLE
            }
        }


        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyText.isVisible = it.isEmpty()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.cryptoDetailFragment,
                Bundle().apply {
                    cryptoName = "Crypto Name"
                    image = "Crypto Description"
                    imageUrl = ""
                    cryptoDescription = ""
                    cryptoAmount = 1.0
                    cryptoCost = 0.0
                    viewType = "create"
                }
            )
        }


        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshCryptos()
        }

        binding.updatePriceNew.setOnClickListener {
            viewModel.updatePrice()
        }

        viewModel.dataState.observe(viewLifecycleOwner){
            if (it.loading){
                binding.swiperefresh.isRefreshing = true
            } else if(!it.refreshing){
                binding.swiperefresh.isRefreshing = false
            }
        }

        return binding.root
    }
}
