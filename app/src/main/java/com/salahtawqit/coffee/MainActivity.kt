package com.salahtawqit.coffee

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.salahtawqit.coffee.fragments.SplashPageFragment
import com.salahtawqit.coffee.viewmodels.CalculationHelperViewModel
import java.util.*
import kotlin.concurrent.schedule

/**
 * The entry point of the app.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class MainActivity : FragmentActivity() {
    private lateinit var calculationHelperViewModel: CalculationHelperViewModel

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

        // Add Splash Screen.
        if(savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SplashPageFragment>(R.id.fragment_splash_page)
                addToBackStack(null)
            }
        }

        navigateFromSplashScreen(savedInstanceState)

        // Initialize the ViewModel.
        calculationHelperViewModel = ViewModelProvider(this).get(CalculationHelperViewModel::class.java)
    }
}