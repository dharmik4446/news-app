package com.example.newsapp
//https://newsapi.org/v2/top-headlines?country=in&apiKey=3ffd9211354546e7899fd6521de9f50f
//https://newsapi.org/v2/top-headlines?apiKey=3ffd9211354546e7899fd6521de9f50f&country=in&page=1

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL="https://newsapi.org/"
const val API_KEY="3ffd9211354546e7899fd6521de9f50f"
interface NewsInterface {
    @GET("v2/top-headlines?apikey=$API_KEY")
    fun getHeadlines(@Query("Country")country:String,@Query("page")page:Int): Call<News>

}

object NewsService{
    val newsInstance: NewsInterface
    init{
        val retrofit =Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsInstance=retrofit.create(NewsInterface::class.java)

    }
}
