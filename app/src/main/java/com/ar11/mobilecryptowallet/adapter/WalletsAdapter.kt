package com.ar11.mobilecryptowallet.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.CardWalletBinding
import com.ar11.mobilecryptowallet.dto.WalletsModel


interface WalletOnInteractionListener {
    fun onEdit(wallet: WalletsModel) {}
    fun onRemove(wallet: WalletsModel) {}
    fun onView(wallet: WalletsModel) {}
}

class WalletsAdapter(
    private val walletOnInteractionListener: WalletOnInteractionListener,
) : ListAdapter<WalletsModel, WalletViewHolder>(WalletsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val binding = CardWalletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding, walletOnInteractionListener)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val wallet = getItem(position)
        holder.bind(wallet)
    }
}

class WalletViewHolder(
    private val binding: CardWalletBinding,
    private val walletOnInteractionListener: WalletOnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wallet: WalletsModel) {
        binding.apply {
            walletName.text = wallet.walletName
            walletDescription.text = wallet.walletDescription
            numCryptos.text = wallet.cryptosCount.toString()
            costs.text = wallet.cryptosCost.toString()

            //menu.visibility = View.INVISIBLE

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.wallets_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                val builder = AlertDialog.Builder(it.context)
                                builder.setMessage("Do you really want to delete wallet ?")
                                builder.setTitle("Delete Wallet")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Yes") {
                                        _, _ -> walletOnInteractionListener.onRemove(wallet)
                                }
                                builder.setNegativeButton("No") {
                                        dialog, _ -> dialog.cancel()
                                }
                                val alertDialog = builder.create()
                                alertDialog.show()


                                true
                            }
                            R.id.edit -> {
                                walletOnInteractionListener.onEdit(wallet)
                                true
                            }
                            R.id.view -> {
                                walletOnInteractionListener.onView(wallet)
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

class WalletsDiffCallback : DiffUtil.ItemCallback<WalletsModel>() {
    override fun areItemsTheSame(oldItem: WalletsModel, newItem: WalletsModel): Boolean {
        return oldItem.userId == newItem.userId && oldItem.walletName == newItem.walletName
    }

    override fun areContentsTheSame(oldItem: WalletsModel, newItem: WalletsModel): Boolean {
        return oldItem == newItem
    }
}
