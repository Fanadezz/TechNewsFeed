package com.androidshowtime.technewsfeed

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*@Parcelize is an annotation provided by Kotlin Android Extensions
that will automatically generate the serialization implementation
for your custom Parcelable type at compile time!*/

//data class - constructor takes main and description Strings
@Parcelize
data class NewsItem(val id: Int, val title: String, val url: String) : Parcelable