package com.salahtawqit.coffee.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.viewmodels.SharedViewModel

/**
 * The application's app bar.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class AppBarFragment : Fragment() {
    private lateinit var fragmentContext: Context
    private lateinit var menuButton: ImageButton
    private val sharedViewModel: SharedViewModel by activityViewModels()

    /**
     * The app bar's title that indicates the current controller state.
     */
    lateinit var appBarTitle: TextView

    private fun ImageButton.changeToSettingsButton() {
        // Set tag.
        this.tag = "settings"

        // Set icon.
        this.setImageDrawable(ContextCompat.getDrawable(fragmentContext, R.drawable.icon_settings_plain))
    }

    private fun ImageButton.changeToHomeButton() {
        // Set tag.
        this.tag = "home"

        // Set icon.
        this.setImageDrawable(ContextCompat.getDrawable(fragmentContext, R.drawable.icon_home_plain))
    }

    private fun ImageButton.changeToContactFormButton() {
        // Set tag.
        this.tag = "contactForm"

        // Set icon.
        this.setImageDrawable(ContextCompat.getDrawable(fragmentContext, R.drawable.icon_settings_plain))
    }

    private fun navigateToSettings(it: ImageButton) {
        it.changeToHomeButton()
        findNavController().navigate(R.id.action_global_settingsFragment)
    }

    private fun navigateToHome(it: ImageButton) {
        it.changeToSettingsButton()
        sharedViewModel.appBarTitle.value = getString(R.string.app_name)
        findNavController().popBackStack()
    }

    private fun navigateBackToSettings(it: ImageButton) {
        it.changeToHomeButton()
        activity?.onBackPressed()
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
        appBarTitle = view.findViewById(R.id.appbar_title)
        menuButton = view.findViewById(R.id.appbar_menuButton)

        // Navigate to settings
        menuButton.setOnClickListener {
            when(it.tag) {
                "settings" -> navigateToSettings(it as ImageButton)
                "contactForm" -> navigateBackToSettings(it as ImageButton)
                else -> navigateToHome(it as ImageButton)
            }
        }

        // Update the appBarTitle when the mutableLiveData instance gets updated.
        sharedViewModel.appBarTitle.observe(viewLifecycleOwner) {
            appBarTitle.text = it
        }

        // Update the menu button based on the current fragment.
        sharedViewModel.currentFragmentLabel.observe(viewLifecycleOwner) {
            when(it) {
                SettingsFragment::class.simpleName -> menuButton.changeToHomeButton()
                FeatureRequestFragment::class.simpleName -> menuButton.changeToContactFormButton()
                ContactFormFragment::class.simpleName -> menuButton.changeToContactFormButton()
                else -> menuButton.changeToSettingsButton()
            }
        }
    }
}
