package com.ar11.mobilecryptowallet.activity


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.viewmodel.Auth2ViewModel
import com.ar11.mobilecryptowallet.viewmodel.CryptoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import androidx.core.content.edit


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AppActivity : AppCompatActivity(R.layout.activity_app) {

    @Inject
    lateinit var auth2: AppAuth2

    private val viewModelAuth2: Auth2ViewModel by viewModels()

    private val viewModelCrypto: CryptoViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        viewModelAuth2.data2.observe(this) {
            invalidateOptionsMenu()
        }


        val context = this

        val sharedPreferences = context.getSharedPreferences("cryptoPref", Context.MODE_PRIVATE)
        val isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false)
        viewModelCrypto.setTheme(isDarkTheme)

        viewModelCrypto.isDarkTheme.observe(this) { isDarkTheme ->
            val sharedPreferences = context.getSharedPreferences("cryptoPref", Context.MODE_PRIVATE)
            sharedPreferences.edit(commit = true) { putBoolean("isDarkTheme", isDarkTheme) }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_market -> {
                    title = "MobileCryptoWallet (Project Info)"
                    findNavController(R.id.nav_host_fragment)
                        .navigate(
                            R.id.feedFragment
                        )
                    true
                }

                R.id.action_wallet -> {
                    if (auth2.authStateFlow2.value.email != null) {

                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.walletsFragment
                            )
                        true
                    } else {
                        bottomNav.selectedItemId = R.id.action_profile
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.login2Fragment
                            )
                        false
                    }
                }

                R.id.action_search -> {
                    title = "MobileCryptoWallet (Project Info)"
                    findNavController(R.id.nav_host_fragment)
                        .navigate(
                            R.id.listUserInfoFragment
                        )
                    true
                }

                R.id.action_profile -> {
                    if (auth2.authStateFlow2.value.email != null) {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.userInfoFragment
                            )
                    } else {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.login2Fragment
                            )
                    }
                    true
                }

                R.id.action_about -> {
                    title = "MobileCryptoWallet (Crypto Info)"
                    findNavController(R.id.nav_host_fragment)
                        .navigate(
                            R.id.projectFragment
                        )
                    true
                }
                else -> false
            }
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(
                menu: Menu, menuInflater:
                MenuInflater
            ) {
                menuInflater.inflate(R.menu.menu_main, menu)

                menu.let {
                    it.setGroupVisible(R.id.unauthenticated, !viewModelAuth2.authenticated)
                    it.setGroupVisible(R.id.authenticated, viewModelAuth2.authenticated)
                }


            }

            override fun onMenuItemSelected(menuItem: MenuItem):
                    Boolean =
                when (menuItem.itemId) {
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

                    R.id.user_info -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.userInfoFragment
                            )
                        true
                    }

                    R.id.about -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.projectFragment
                            )
                        true
                    }

                    R.id.login2 -> {
                        findNavController(R.id.nav_host_fragment)
                            .navigate(
                                R.id.login2Fragment
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

                    R.id.updatePrice -> {
                        viewModelCrypto.updatePrice()
                        true
                    }

                    R.id.signout -> {

                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Do you want to logout ?")
                        builder.setTitle("Logout")
                        builder.setCancelable(false)
                        builder.setPositiveButton("Yes") { _, _ ->
                            run {
                                auth2.removeAuth2()
                                findNavController(R.id.nav_host_fragment)
                                    .navigate(
                                        R.id.feedFragment
                                    )
                            }
                        }
                        builder.setNegativeButton("No") { dialog, _ ->
                            dialog.cancel()
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