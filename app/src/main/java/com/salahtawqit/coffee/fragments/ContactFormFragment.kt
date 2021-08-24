package com.salahtawqit.coffee.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.helpers.ContactFormHelper

/**
 * Fragments that allows the users to contact the developers using e-mail.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ContactFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ContactFormHelper(context = requireActivity(), "Contact Form")
    }
}