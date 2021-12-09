package com.example.gamesdigest.presentation.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.gamesdigest.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<SwitchPreferenceCompat>(
            getString(R.string.theme_switcher_key)
        )?.setOnPreferenceChangeListener { preference, newValue ->
            AppCompatDelegate.setDefaultNightMode(
                if (newValue == true) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            true
        }
    }
}
