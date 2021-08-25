package com.salahtawqit.coffee

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

/**
 * Hide keyboard from a fragment.
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/**
 * Hide keyboard from an activity.
 */
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * Hide soft keyboard.
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Convert string to editable.
 * @return [String]. The editable retrieved from a string.
 */
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

/**
 * Show the option to select a mailing app.
 *
 * @param context [Context]. the application context.
 * @param to [String]. The receiver's e-mail address, it's the development team, mostly.
 * @param subject [String]. The e-mail's subject.
 * @param message [String]. The e-mail's message.
 */
fun showMailingApps(context: Context,
    to: String, subject: String, message: String) {

    val mailIntent = Intent(Intent.ACTION_SEND)

    // Appropriate data and type are necessary for the best UX.
    mailIntent.data = Uri.parse("mailto:")
    mailIntent.type = "text/plain"

    mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
    mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    mailIntent.putExtra(Intent.EXTRA_TEXT, message)

    try {
        context.startActivity(Intent.createChooser(mailIntent, "Send mail..."))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context,
            context.getString(R.string.email_error), Toast.LENGTH_SHORT).show()
    }
}

/**
 * Hide parent element of this view.
 */
fun View.hideParent() {
    val parent = this.parent as View
    parent.visibility = View.GONE
}

/**
 * Open a url in the browser.
 * @param context [Context], the application context.
 * @param url [String], the URL to open in the browser.
 */
fun browse(context: Context?, url: String) {
    val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    // Starting an activity from a non-activity context throws IllegalStateException
    browseIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    context?.startActivity(browseIntent)
}