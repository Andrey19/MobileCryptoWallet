package com.ar11.mobilecryptowallet.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class CryptoListAdapter (context: Context, resource: Int, items: List<String>) :
    ArrayAdapter<String>(context, resource, items) {

    private var originalItems: ArrayList<String> = ArrayList(items) // Храним оригинальный список
    private var filteredItems: ArrayList<String> = ArrayList(items)

    override fun getCount(): Int {
        return filteredItems.size
    }

    override fun getItem(position: Int): String? {
        return filteredItems[position]
    }



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (!constraint.isNullOrEmpty()) {
                    val suggestions = ArrayList<String>()
                    for (item in originalItems) {
                        if (item.contains(constraint, ignoreCase = true)) {
                            suggestions.add(item)
                        }
                    }
                    results.values = suggestions
                    results.count = suggestions.size
                } else {
                    synchronized(this) {
                        results.values = originalItems
                        results.count = originalItems.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = results?.values as ArrayList<String>
                notifyDataSetChanged() // Уведомляем адаптер об изменении данных
            }
        }
    }
}
