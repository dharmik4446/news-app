package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(val context: Context, val articles: List<Article>) :

    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    @SuppressLint("CommitPrefEdits", "MutatingSharedPrefs")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        holder.newsTitle.text = article.title
        holder.newsDescription.text = article.description
        Glide.with(context).load(article.urlToImage).into(holder.newsImage)

        // Read from SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val storedNews = sharedPreferences.getStringSet("FAV_NEWS", mutableSetOf())
// Set the initial state of the favorite news button
        if (storedNews?.contains(article.title) == true) {
            holder.favNews.setImageResource(R.drawable.fav_clicked)
            holder.favNews.tag = "fav_clicked"
        } else {
            holder.favNews.setImageResource(R.drawable.fav_news)
            holder.favNews.tag = "fav_news"
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(context, article.title, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("URL", article.url)
            context.startActivity(intent)
            Log.d("URL", article.url)
        }


        holder.favNews.setOnClickListener {
            if (holder.favNews.tag == "fav_news") {
                Toast.makeText(context, article.title+"Your liked news is added to the fav news list", Toast.LENGTH_SHORT).show()
                holder.favNews.setImageResource(R.drawable.fav_clicked)
                holder.favNews.tag = "fav_clicked"


// Add the news to SharedPreferences
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                editor = sharedPreferences.edit()
                val newsSet = sharedPreferences.getStringSet("FAV_NEWS", mutableSetOf())
                newsSet?.add(article.title)
                newsSet?.add(article.description)
                newsSet?.add(article.urlToImage)
                editor.putStringSet("FAV_NEWS", newsSet)
                editor.apply()
                Log.d("news", editor.toString())


            } else {
                Toast.makeText(context, article.title+"Your liked news is removed from the fav news list", Toast.LENGTH_SHORT).show()
                holder.favNews.setImageResource(R.drawable.fav_news)
                holder.favNews.tag = "fav_news"

//                val newsSet = sharedPreferences.getStringSet("FAV_NEWS", mutableSetOf())
//                newsSet?.remove(article.title)
//                newsSet?.remove(article.description)
//                newsSet?.remove(article.urlToImage)
//                editor.putStringSet("FAV_NEWS", newsSet)
//                editor.apply()
            }
        }
    }


    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsImage = itemView.findViewById<ImageView>(R.id.newsImage)
        var newsTitle = itemView.findViewById<TextView>(R.id.newsTitle)
        var newsDescription = itemView.findViewById<TextView>(R.id.newsDescription)
        var favNews = itemView.findViewById<ImageView>(R.id.fav_news)


    }

}