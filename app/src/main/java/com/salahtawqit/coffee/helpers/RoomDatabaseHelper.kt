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

    @Entity(indices = [Index(value = ["city"], unique = true)])
    data class RecentSearch(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "country") val country: String,
        @ColumnInfo(name = "city") val city: String,
    )

    @Dao
    interface CalculationResultsDao {

        @Insert
        fun insert(calculationResults: CalculationResults)

        @Query("SELECT * FROM CalculationResults")
        fun selectAll(): List<CalculationResults>

        @Update(onConflict = OnConflictStrategy.REPLACE)
        fun update(calculationResults: CalculationResults)
    }

    @Dao
    interface RecentSearchesDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(recentSearch: RecentSearch)

        @Query("SELECT * FROM RecentSearch")
        fun selectAll(): List<RecentSearch>

        @Delete
        fun delete(recentSearch: RecentSearch)

        @Query("DELETE FROM RecentSearch")
        fun deleteAll()
    }

    /**
     * The actual database instance.
     *
     * version = 2) Added RecentSearch.
     *
     * @since v1.0
     * @author Devshot devshot.coffee@gmail.com
     */
    @Database(
        version = 3,
        exportSchema = true,
        entities = [CalculationResults::class, RecentSearch::class],
        autoMigrations = [
            AutoMigration(from = 2, to = 3)
        ]
    )
    abstract class MyDatabase: RoomDatabase() {
        /**
         * Abstract method for receiving a [CalculationResultsDao] instance.
         * @return [CalculationResultsDao]. Instance of the data access object.
         */
        abstract fun getCalculationResultsDao(): CalculationResultsDao

        /**
         * Abstract method for receiving a [RecentSearchesDao] instance.
         * @return [RecentSearchesDao]. Instance of the data access object.
         */
        abstract fun getRecentSearchesDao(): RecentSearchesDao
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