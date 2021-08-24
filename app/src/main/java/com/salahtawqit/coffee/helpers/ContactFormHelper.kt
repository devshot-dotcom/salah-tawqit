package com.salahtawqit.coffee.helpers

import android.app.Activity
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.hideKeyboard
import com.salahtawqit.coffee.showMailingApps

/**
 * Helper to control everything related to a contact form.
 *
 * @param context [Context]. The application context.
 * @param subject [String]. The e-mail subject of the contact form.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ContactFormHelper(private val context: Activity, private val subject: String) {

    var displayName: EditText = context.findViewById(R.id.display_name_edit_text)
    var message: EditText = context.findViewById(R.id.message_edit_text)
    private var submitButton: TextView = context.findViewById(R.id.submit_button)

    /**
     * @return [Boolean]. True if [displayName] && [message] are empty, false otherwise.
     */
    private fun areFieldsEmpty(): Boolean {
        return displayName.text.isEmpty() or message.text.isEmpty()
    }

    private fun setListeners() {
        // Enable submit button if displayName and message are not empty, disable otherwise.
        displayName.doOnTextChanged { _, _, _, _ -> submitButton.isEnabled = !areFieldsEmpty() }
        message.doOnTextChanged { _, _, _, _ -> submitButton.isEnabled = !areFieldsEmpty() }

        submitButton.setOnClickListener {
            context.hideKeyboard()

            showMailingApps(
                context = context,
                to = context.getString(R.string.our_mail),
                subject = "${context.getString(R.string.app_name)}: $subject",
                message = "From: ${ displayName.text } \n ${ message.text }"
            )
        }
    }

    init {
        setListeners()
    }
}