package com.example.newsapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavNewsCollection : AppCompatActivity() {
    private lateinit var adapter: FavNewsAdapter
    private lateinit var articles: MutableList<Article>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_news_collection)
        setSupportActionBar(findViewById(R.id.myToolBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favnewsList = findViewById<RecyclerView>(R.id.favnewsList)
        favnewsList.layoutManager = LinearLayoutManager(this@FavNewsCollection)

        articles = getFavoriteNewsFromSharedPreferences()
        adapter = FavNewsAdapter(this@FavNewsCollection, articles)
        favnewsList.adapter = adapter

        retrieveNewsFromAPI()
    }

    private fun getFavoriteNewsFromSharedPreferences(): MutableList<Article> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@FavNewsCollection)
        val favoriteNewsSet = sharedPreferences.getStringSet("FAV_NEWS", mutableSetOf())
        val articles = mutableListOf<Article>()

        favoriteNewsSet?.forEach { favoriteNewsItem ->
            val newsArray = favoriteNewsItem.split("|")
            val title = newsArray[0]
            val description = newsArray[1]
            val urlToImage = newsArray[2]
            val article = Article("", title, description, "", urlToImage)
            articles.add(article)
        }

        return articles
    }

    private fun retrieveNewsFromAPI() {
        val progressDialog = ProgressDialog(this@FavNewsCollection)
        progressDialog.setTitle("News is being fetched. Please wait for a moment")
        progressDialog.setMessage("Fetching news, please wait")
        progressDialog.show()

        val news: Call<News> = NewsService.newsInstance.getHeadlines("in", 1)
        news.enqueue(object : Callback<News> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    Log.d("NewsApp", news.toString())
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                }
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("NewsApp", "Error in fetching news", t)
                progressDialog.dismiss()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.favcollectionmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_all -> {
                Toast.makeText(
                    this@FavNewsCollection,
                    "Favorite News cleared from the collection",
                    Toast.LENGTH_SHORT
                ).show()
                clearCardViewItems()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearCardViewItems() {
        articles.clear()
        adapter.notifyDataSetChanged()
    }
}