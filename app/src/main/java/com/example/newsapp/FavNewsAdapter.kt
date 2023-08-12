package com.example.newsapp
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavNewsAdapter(val context: Context, val articles: List<Article>) :
    RecyclerView.Adapter<FavNewsAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
//
//        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val favoriteNewsSet = sharedPreferences.getStringSet("FAV_NEWS", mutableSetOf())
//        favoriteNewsSet?.forEach { favoriteNewsItem ->
//            val newsArray = favoriteNewsItem.split("|")
//            val title = newsArray[0]
//            val description = newsArray[1]
//            val urlToImage = newsArray[2]
//            val article = Article("", title, description, "", urlToImage)
//            article.addAll(articles)
//        }

        holder.newsTitle.text = article.title
        holder.newsDescription.text = article.description
        Glide.with(context).load(article.urlToImage).into(holder.newsImage)


    }
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsImage = itemView.findViewById<ImageView>(R.id.newsImage)
        var newsTitle = itemView.findViewById<TextView>(R.id.newsTitle)
        var newsDescription = itemView.findViewById<TextView>(R.id.newsDescription)
//        var favNews = itemView.findViewById<ImageView>(R.id.fav_news)
    }

}