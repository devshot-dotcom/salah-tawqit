package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.salahtawqit.coffee.databinding.FragmentSettingsBinding
import com.salahtawqit.coffee.viewmodels.SettingsViewModel

/**
 * The fragment that holds both [com.salahtawqit.coffee.fragments.SettingsHierarchyFragment] and
 * the new additional options for the settings page.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

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
        binding.fragment = this
    }
}