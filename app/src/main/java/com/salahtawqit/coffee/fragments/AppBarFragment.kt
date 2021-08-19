package com.salahtawqit.coffee.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.salahtawqit.coffee.R

/**
 * The application's app bar.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class AppBarFragment : Fragment() {
    private lateinit var fragmentContext: Context
    private lateinit var menuButton: ImageButton

    /**
     * Change the image button's display state (icon and contentDescription) to display the home state.
     */
    private fun changeToSettingsButton(it: ImageButton) {
        // Set tag.
        it.tag = "settings"

        // Set icon.
        it.setImageDrawable(ContextCompat.getDrawable(fragmentContext, R.drawable.icon_settings_plain))
    }

    /**
     * @param it [ImageButton]. The menu button being listened.
     */
    private fun navigateToSettings(it: ImageButton) {
        // Set tag.
        it.tag = "home"

        // Set icon.
        it.setImageDrawable(ContextCompat.getDrawable(fragmentContext, R.drawable.icon_home_plain))

        findNavController().navigate(R.id.action_global_settingsFragment)
    }

    /**
     * @param it [ImageButton]. The menu button being listened.
     */
    private fun navigateToHome(it: ImageButton) {
        changeToSettingsButton(it)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_app_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentContext = view.context

        menuButton = view.findViewById(R.id.appbar_menuButton)

        // Navigate to settings
        menuButton.setOnClickListener {
            when(it.tag) {
                "settings" -> navigateToSettings(it as ImageButton)
                else -> navigateToHome(it as ImageButton)
            }
        }
    }
}
