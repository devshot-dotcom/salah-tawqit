package com.salahtawqit.coffee.helpers

import com.google.gson.annotations.SerializedName

/**
 * The class that represents json objects containing a country name and a timezone offset.
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class CountrySet {
    private lateinit var timezoneOffset: String
    private lateinit var name: String

    fun getName(): String {
        return name
    }

    fun getTimezoneOffset(): String {
        return timezoneOffset
    }
}

/**
 * Model class for Timezone API's response.
 * @since v1.0.1
 * @author Devshot devshot.coffee@gmail.com
 */
class TimezoneData(
    @SerializedName("status")
    private var status: String = "",

    @SerializedName("message")
    private var message: String = "",

    @SerializedName("countryCode")
    private var countryCode: String = "",

    @SerializedName("CountryName")
    private var countryName: String = "",

    @SerializedName("zoneName")
    private var zoneName: String = "",

    @SerializedName("abbreviation")
    private var abbreviation: String = "",

    @SerializedName("gmtOffset")
    private var gmtOffset: String = "",

    @SerializedName("dst")
    private var dst: String = "",

    @SerializedName("zoneStart")
    private var zoneStart: String = "",

    @SerializedName("zoneEnd")
    private var zoneEnd: String = "",

    @SerializedName("nextAbbreviation")
    private var nextAbbreviation: String = "",

    @SerializedName("timestamp")
    private var timestamp: String = "",

    @SerializedName("formatted")
    private var formatted: String = "",
) {
    fun getZoneName(): String {
        return this.zoneName
    }
}