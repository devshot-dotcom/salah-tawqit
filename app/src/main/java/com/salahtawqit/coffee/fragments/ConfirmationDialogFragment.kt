package com.salahtawqit.coffee.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/**
 * A simple confirmation dialog that displays a message and calls the provided click listener
 * on a confirmation action.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ConfirmationDialogFragment(
    private val message: String,
    private val listener: () -> Unit
    ): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ -> listener() }
                .setNegativeButton(android.R.string.cancel) {_, _ -> }

            // Return the dialog.
            builder.create()
        } ?: throw IllegalStateException("Activity can't be null.")
    }
}