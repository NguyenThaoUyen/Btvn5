package com.example.btvn5

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.btvn5.Model.CONSTANT_KEY
import com.example.btvn5.Model.Item
import com.example.btvn5.Model.MOVIE_KEY
import com.example.btvn5.Model.Movie
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_detail_movie.date
import kotlinx.android.synthetic.main.activity_detail_movie.name
import kotlinx.android.synthetic.main.itemmovie.*

class DetailMovieActivity : AppCompatActivity() {
    val TAG: String =DetailMovieActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = "Movie Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getData()

    }

    private fun getData() {
        val data = intent.extras
        if (data != null) {
            val item: Item = data.getParcelable(CONSTANT_KEY) as Item
            val movie = data.getParcelable(MOVIE_KEY) as Movie.Results
            Log.e(TAG,item.toString())
            name.text = movie.title
            date.text = movie.release_date
            overview.text = movie.overview
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/${movie.backdrop_path}")
                .centerCrop()
                .into(movie_view)
            if(movie.video){
                video_play.visibility = View.VISIBLE
            }
            else{
                video_play.visibility = View.INVISIBLE
            }
            rate.rating = (movie.vote_average/2).toFloat()
        }
    }
}

