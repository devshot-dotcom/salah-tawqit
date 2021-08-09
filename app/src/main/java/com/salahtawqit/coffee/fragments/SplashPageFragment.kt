package com.salahtawqit.coffee.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.salahtawqit.coffee.R

/**
 * The first page visible to a user.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class SplashPageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_page, container, false)
    }
}