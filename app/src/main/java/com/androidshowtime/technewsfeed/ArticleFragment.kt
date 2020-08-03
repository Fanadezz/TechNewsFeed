package com.androidshowtime.technewsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.androidshowtime.technewsfeed.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {
    private lateinit var viewModel: NewsViewModel

    //getting the Navigation Args class
    private val args: ArticleFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //initializing ViewModel
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        //binding object
        val binding = FragmentArticleBinding.inflate(inflater)


        // Allowing DataBinding to Observe LiveData with the lifecycle of this Activity
        binding.lifecycleOwner = this

        /*giving the binding access to the OverviewViewModel - because we have set the
         lifecycle Owner any LiveData used in data binding will automatically be
         observed for any changes and the UI will be updated accordingly*/
        binding.viewModel = viewModel

        // enable Java Script to allow web interactions
        binding.webView.settings.javaScriptEnabled

        //get url from the args class
        val url = args.url


        //inserting fragment title on the ActionBar from title String passed over to Navigation
        (activity as AppCompatActivity).supportActionBar?.title = args.title

        //load site
        binding.webView.loadUrl(url)

        // Inflate the layout for this fragment using binding object
        return binding.root
    }


}