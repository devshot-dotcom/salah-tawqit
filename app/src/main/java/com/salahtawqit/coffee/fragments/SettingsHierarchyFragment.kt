package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.salahtawqit.coffee.R

class SettingsHierarchyFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Scrolling is disabled in favor of the parent fragment's ScrollView layout.
        listView.overScrollMode = View.OVER_SCROLL_NEVER
        listView.isVerticalScrollBarEnabled = false
    }
}