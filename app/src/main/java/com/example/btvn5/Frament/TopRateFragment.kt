package com.example.btvn5.Frament

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.btvn5.Adapter.ItemClickListenner
import com.example.btvn5.Adapter.MovieAdapter
import com.example.btvn5.DetailMovieActivity
import com.example.btvn5.Model.*

import com.example.btvn5.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_top_rate.*
import okhttp3.*
import java.io.IOException



class TopRateFragment : Fragment() {

    var movie_rate: ArrayList<Movie.Results> = ArrayList()
    lateinit var toprateAdapter: MovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMovie()
        toprateAdapter = MovieAdapter(movie_rate,activity)

    }

    private fun addMovie() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/top_rated?api_key=7519cb3f829ecd53bd9b7007076dbe23")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    activity!!.runOnUiThread {
                        call.cancel()
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()!!.string()
                    val datagson = Gson().fromJson(json, Movie.ResultArray::class.java)
                    activity!!.runOnUiThread {
                        movie_rate.clear()
                        for (i in datagson.results) {
                            movie_rate.add(i)
                            toprateAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_rate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        top_rate.layoutManager = LinearLayoutManager(context)
        top_rate.adapter = toprateAdapter
        toprateAdapter.setListenner(movieItemCLickListener)
        swipeRefresh.setOnRefreshListener{
            loadItems()
        }
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light)

    }

    var movieFresh:ArrayList<Movie.Results> = ArrayList()
    private fun loadItems() {
        toprateAdapter.clear()
        addFresh()
        onItemsLoadComplete()

    }

    private fun onItemsLoadComplete() {
        swipeRefresh.isRefreshing = false
    }

    private fun addFresh() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/top_rated?api_key=7519cb3f829ecd53bd9b7007076dbe23")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    activity!!.runOnUiThread {
                        call.cancel()
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()!!.string()
                    val datagson = Gson().fromJson(json, Movie.ResultArray::class.java)
                    activity!!.runOnUiThread {
                        for (i in datagson.results) {
                            movieFresh.add(i)
                        }
                        toprateAdapter.addAll(movieFresh)

                    }
                }
            })
    }

    private val movieItemCLickListener = object : ItemClickListenner {

        override fun onItemCLicked(position: Int) {
            var category = Category(categoryId = 1, categoryName = "MacBook")
            var item = Item(
                imageId = 2,
                price = 30.0,
                title = "MacBook Pro",
                category = category
            )
            val intent = Intent(activity, DetailMovieActivity::class.java)
            intent.putExtra(MOVIE_KEY, movie_rate[position])
            intent.putExtra(CONSTANT_KEY, item)
            startActivity(intent)
        }
    }
}

