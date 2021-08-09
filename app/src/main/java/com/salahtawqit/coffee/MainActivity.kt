package com.salahtawqit.coffee

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import java.util.*
import kotlin.concurrent.schedule

/**
 * The entry point of the app.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class MainActivity : FragmentActivity() {
    private lateinit var appbar: LinearLayout
    private lateinit var calculationHelperViewModel: CalculationHelperViewModel

    /**
     * Initialize the lateinit variables.
     */
    private fun initLateInit() {
        appbar = findViewById(R.id.appbar)
    }

    /**
     * Propagate the navigation from splash screen after a delay.
     * @param savedInstanceState [Bundle] whether the savedInstanceState is available or not.
     * No need to navigateFromSplashScreen if [Bundle] exists.
     */
    private fun navigateFromSplashScreen(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) return

        Timer().schedule(2000) {
            runOnUiThread {
                /** Navigate to [com.salahtawqit.coffee.fragments.HomePageFragment] */
                findNavController(R.id.fragment_container)
                    .navigate(R.id.action_splashScreenFragment_to_homeScreenFragment)

                /** Show the hidden [appbar] */
                appbar.visibility = View.VISIBLE
                appbar.startAnimation(AnimationUtils
                    .loadAnimation(this@MainActivity, R.anim.fade_in))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLateInit()
        navigateFromSplashScreen(savedInstanceState)

        // Initialize the ViewModel.
        calculationHelperViewModel = ViewModelProvider(this).get(CalculationHelperViewModel::class.java)
    }
}