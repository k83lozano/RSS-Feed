package com.example.rssapp

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates


class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageUrl: String = ""

    override fun toString(): String {
        return """"
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageUrl = $imageUrl
            """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.reciclerbiu)

        Log.d(TAG, "onCreate")

        val downloadData = DownloadData(this, recyclerView)
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate DONE")
    }

    companion object {

        private class DownloadData(context: Context, recyclerView: RecyclerView) :
            AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var localContext: Context by Delegates.notNull()
            var localRecyclerView: RecyclerView by Delegates.notNull()

            init {
                localContext = context
                localRecyclerView = recyclerView
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground")
                val resFeed = downloadXML(url[0])
                if(resFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: failed")
                }
                Log.d(TAG, resFeed)
                return resFeed
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute")
                super.onPostExecute(result)
                val parsedApplication = ParseApp()
                parsedApplication.parse(result)


                val adapter: ApplicationAdapter = ApplicationAdapter(localContext,parsedApplication.application)
                localRecyclerView.adapter =adapter
                localRecyclerView.layoutManager = LinearLayoutManager(localContext)

            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()
            }


        }
    }
}