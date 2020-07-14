package com.androidshowtime.technewsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidshowtime.technewsfeed.database.NewsDatabase
import com.androidshowtime.technewsfeed.database.NewsEntity
import com.androidshowtime.technewsfeed.databinding.FragmentHeadlinesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HeadlinesFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel

    private lateinit var idsList: MutableList<Int>
    private lateinit var titles: MutableList<String>
    private lateinit var database: NewsDatabase


    //onCreateView() is called when the Fragment is ready to display content to the screen.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {


        // intializing database

        database = NewsDatabase.getDatabaseInstance(requireActivity())

        //initializing ViewModel
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentHeadlinesBinding.inflate(inflater)

        // setting the fragment as the lifecycle owner
        binding.lifecycleOwner = this

        /*set ViewModel for Data Binding - this allows the layout to access all data in the
          ViewModel*/
        binding.viewModel = viewModel

        //inserting fragment title on the ActionBar
        (activity as AppCompatActivity).supportActionBar?.title = "Top Topics"



        CoroutineScope(IO).launch {

            getNewsIDs()

            titles = database.newsDAO.getAllTitles()


            val urls = database.newsDAO.getAllUrls()

            /*switching CoroutineScope to the to the Main Thread to update the listView which
              cannot be done outside the main thread*/
            withContext(Main) {
                val adapter = ArrayAdapter<String>(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    titles)

                //setting ArrayAdapter to the list view
                binding.listView.adapter = adapter

                //Navigating to the Article Fragment when list item is clicked
                binding.listView.setOnItemClickListener { adapterView, view, i, l ->

                    //when navigating we put NavsArg for the url and title as Strings
                    findNavController().navigate(
                        HeadlinesFragmentDirections.actionHeadlinesFragmentToArticleFragment(
                            url = urls[i],
                            title = titles[i]
                                                                                            )
                                                )

                }

            }

        }


        // Inflate the layout for this fragment
        return binding.root
    }


    private fun getNewsIDs() {
        CoroutineScope(Unconfined).launch {
            //calling retrofit to get a Deferred Object with the Ids
            val getItemsIds = NewsService.newsService.getNewsItemsIds()
            try {

                //getting list of Ids
                idsList = getItemsIds.await()

                //calling save method to retrieve and save NewsItem objects
                saveNewsItemIntoDatabase()

            }
            catch (e: Exception) {
                //Logging the error using Timber Library
                Timber.i("$e")

            }
        }


    }

    //suspend function to fetch data and put it into Room Database
    private suspend fun saveNewsItemIntoDatabase() {


        /*	CoroutineScope(IO) -  optimized input/output operations e.g. for
         network requests or requests to local database interactions and disc operations*/
        withContext(IO) {


            //null check for the listOfIDs received from Retrofit
            if (!idsList.isNullOrEmpty()) {

                //first clear database to avoid news item entries duplication
                database.newsDAO.clear()

                //obtain the first 20 entries using for-loop
                for (value in 0..20) {
                    //getting the NewsItem Objects after passing the IDs
                    val getNewsItemDeferred =
                            NewsService.newsService.getNewsItemsAsync(idsList[value])


                    try {
                        //set the result to a NewsItem Object received with each ID looped
                        val newsItem = getNewsItemDeferred.await()

                        //save the NewsItems Object into Database passing zero for the primary key
                        database.newsDAO.insert(
                            NewsEntity(0, newsItem.id, newsItem.title, newsItem.url))
                    }
                    catch (e: java.lang.Exception) {

                        //Logging the error using Timber Library

                        Timber.i("Error Occurred - $e")

                    }

                }
            }

        }


    }
}
