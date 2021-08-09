package com.salahtawqit.coffee.helpers

import android.content.Context
import androidx.room.*

/**
 * All the database related functionalities in one class.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
object RoomDatabaseHelper {
    @Entity
    /**
     * Calculation results entity.
     *
     * Contains all the data related to the calculation results i.e; Prayer times, location details,
     * etc, except the hijri and gregorian date as that'll be calculated on runtime.
     *
     * @since v1.0
     * @author Devshot devshot.coffee@gmail.com
     */
    data class CalculationResults(
        @PrimaryKey val id: Int,
        val latitude: String?,
        val longitude: String?,
        val city: String?,
        val country: String?,
        val tahajjud: String?,
        val fajr: String?,
        val sunrise: String?,
        val ishraq: String?,
        val duha: String?,
        val dhuhr: String?,
        val asr: String?,
        val sunset: String?,
        val maghrib: String?,
        val isha: String?,
    )

    @Dao
    /**
     * Calculation results DAO (data access object) interface.
     *
     * Contains all the database-related query methods allowed by the room persistence library.
     * @since v1.0
     * @author Devshot devshot.coffee@gmail.com
     */
    interface CalculationResultsDao {

        /**
         * Insert a new row of [CalculationResults] into the database.
         * @param calculationResults [CalculationResults]. The new row to be inserted.
         */
        @Insert
        fun insert(calculationResults: CalculationResults)

        /**
         * Select all rows of [CalculationResults] from the database.
         * @return [List]<[CalculationResults]>.
         */
        @Query("SELECT * FROM CalculationResults")
        fun selectAll(): List<CalculationResults>

        /**
         * Update a row of [CalculationResults] from the database.
         *
         * Note that onConflict parameter, OnConflictStrategy.REPLACE will replace any conflicting
         * data in a cell of the matching row.
         *
         * The match is done automatically by room by comparing the [CalculationResults.id] of both
         * entries. This eliminates the need to provide a unique identifier.
         */
        @Update(onConflict = OnConflictStrategy.REPLACE)
        fun update(calculationResults: CalculationResults)
    }

    @Database(
        version = 1,
        entities = [CalculationResults::class],
        exportSchema = true,
    )
    /**
     * The actual database instance.
     * @since v1.0
     * @author Devshot devshot.coffee@gmail.com
     */
    abstract class MyDatabase: RoomDatabase() {
        /**
         * Abstract method for receiving a [CalculationResultsDao] instance.
         * @return [CalculationResultsDao]. Instance of the data access object.
         */
        abstract fun calculationResultsDao(): CalculationResultsDao
    }

    /**
     * Get an instance of room database.
     * @param context [Context]. The application context.
     * @return [RoomDatabase]. The database instance.
     */
    fun getRoom(context: Context): MyDatabase {
        return Room.databaseBuilder(context,
            MyDatabase::class.java, "salah-tawqit").build()
    }
}