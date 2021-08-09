package com.salahtawqit.coffee.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.Utilities
import com.salahtawqit.coffee.fragments.HomePageFragmentDirections
import com.salahtawqit.coffee.fragments.LoadingPageFragment
import com.salahtawqit.coffee.fragments.LocationRationaleFragment
import com.salahtawqit.coffee.fragments.NetworkErrorFragment

/**
 * Instantiate and handle automatic calculation mode.
 *
 * The class is a module that was once part of the [com.salahtawqit.coffee.fragments.LandingPageFragment]
 * before it was separated into it's own module to reduce bloating and increase readability.
 *
 * @constructor requires [Context], [NavController], [ActivityResultLauncher] since all 3 of these
 * were also a part of the parent fragment.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class AutomaticCalculationHelper(
    private val context: Context,
    private val navController: NavController,
    private val resultLauncher: ActivityResultLauncher<String>) {
    private val utilities = Utilities()
    private val permissionName = Manifest.permission.ACCESS_FINE_LOCATION

    /**
     * Handle whether to show a rationale or to request the permission.
     * Notice that the function returns as soon as the rationale is navigated upon.
     */
    private fun handlePermission() {
        /**
         * Navigate to [LocationRationaleFragment] in case it's proposed.
         * The fragment will navigate to [LoadingPageFragment] in certain use cases and
         * to provide a calculation mode to the [LoadingPageFragment], we pass the required
         * string to [LocationRationaleFragment].
         */
        if(shouldShowRequestPermissionRationale(context as Activity, permissionName)) {
            navController.navigate(HomePageFragmentDirections
                .actionHomeScreenFragmentToLocationRationaleFragment("automatic"))
            return
        }

        // Request location permission
        resultLauncher.launch(permissionName)
    }

    /**
     * Starting point of the class's functionality.
     * Performs several checks before navigating to the next fragment.
     *
     * 1) Checks whether GPS is enabled, otherwise it'd be useless to navigate ahead.
     * 2) Checks whether internet connection is stable.
     * 3) Checks whether location access is granted.
     *
     * In case all checks return true, navigates to the next fragment.
     */
    fun onButtonClick() {

        // Check for location provider's availability (at least one must be enabled).
        if(!utilities.isGpsEnabled(context) && !utilities.isNetworkEnabled(context)) {
            /**
             * The case represents the disabled status of GPS & network service.
             * Show a valid reason why the calculation stopped and what to do in case enabling
             * location isn't an option.
             */
            Toast.makeText(
                context, context.getString(R.string.location_access_disabled), Toast.LENGTH_LONG).show()
            return
        }

        /**
         * Navigate to [NetworkErrorFragment] if offline
         * Add the "automatic" as an argument to the [navigation] action.
         */
        if(!utilities.isConnectedToInternet(context)) {
            navController.navigate(
                HomePageFragmentDirections
                    .actionHomeScreenFragmentToNetworkErrorFragment("automatic"))
            return
        }

        /**
         * Check if [permissionName] is not granted.
         * In case of automatic calculation, user's location needs to be known and that requires
         * the [permissionName] permission to be granted at runtime.
         */
        if(!utilities.hasPermission(context, permissionName)) {
            handlePermission()
            return
        }

        /** Else navigate to the [LoadingPageFragment] */
        navController.navigate(HomePageFragmentDirections
            .actionHomeScreenFragmentToLoadingPageFragment("automatic"))
    }
}