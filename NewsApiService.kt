package com.example.newssapplicationapp

import NewsData
import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date

object NewsApiService {
    private const val API_URL = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-apifeed/staticResponse.json"

    fun fetchNews(onResult: (List<NewsData>) -> Unit) {
        val url = URL(API_URL)
        Thread {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val response = bufferedReader.use { it.readText() }

            val jsonArray = JSONArray(response)
            val newsList = mutableListOf<NewsData>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val title = jsonObject.getString("title")
                val url = jsonObject.getString("url")

                newsList.add(NewsData(title, url, Date()))
            }

            Handler(Looper.getMainLooper()).post {
                onResult(newsList)
            }
        }.start()
    }
}
