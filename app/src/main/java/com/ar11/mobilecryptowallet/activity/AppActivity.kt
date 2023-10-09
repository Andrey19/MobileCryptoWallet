package com.ar11.mobilecryptowallet.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.auth.AppAuth
import com.ar11.mobilecryptowallet.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AppActivity : AppCompatActivity(R.layout.activity_app) {

    @Inject
    lateinit var auth: AppAuth

    private val viewModelAuth: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelAuth.data.observe(this) {
            invalidateOptionsMenu()
        }

        val context = this
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater:
            MenuInflater
            ) {
                menuInflater.inflate(R.menu.menu_main, menu)

                menu.let {
                    it.setGroupVisible(R.id.unauthenticated, !viewModelAuth.authenticated)
                    it.setGroupVisible(R.id.authenticated, viewModelAuth.authenticated)
                }

            }

            override fun onMenuItemSelected(menuItem: MenuItem):
                    Boolean =
                when (menuItem.itemId) {
                    R.id.login -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.loginFragment
                            )
                        true
                    }
                    R.id.signin -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.registerFragment
                            )
                        true
                    }
                    R.id.home -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.feedFragment
                            )
                        true
                    }
                    R.id.wallets -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.walletsFragment
                            )
                        true
                    }
                    R.id.signout -> {

                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Do you want to logout ?")
                        builder.setTitle("Logout")
                        builder.setCancelable(false)
                        builder.setPositiveButton("Yes") {
                                _, _ ->
                            run {
                                auth.removeAuth()
                                findNavController(R.id.nav_host_fragment)
                                    .navigate(
                                        R.id.feedFragment
                                    )
                            }
                        }
                        builder.setNegativeButton("No") {
                                dialog, _ -> dialog.cancel()
                        }
                        val alertDialog = builder.create()
                        alertDialog.show()

                        true
                    }
                    else -> false
                }

        })
    }


}
