package com.example.newsapp

//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.interstitial.InterstitialAd
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.interstitial.InterstitialAd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd
    var pageNum = 1
    var totalResults = -1
    lateinit var adapter: NewsAdapter
    private var articles = mutableListOf<Article>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.myToolBar))

        adapter = NewsAdapter(this@MainActivity, articles)
        val newsList = findViewById<RecyclerView>(R.id.newsList)
        newsList.adapter = adapter
        newsList.layoutManager = LinearLayoutManager(this@MainActivity)
        getNews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menubar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fav_news -> {
                Toast.makeText(this@MainActivity, "Favorite News clicked", Toast.LENGTH_SHORT) .show()
                val intent = Intent(applicationContext, FavNewsCollection::class.java)
                startActivity(intent)

                true
            }

            R.id.settings -> {
                Toast.makeText(this@MainActivity, "Back clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun getNews() {

        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setTitle("News is being fetched. Please wait for a moment")
        progressDialog.setMessage("Fetching news , please wait")
        progressDialog.show()

        val news: Call<News> = NewsService.newsInstance.getHeadlines("in", pageNum)
        news.enqueue(object : Callback<News> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    Log.d("NewsApp", news.toString())
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                    progressDialog.dismiss()
                    totalResults = news.totalResults

                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("NewsApp", "Error in fetching news", t)
            }
        })
    }
}