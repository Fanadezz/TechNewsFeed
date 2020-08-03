package com.androidshowtime.technewsfeed.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "news_items_table")
data class NewsEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "news_item_id_column")
    var newsItemId: Int,
    @ColumnInfo(name = "title_column")
    var title: String,
    @ColumnInfo(name = "url_column")
    var url: String


)