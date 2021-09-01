package com.salahtawqit.coffee.viewmodels

import android.app.Application
import android.location.Address
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.jakewharton.threetenabp.AndroidThreeTen
import com.salahtawqit.coffee.R
import com.salahtawqit.coffee.getTimezoneOffsetFrom
import com.salahtawqit.coffee.helpers.InvalidResponseException
import com.salahtawqit.coffee.helpers.NetworkException
import com.salahtawqit.coffee.helpers.TimezoneData
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ViewModel for requesting timezone for an address using [RetrofitClient].
 * @since v1.0.1
 * @author Devshot devshot.coffee@gmail.com
 */
class TimezoneViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application
    private val retrofitCallback = object: Callback<TimezoneData> {

        /**
         * Invoked for a received HTTP response.
         *
         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
         * Call [Response.isSuccessful] to determine if the response indicates success.
         */
        override fun onResponse(call: Call<TimezoneData>, response: Response<TimezoneData>) {
            if(!response.isSuccessful) {
                Toast.makeText(context, context.getString(R.string
                    .timezone_failure_message), Toast.LENGTH_LONG).show()
                timezoneUsageError.value = true
                return
            }

            getTimezoneFrom(response.body()?.getZoneName())
        }

        /**
         * Invoked when a network exception occurred talking to the server or when an unexpected exception
         * occurred creating the request or processing the response.
         */
        override fun onFailure(call: Call<TimezoneData>, t: Throwable) {
            Toast.makeText(context, context.getString(R.string
                .network_connection_too_slow), Toast.LENGTH_LONG).show()
            networkError.value = true
        }
    }
    val networkError = MutableLiveData(false)
    val timezoneUsageError = MutableLiveData(false)
    var apiCall: Call<TimezoneData>? = null

    /**
     * The long awaited offset,
     * assigned the value when the request is successfully processed and the zone name from the
     * request is parsed into the offset.
     */
    val timezoneOffset = MutableLiveData<String?>(null)

    /**
     * Interface to be used to request timezone
     * from Google Timezone API.
     * @since v1.0.1
     * @author Devshot devshot.coffee@gmail.com
     */
    interface TimezoneApi {
        @GET("get-time-zone")
        fun getTimezoneData(
            @Query("key") key: String,
            @Query("format") format: String,
            @Query("by") by: String,
            @Query("lat") lat: Double,
            @Query("lng") lng: Double
        ): Call<TimezoneData>
    }

    /**
     * @param zoneName [String], the timezone to calculate the offset of.
     * @throws Exception when the [getTimezoneOffsetFrom] fails to parse
     * the timezone or the zone name is empty.
     */
    private fun getTimezoneFrom(zoneName: String?) {
        try {
            AndroidThreeTen.init(context)
            val unFormattedOffset = getTimezoneOffsetFrom(name = zoneName)

            // Replacing `+05:00` with `+05.00` to make conversion to Double work correctly.
            timezoneOffset.value = unFormattedOffset.toString().replace(":", ".")
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string
                .timezone_failure_message), Toast.LENGTH_LONG).show()
            timezoneUsageError.value = true
        }
    }

    /**
     * Request timezone for the provided address.
     * @param address [Address], the address of the user's location.
     * @throws [NetworkException] when the endpoint couldn't be reached.
     * @throws [InvalidResponseException] when the received response is erred.
     */
    fun requestTimezoneFor(address: Address) {

        val retrofitClient = Retrofit.Builder()
            .baseUrl(context.getString(R.string.TIMEZONE_API_ENDPOINT))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        val timezoneApi = retrofitClient.create(TimezoneApi::class.java)

        // Pass query parameters.
        apiCall = timezoneApi.getTimezoneData(
            key = context.getString(R.string.TIMEZONE_API_KEY),
            format = "json",
            by = "position",
            lat = address.latitude,
            lng = address.longitude
        )

        // Request.
        apiCall?.enqueue(retrofitCallback)

        // Start timeout timer.
    }
}