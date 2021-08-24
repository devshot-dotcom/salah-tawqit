package com.salahtawqit.coffee

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import com.salahtawqit.coffee.viewmodels.SharedViewModel
import java.util.*
import kotlin.concurrent.schedule

/**
 * The entry point of the app.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class MainActivity : FragmentActivity() {
    /**
     * Propagate the navigation from splash screen after a delay.
     * @param savedInstanceState [Bundle] whether the savedInstanceState is available or not.
     * No need to navigateFromSplashScreen if [Bundle] exists.
     */
    private fun navigateFromSplashScreen(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) return

        Timer().schedule(2000) {

            // Hide the splash page.
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_splash_page)
            if (fragment != null) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateFromSplashScreen(savedInstanceState)

        // Initialize the ViewModels.
        ViewModelProvider(this).get(CalculationHelperViewModel::class.java)
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }
}