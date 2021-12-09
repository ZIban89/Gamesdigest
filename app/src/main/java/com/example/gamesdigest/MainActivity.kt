package com.example.gamesdigest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.example.gamesdigest.common.isValidEmail
import com.example.gamesdigest.common.showToast
import com.example.gamesdigest.presentation.subscriptions.SubscriptionsFragment.Companion.SUBSCRIPTIONS_EMAIL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTheme()

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.container_nav_host) as NavHostFragment
        navController = host.navController
            .also { setupActionBarWithNavController(it) }
    }

    private fun setTheme() {
        val isDarkTheme = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.theme_switcher_key), false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController?.popBackStack()
            }
            R.id.settings -> {
                navController?.navigate(R.id.action_open_preference_fragment)
            }
            R.id.subscriptions_by_email -> {
                val email = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString(getString(R.string.email_pref_key), "")
                if (email?.isValidEmail() == true) {
                    navController?.navigate(
                        R.id.action_open_subscriptions_fragment,
                        Bundle().apply {
                            putString(SUBSCRIPTIONS_EMAIL, email)
                        })
                } else {
                    showToast(
                        this,
                        getString(R.string.check_email_message),
                        true
                    )
                }
            }
        }
        return true
    }
}
