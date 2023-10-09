package com.ar11.mobilecryptowallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.CardCryptoBinding
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

interface OnInteractionListener {

}

class CryptosAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Cryptos, CryptoViewHolder>(CryptoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = CardCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CryptoViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class CryptoViewHolder(
    private val binding: CardCryptoBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(crypto: Cryptos) {
        binding.apply {

            cryptoName.text = crypto.cryptoName
            cryptoDescription.text = crypto.cryptoDescription
            cryptoCost.text = crypto.cryptoCost.toString()


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
            }
        }
    }
}

class CryptoDiffCallback : DiffUtil.ItemCallback<Cryptos>() {
    override fun areItemsTheSame(oldItem: Cryptos, newItem: Cryptos): Boolean {
        return oldItem.cryptoName == newItem.cryptoName
    }

    override fun areContentsTheSame(oldItem: Cryptos, newItem: Cryptos): Boolean {
        return oldItem == newItem
    }
}
