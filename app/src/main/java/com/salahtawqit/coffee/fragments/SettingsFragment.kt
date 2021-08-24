package com.salahtawqit.coffee.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.salahtawqit.coffee.BuildConfig
import com.salahtawqit.coffee.databinding.FragmentSettingsBinding
import com.salahtawqit.coffee.viewmodels.SettingsViewModel
import com.salahtawqit.coffee.viewmodels.SharedViewModel


/**
 * The fragment that holds both [com.salahtawqit.coffee.fragments.SettingsHierarchyFragment] and
 * the additional options for the settings page.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    fun navigateToFeatureRequest(v: View) {
        findNavController().navigate(SettingsFragmentDirections
            .actionSettingsFragmentToFeatureRequestFragment())
    }

    fun navigateToContactUs(v: View) {
        findNavController().navigate(SettingsFragmentDirections
            .actionSettingsFragmentToContactFormFragment())
    }

    /**
     * Ask for confirmation and then clear search history from database on a background thread.
     */
    fun clearSearchHistory(v: View) {
        var confirmationDialog: ConfirmationDialogFragment? = null

        try {
            confirmationDialog = ConfirmationDialogFragment(
                message = "Are you sure you want to delete records of manually searched locations?"
            ) { viewModel.clearSearchHistory() }
        } catch (e: IllegalStateException) {
            Toast.makeText(this.context,
                "An internal error prevented deletion of searched history, please try again",
                Toast.LENGTH_LONG).show()
        }

        // Show the dialog if it has been instantiated successfully.
        confirmationDialog?.show(parentFragmentManager, "clear-search-history")
    }

    /**
     * Open the store page of the app to request user rating.
     */
    fun rateTheApp(v: View) {
        val packageName = BuildConfig.APPLICATION_ID

        startActivity(Intent(Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$packageName")))
    }

    /**
     * Open the app's web instance in a browser that displays the privacy policy & terms of service.
     */
    fun showPrivacyPolicy(v: View) {}

    /**
     * Open the app's web instance in a browser that displays the credits.
     */
    fun showCredits(v: View) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.appBarTitle.value = "Settings"
        binding.fragment = this
    }
}