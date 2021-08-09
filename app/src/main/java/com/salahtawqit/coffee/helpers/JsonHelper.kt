package com.salahtawqit.coffee.helpers

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