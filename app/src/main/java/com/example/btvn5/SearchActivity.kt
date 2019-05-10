package com.example.btvn5


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.btvn5.Adapter.ItemClickListenner
import com.example.btvn5.Adapter.MovieAdapter
import com.example.btvn5.Model.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.*
import java.io.IOException

class SearchActivity : AppCompatActivity() {

    lateinit var keyword: String
    var movieSearch:ArrayList<Movie.Results> = ArrayList()
    lateinit var searchAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""
        keyword = intent.getStringExtra("keyword")
        searchResult.text =keyword
        addMovie()
        searchMovie.layoutManager = LinearLayoutManager(this)
        searchAdapter = MovieAdapter(movieSearch,this@SearchActivity)
        searchMovie.adapter = searchAdapter

        searchAdapter.setListenner(movieItemClickListener)

    }
    private fun addMovie() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/search/movie?api_key=7519cb3f829ecd53bd9b7007076dbe23&query=$keyword")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        call.cancel()
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()!!.string()
                    val datagson = Gson().fromJson(json, Movie.ResultArray::class.java)
                    runOnUiThread {
                        movieSearch.clear()
                        for (i in datagson.results) {
                            movieSearch.add(i)
                            searchAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })


    }
    private val movieItemClickListener = object : ItemClickListenner {

        override fun onItemCLicked(position: Int) {
            val category = Category(categoryId = 1, categoryName = "MacBook")
            val item = Item(
                imageId = 2,
                price = 30.0,
                title = "MacBook Pro",
                category = category
            )
            val intent = Intent(this@SearchActivity, DetailMovieActivity::class.java)
            intent.putExtra(MOVIE_KEY, movieSearch[position])
            intent.putExtra(CONSTANT_KEY, item)
            startActivity(intent)
        }
    }
}
