# Salah Tawqit - Android

> This thread contains all the documents related to Salah Tawqit's Android version.

External Libraries

- [Umm-Al-Qura Calendar](https://github.com/msarhan/ummalqura-calendar) for Hijri date calculation.
- [GSON by Google](https://github.com/google/gson) for parsing JSON strings.
- [Prayer Times Calculator](http://praytimes.org/code/) to calculate prayer timings.

The app is built using the Modern Android Development ([MAD](https://developer.android.com/modern-android-development)) patterns, using Kotlin and the Model-View-ViewModel ([MVVM](https://developer.android.com/jetpack/guide)) architecture.

---

## Release Notes

> **version 1.0**

#### Package List

com.salahtawqit.coffee
|_ calculators
|_ fragments
|_ helpers
|_ viewmodels

<br>

#### Class List

`com.salahtawqit.coffee`
|_ MainActivity.kt
|_ Utilities.kt

`calculators`
|_ PrayerTimesCalculator.java
|_ VoluntaryPrayersCalculator.kt

`fragments`
|_ LandingPageFragment.kt
|_ LoadingPageFragment.kt
|_ LocationRationaleFragment.kt
|_ ManualCalculationFragment.kt
|_ NetworkErrorFragment.kt
|_ PrayerTimesFragment.kt
|\_ SplashPageFragment.kt

`helpers`
|_ AutomaticCalculationHelper.kt
|_ JsonHelper.kt
|_ PreferencesHelper.kt
|_ RoomDatabaseHelper.kt

`viewmodels`
|_ CalculationHelperViewModel.kt
|_ LocationHelperViewModel.kt
|_ ManualCalculationViewModel.kt
|_ PrayerTimesViewModel.kt

---

## Workflow

- The app launch triggers the start of `MainActivity`.
- MainActivity starts navigation component, initializes `CalculationHelperViewModel`, shows `SplashPageFragment` and the appbar.
- Navigation component loads `LandingPageFragment`.
- LandingPageFragment checks the database for existing prayer times.

1. If prayer times exist, `CalculationHelperViewModel` is used to create a `HashMap` from the data retrived from database and `PrayerTimesFragment` is navigated upon, which displays this data.
2. If prayer times don't exist, the user has an option to select either _Automatic Calculation_ or _Manual Calculation_.

Both the cases are handled below.

**Automatic Calculation**

- `AutomaticCalculationHelper` is called to check for location access, network access, and location permission.

1. In case location permission isn't granted, ask for permission or show `LocationRationaleFragment` if the user has denied it beforehand.

2. In case location isn't accessible, show a `Toast` saying the problem.

3. In case network isn't accesible, navigate to the `NetworkErrorFragment`.

4. In case no error is seen, navigate to `LoadingPageFragment` and pass it an identifier string that says **`automatic`**.

**Manual Calculation**

- `ManualCalculationFragment` is navigated upon where the user has to enter a country name and a city name.
- Upon filling the form fields, the calculate button becomes enabled and can be clicked.
- Upon clicking the calculate button, the `ManualCalculationViewModel` is called to validate this form and then geocode the city name to match the accurate country name with country names of an external JSON file, getting the timezone offset in the process.
- All the data is stored in `CalculationHelperViewModel` and `LoadingPageFragment` is navigated upon with an identifier string that says **`manual`**.

> Either way, the `LoadingPageFragment` receives an identifier that tells which calculation mode to use.

- For automatic calculation, it retrives user location using `LocationHelperFragment` and then calculates prayer times.
- For manual calculation, it does nothing as the `ManualCalculationViewModel` does everything.
- In successful cases, it sets a HashMap in `CalculationHelperViewModel` for the next fragment to use and navigates to `PrayerTimesFragment`.
- In failure, can be network errors, location errors, geocoding errors, it rolls back to `LandingPageFragment`.
- `PrayerTimesFragment` simply retrieves the `HashMap` from `CalculationHelperViewModel` and passes it to `PrayerTimesViewModel`.
- The view model stores this map in the database.
- The view model calculates hijri and gregorian dates.
- The view model converts the prayer times from 24-hour format to the user-preferred format.
- The fragment then binds the view model to its `DataBinding` class and displays the user interface.

---

## Class Documentation

`MainActivity`
: The entry point of the app. Initiates the `SplashPageFragment` and the navigation host that maintains and navigates through all the fragments, gracefully.

`Utilities`
: Wide variety of utilities for every other class to use. Notable examples would be `isConnectedToInternet()`. `addMinutes()`, and `subtractMinutes()` methods.

<br>

#### Calculators

`PrayerTimesCalculator`
: The only JAVA class being used in the app, of course before compilation of Kotlin code. Downloaded from an external git respository, manages the calculation of prayer times using latitude, longitude, and timezone of the client.

`VoluntaryPrayersCalculator`
: The calculator for the nafl/voluntary prayers. Uses some of the prayer times calculated by the former class to calculate further times using the `addMinutes()` and `subtractMinutes()` utilities of the `Utilities.kt`.

<br>

#### Fragments

`LandingPageFragment`
: The landing page of the app, also the host fragment for the application-wide navigation component. Manages the initialization of calculation modes, checks whether calculation results exist in the database and acts accordingly. Has the power to navigate to all other fragments in it's navigation host component, including the `PrayerTimesFragment.kt` where it pre-populates a Kotlin `HashMap` from the database in case of existing results.

`LoadingPageFragment`
: The loading page of the app. The end user sees a simple loading animation and a status text that displays what is happening. Meanwhile, prayer times are being calculated, user location is being calculated, and other background stuff is happening.

`LocationRationaleFragment`
: When the user needs automatic prayer times but doesn't wanna let us access their location, this fragment comes into play. A simple yet authentic message is displayed that says hey, we need to access your location for accurate prayer times, or you can use manual calculation. Allowing the user to either grant location access or switch to manual mode.

`ManualCalculationFragment`
: Possibly the first form the user will see in the app, instead of accessing their location for latitude, longitude, and timezone, we display a form that asks for the country and city.

`NetworkErrorFragment`
: Shown when user's network isn't stable. Network access is required for either location or geocoding purposes.

`PrayerTimesFragment`
: The most critical fragment of the app, displays the actual prayer times from a `HashMap` retrieved from `CalculationHelperViewModel`

`SplashPageFragment`
: The first screen the user sees, displays the app logo and the build version.

<br>

#### Helpers

`AutomaticCalculationHelper`
: An extension class for the `LandingPageFragment`, which is extended to prevent bloating and increase readability. Performs several checks before starting automatic calculation.

`JsonHelper.kt`
: It's a file, not a class. GSON requires classes modelled after JSON strings, this file contains those models.

`PreferencesHelper`
: User preferences can be hard to maintain, this class handles the hard work and provides simple setter and getter methods.

`RoomDatabaseHelper`
: Helper for the room persistence library. Is a Kotlin `object`, which is a singleton way of creating a class. Provides a database instance, entities, and several Data Access Objects (DAOs).

<br>

#### ViewModels

`CalculationHelperViewModel`
: The only global view model so far, initiated in the `MainActivity`, contains a `HashMap` that contains prayer times, location information, and other details that need to be shown to the user in the `PrayerTimesViewModel`.

`LocationHelperViewModel`
: Can also be called as `LoadingPageViewModel` but, it does what it says so, that is its name. Calculates user location.

`ManualCalculationViewModel`
: View model for the `ManualCalculationFragment`. Validates the country-city form, calculates timezone, parses external JSON file.

`PrayerTimesViewModel`
: View model for the `PrayerTimesFragment`. Stores the hashmap obtained from `CalculationHelperViewModel` into the database, calculates hijri and gregorian dates.
