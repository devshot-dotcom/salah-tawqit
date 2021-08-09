package com.salahtawqit.coffee.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.Utilities
import java.util.*


/**
 * Request user location.
 *
 * - Starting point: [calculateUserLocation] (Sets the [loadingStatus], safe requests location
 * updates, starts a timer [requestTimer])
 * - [MyLocationListener] handles 2 cases;
 * A location update AND A case where a registered location provider gets disabled.
 * - In case a fixed time has passed and location isn't received, [locationTimerTask] cancels the
 * request and updates the LiveData instances ([loadingStatus],[isLocationErred]) to inform the
 * observers in [com.salahtawqit.coffee.fragments.LoadingPageFragment].
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 * @param application [Application]. The application context.
 */
class LocationHelperViewModel(application: Application) : AndroidViewModel(application) {
    private val utilities = Utilities()
    private val locationManager : LocationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val loadingStatus = MutableLiveData(application.getString(R.string.calculating_timings))
    val location = MutableLiveData<Location>()
    val isLocationErred = MutableLiveData(false)
    private lateinit var requestTimer : Timer

    inner class MyLocationListener : LocationListener {
        /**
         * Listen for location updates.
         * Listen for updates and cancel them when the first one is received.
         */
        override fun onLocationChanged(updatedLocation: Location) {
            removeUpdates()
            loadingStatus.value = getApplication<Application>().getString(R.string.located_user_location)
            location.value = updatedLocation
        }

        /**
         * In case any of the registered providers gets cancelled.
         * Update the livedata instances to inform their observers in
         * [com.salahtawqit.coffee.fragments.LoadingPageFragment] and cancel location updates.
         */
        override fun onProviderDisabled(provider: String) {
            removeUpdates()
            loadingStatus.postValue(getApplication<Application>().getString(R.string.error_finding_location))
            isLocationErred.postValue(true)
        }
    }

    private val locationListener = MyLocationListener()

    /**
     * Cancel running location request when [requestTimer] runs out.
     */
    private val locationTimerTask : TimerTask = object : TimerTask() {
        @SuppressLint("MissingPermission")
        override fun run() {
            /** [loadingStatus] changes when location is received. */
            if (loadingStatus.value != application.getString(R.string.located_user_location)){
                removeUpdates()
                loadingStatus.postValue(application.getString(R.string.error_finding_location))
                isLocationErred.postValue(true)
            }
        }
    }

    /**
     * Remove [locationManager] updates.
     */
    fun removeUpdates() {
        locationManager.removeUpdates(locationListener)
    }

    /**
     * Calculates the user's location using an enabled provider.
     */
    @SuppressLint("MissingPermission")
    fun calculateUserLocation() {

        /** [loadingStatus] is being observed in the corresponding fragment */
        loadingStatus.value = getApplication<Application>().getString(R.string.finding_locating)

        // Request user location using GPS.
        if(utilities.isGpsEnabled(getApplication()))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        // Request user location using network.
        if(utilities.isNetworkEnabled(getApplication()))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

        // Start a timer to monitor location requests.
        requestTimer = Timer()
        requestTimer.schedule(locationTimerTask, 30000)
    }
}