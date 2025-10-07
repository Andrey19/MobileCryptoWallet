package com.ar11.mobilecryptowallet.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.CardCryptoInWalletBinding
import com.ar11.mobilecryptowallet.dto.CryptosModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions



interface OnCryptoInteractionListener {
    fun onEdit(crypto: CryptosModel) {}
    fun onRemove(crypto: CryptosModel) {}
    fun onView(crypto: CryptosModel) {}

    fun isMenuActive(): Boolean
}

class CryptoInWalletAdapter(
    private val onCryptoInteractionListener: OnCryptoInteractionListener,
) : ListAdapter<CryptosModel, CryptoInWalletViewHolder>(CryptoInWalletDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoInWalletViewHolder {
        val binding = CardCryptoInWalletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CryptoInWalletViewHolder(binding, onCryptoInteractionListener)
    }

    override fun onBindViewHolder(holder: CryptoInWalletViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class CryptoInWalletViewHolder(
    private val binding: CardCryptoInWalletBinding,
    private val onCryptoInteractionListener: OnCryptoInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(crypto: CryptosModel) {
        binding.apply {
            cryptoName.text = crypto.cryptoName
            cryptoType.text = crypto.cryptoType
            cryptoDescription.text = crypto.cryptoDescription
            count.text = crypto.cryptoAmount.toString()
            cost.text = crypto.cryptoCost.toString()


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

            if(onCryptoInteractionListener.isMenuActive()) {
                menu.visibility = View.VISIBLE
            } else {
                menu.visibility = View.INVISIBLE
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.crypto_in_wallet_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                val builder = AlertDialog.Builder(it.context)
                                builder.setMessage("Do you really want to delete crypto ?")
                                builder.setTitle("Delete Crypto")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Yes") {
                                        _, _ -> onCryptoInteractionListener.onRemove(crypto)
                                }
                                builder.setNegativeButton("No") {
                                        dialog, _ -> dialog.cancel()
                                }
                                val alertDialog = builder.create()
                                alertDialog.show()

                                true
                            }
                            R.id.edit -> {
                                onCryptoInteractionListener.onEdit(crypto)
                                true
                            }
                            R.id.view -> {
                                onCryptoInteractionListener.onView(crypto)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

        }
    }
}

class CryptoInWalletDiffCallback : DiffUtil.ItemCallback<CryptosModel>() {
    override fun areItemsTheSame(oldItem: CryptosModel, newItem: CryptosModel): Boolean {
        return oldItem.cryptoName == newItem.cryptoName
    }

    override fun areContentsTheSame(oldItem: CryptosModel, newItem: CryptosModel): Boolean {
        return oldItem == newItem
    }
}
