package com.salahtawqit.coffee.viewmodels

import android.app.Application
import android.location.Address
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.salahtawqit.coffee.R

/**
 * A shared [ViewModel] that is instantiated in the [com.salahtawqit.coffee.MainActivity].
 *
 * Contains shared variables to be used between fragments. Fragments can update their views, perform
 * calculation, and other operations by observing, checking, and updating the variables provided in
 * this view model.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class SharedViewModel(application: Application): AndroidViewModel(application) {

    /**
     * [MutableLiveData]<[String]> instance that contains the text for
     * [com.salahtawqit.coffee.fragments.AppBarFragment.appBarTitle]. Update the value from a
     * fragment with an appropriate string to display it in the app bar's title.
     */
    val appBarTitle = MutableLiveData(application.getString(R.string.app_name))

    /**
     * [MutableLiveData]<[String]> instance that contains the class name of the current fragment in
     * the navigation host.
     */
    val currentFragmentLabel = MutableLiveData<String>()

    /**
     * Boolean variable that indicates whether the navigation controller has navigated from
     * [com.salahtawqit.coffee.fragments.LandingPageFragment] or not.
     */
    var isLandingPageDisplaced = false

    /**
     * Updated from the [com.salahtawqit.coffee.fragments.SettingsHierarchyFragment] when the user
     * updates a preference.
     */
    var areSettingsUpdated = false

    /**
     * Shared address between ManualCalculationFragment & LoadingPageFragment.
     *
     * When the user manually searches for a location,
     * the former fragment does it's thing and sets this address
     * for the later fragment to request timezone from an API for this address.
     */
    var manualAddress: Address? = null
}