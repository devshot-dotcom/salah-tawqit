package com.salahtawqit.coffee

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The global utilities at a single place.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class Utilities {

    /**
     * Check client's network connectivity.
     * @return [Boolean] Whether the client is connected or not.
     */
    fun isConnectedToInternet(context: Context) : Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // In case the capabilities are null, Elvis [?:] returns false [not connected to internet]
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false

        if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            return true

        if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            return true

        if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            return true

        // Other network capabilities don't quality for actual internet connectivity.
        return false
    }

    /**
     * Calculate the timezoneOffset for the current location.
     * [Taken from this answer on StackOverflow][https://stackoverflow.com/a/15068851/14716989]
     * @return [Long] timezoneOffset
     */
    fun getTimezoneOffset() : Long {
        val mCalendar: Calendar = GregorianCalendar()
        val mTimeZone = mCalendar.timeZone
        val mGMTOffset =
            mTimeZone.rawOffset + if (mTimeZone.inDaylightTime(Date())) mTimeZone.dstSavings else 0
        return TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS)
    }

    /**
     * Check if permission has been granted.
     * @param permissionName [String] The permission name from [Manifest]
     * @return [Boolean] Whether the permission has been granted or not.
     */
    fun hasPermission(context : Context, permissionName : String) : Boolean {
        return ContextCompat.checkSelfPermission(
            context, permissionName) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check whether GPS provider is enabled.
     * @param context [Context] The application context.
     */
    fun isGpsEnabled(context: Context) : Boolean {
        val locationManager : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Check whether network provider is enabled.
     * @param context [Context] The application context.
     */
    fun isNetworkEnabled(context: Context) : Boolean {
        val locationManager : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Check whether GPS provider is enabled.
     * @param application [Application] The application's instance..
     */
    fun isGpsEnabled(application: Application) : Boolean {
        val locationManager : LocationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * Check whether network provider is enabled.
     * @param application [Application] The application's instance..
     */
    fun isNetworkEnabled(application: Application) : Boolean {
        val locationManager : LocationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Add minutes to string of time.
     *
     * - Make [Date] object using the provided [format] and the [source] time string.
     * - Convert the [minutes] to milliseconds.
     * - Add the converted [minutes] and the [source] time by getting milliseconds of the [source]
     * using [Date].time method.
     *
     * @param minutes [Int]. The number of minutes to add.
     * @param source [String]. The time string to add minutes to.
     * @param format [SimpleDateFormat]. The time format used to make time transactions.
     *
     * @return [String]. The [source] time string with [minutes] added to it.
     */
    fun addMinutes(minutes: Int, source: String, format: SimpleDateFormat): String {
        val date: Date? = format.parse(source)

        // Minutes to milliseconds.
        val minutesInMillis = (minutes * 60 * 1000).toLong()
        val timeInMillis = date?.time?.plus(minutesInMillis)

        //format milliseconds into a time string
        return format.format(timeInMillis)
    }

    /**
     * Subtract minutes from a string of time.
     *
     * - Make [Date] object using the provided [format] and the [source] time string.
     * - Convert the [minutes] to milliseconds.
     * - Subtract the converted [minutes] from the [source] time by getting milliseconds of the
     * [source] using [Date].time method.
     *
     * @param minutes [Int]. The number of minutes to subtract.
     * @param source [String]. The time string to subtract minutes from.
     * @param format [SimpleDateFormat]. The time format used to make time transactions.
     *
     * @return [String]. The [source] time string with [minutes] subtracted from it.
     */
    fun subtractMinutes(minutes: Int, source: String, format: SimpleDateFormat): String {
        val date: Date? = format.parse(source)

        // Minutes to milliseconds.
        val minutesInMillis = (minutes * 60 * 1000).toLong()
        val timeInMillis = date?.time?.minus(minutesInMillis)

        //format milliseconds into a time string
        return format.format(timeInMillis)
    }
}