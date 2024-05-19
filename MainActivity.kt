package com.example.newssapplicationapp

import NewsData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private var newsList = listOf<NewsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnSortOldToNew).setOnClickListener { sortNews(false) }
        findViewById<Button>(R.id.btnSortNewToOld).setOnClickListener { sortNews(true) }

        NewsApiService.fetchNews { fetchedNewsList ->
            newsList = fetchedNewsList
            newsAdapter = NewsAdapter(newsList) { url ->
                val intent = Intent(this, NewsDetailActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            }
            recyclerView.adapter = newsAdapter
        }
    }

    private fun sortNews(isNewToOld: Boolean) {
        val sortedList = if (isNewToOld) {

            newsList.sortedByDescending { it.date }  // Assuming NewsData has a date field
        } else {
            newsList.sortedBy { it.date }
        }
        newsAdapter = NewsAdapter(sortedList) { url ->
            val intent = Intent(this, NewsDetailActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }
        recyclerView.adapter = newsAdapter
    }
}



