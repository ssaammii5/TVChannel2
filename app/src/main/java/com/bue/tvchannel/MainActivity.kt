package com.bue.tvchannel

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    private val jsonUrl = "https://raw.githubusercontent.com/ssaammii5/TVChannel2/main/file.json"
    private lateinit var channelMap: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fetch the JSON data from GitHub and update the links
        fetchJsonData()
    }

    private fun fetchJsonData() {
        val client = OkHttpClient()
        val request = Request.Builder().url(jsonUrl).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to load channel links",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("MainActivity", "Failed to fetch JSON", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("MainActivity", "Unexpected response code: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to load channel links",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return
                }

                response.body?.let { responseBody ->
                    val jsonString = responseBody.string()

                    try {
                        // Parse JSON with proper type
                        val gson = Gson()
                        val type: Type = object : TypeToken<Map<String, String>>() {}.type
                        channelMap = gson.fromJson(jsonString, type)

                        // Once JSON is fetched, set the onClickListeners
                        runOnUiThread {
                            setChannelListeners()
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Failed to parse JSON", e)
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "Error parsing channel links",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }

    private fun setChannelListeners() {
        val channelIds = mapOf(
            R.id.channel24 to "channel24",
            R.id.jamunatv to "jamunatv",
            R.id.independenttv to "independenttv",
            R.id.somoytv to "somoytv",
            R.id.news24 to "news24",
            R.id.channeli to "channeli",
            R.id.atnnews to "atnnews",
            R.id.rtv to "rtv",
            R.id.dbcnews to "dbcnews",
            R.id.ntv to "ntv"
        )

        channelIds.forEach { (textViewId, channelKey) ->
            val textView = findViewById<TextView>(textViewId)
            if (textView != null) {
                val liveUrl = channelMap[channelKey]
                liveUrl?.let { url ->  // Explicitly name the parameter `url`
                    textView.setOnClickListener {
                        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                            `package` = "com.teamsmart.videomanager.tv"
                        }
                        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(url))

                        try {
                            startActivity(intentApp)
                        } catch (ex: ActivityNotFoundException) {
                            startActivity(intentBrowser)
                        }
                    }
                }
            } else {
                Log.w("MainActivity", "TextView not found for $channelKey")
            }
        }
    }
}
