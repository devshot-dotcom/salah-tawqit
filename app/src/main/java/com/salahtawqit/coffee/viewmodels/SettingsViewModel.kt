package com.salahtawqit.coffee.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salahtawqit.coffee.helpers.RoomDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for the SettingsFragment.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Clear recent searches from database in a background thread.
     */
    fun clearSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val recentSearchesDao = RoomDatabaseHelper
                .getRoom(getApplication()).getRecentSearchesDao()

            recentSearchesDao.deleteAll()
        }

        Toast.makeText(getApplication(),
            "Search history cleared successfully", Toast.LENGTH_SHORT).show()
    }
}