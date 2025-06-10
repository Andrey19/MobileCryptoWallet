package com.ar11.mobilecryptowallet.activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.ar11.mobilecryptowallet.R

class MenuFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.btn_home).setOnClickListener {
            // Обработка клика по пиктограмме "Home"
            findNavController().navigate(R.id.projectFragment)
        }

        view.findViewById<ImageView>(R.id.btn_search).setOnClickListener {
            // Обработка клика по пиктограмме "Search"
        }
    }
}