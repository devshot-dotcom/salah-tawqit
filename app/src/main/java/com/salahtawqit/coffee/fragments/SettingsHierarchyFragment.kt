package com.salahtawqit.coffee.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.viewmodels.SharedViewModel

class SettingsHierarchyFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferenceManager.getDefaultSharedPreferences(view.context)
            .registerOnSharedPreferenceChangeListener(this)

        // Scrolling is disabled in favor of the parent fragment's ScrollView layout.
        listView.overScrollMode = View.OVER_SCROLL_NEVER
        listView.isVerticalScrollBarEnabled = false
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     *
     * This callback will run on your main thread.
     *
     * @param sharedPreferences The [SharedPreferences] that received the change.
     * @param key The key of the preference that was changed, added, or removed. Apps targeting
     * [android.os.Build.VERSION_CODES.R] on devices running OS versions
     * [Android R][android.os.Build.VERSION_CODES.R] or later, will receive
     * a `null` value when preferences are cleared.
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(activity != null)
            sharedViewModel.areSettingsUpdated = true
    }
}