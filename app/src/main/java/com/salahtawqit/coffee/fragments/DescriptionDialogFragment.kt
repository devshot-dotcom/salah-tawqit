package com.salahtawqit.coffee.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * Dialog fragment for displaying basic descriptions.
 *
 * Contains a simple message, title, and a positive button that dismisses the dialog.
 *
 * @param title [String]. The dialog title.
 * @param description [String]. The dialog description.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class DescriptionDialogFragment(
    private val title: String,
    private val description: String
    ): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(title)
                .setMessage(description)
                .setPositiveButton(android.R.string.ok) { _, _ -> }

            // Return the dialog.
            builder.create()

        } ?: throw IllegalStateException("Activity can't be null.")
    }
}