package com.androidshowtime.technewsfeed

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

//base URL
private const val BASE_URL = "https://hacker-news.firebaseio.com/"

/*Using MoshiBuilder to create Moshi Object ie. the JSON-String to
KotlinJsonAdapterFactory*/


/*For Moshi's annotations to work properly with Kotling, add the
KotlinJsonAdapterFactory*/

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


//using RetrofitBuilder to create Retrofit Object - lenient to avoid malformed JSON
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()


interface NewsInterface {

    @GET("v0/item/{id}.json?print=pretty")
//@Query("id") storyId: Int, @Query("path"path: String)
    fun getNewsItemsAsync(@Path("id") id: Int): Deferred<NewsItem>


    @GET("v0/topstories.json?print=pretty")
    fun getNewsItemsIds(): Deferred<MutableList<Int>>
}

//https://hacker-news.firebaseio.com/v0/item/23802241.json?print=pretty
//https://hacker-news.firebaseio.com/v0/item/23802241.json?print=pretty
object NewsService {
    val newsService: NewsInterface by lazy { retrofit.create(NewsInterface::class.java) }
}