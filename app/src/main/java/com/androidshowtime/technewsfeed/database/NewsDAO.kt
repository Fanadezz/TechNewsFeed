package com.androidshowtime.technewsfeed.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDAO {


    @Query("SELECT title_column FROM news_items_table")
    fun getAllTitles(): MutableList<String>

    @Query("SELECT url_column FROM news_items_table")
    fun getAllUrls(): List<String>

    //NewsDao defines methods for using the NewsEntity with Room


    @Insert
    fun insert(newsEntity: NewsEntity)


    // Selects and returns the row(s) that match the SQL arguments


    @Query("SELECT * from news_items_table")
    fun get(): NewsEntity

    /**
     * Deletes all values from the table - this does not delete the table, only its contents.
     */
    @Query("DELETE FROM news_items_table")
    fun clear()


}