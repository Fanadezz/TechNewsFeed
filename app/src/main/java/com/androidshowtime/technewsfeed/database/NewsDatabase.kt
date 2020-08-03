package com.androidshowtime.technewsfeed.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//entities are the data class i.e. NewsEntity
@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)

//defining abstract class that can't be instantiated and extends RoomDatabase
abstract class NewsDatabase : RoomDatabase() {

    //NewsDatabase needs to know about DAO and this value is used for testing
    abstract val newsDAO: NewsDAO

    /*companion object allows clients to access the methods for getting the database
    without instantiating the class*/
    companion object {

        /*INSTANCE keeps reference to database once created
        it's marked with @volatile to avoid caching making it always up-to-date*/

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        /* get instance() takes a context which database builder needs
           it returns a Database Type*/

        fun getDatabaseInstance(context: Context): NewsDatabase {


            //to ensure only one thread enters the block at a time
            synchronized(this) {
                //smart-cast in local variables

                var instance = INSTANCE
                //if-statament to check if instance is null i.e. there is no database yet

                if (instance == null) {
                    //invoking databaseBuilder(context, database class, database name)
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java, "news_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    //assign INSTANCE = instance as the final step inside if-statement
                    INSTANCE = instance
                }
                //return instance at the end of Synchronized block
                return instance
            }
        }
    }
}

