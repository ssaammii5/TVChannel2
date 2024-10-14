package com.bue.tvchannel

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

                        // Once JSON is fetched, create dynamic TextViews
                        runOnUiThread {
                            createDynamicChannelViews()
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

    private fun createDynamicChannelViews() {
        val channelContainer = findViewById<LinearLayout>(R.id.channelContainer)

        channelMap.forEach { (channelName, channelUrl) ->
            // Dynamically create TextView for each channel
            val textView = TextView(this).apply {
                text = channelName
                textSize = 70f
                setPadding(15, 15, 15, 15)
                setBackgroundResource(R.drawable.card_background) // Background with shadow if needed
                setTextColor(ContextCompat.getColor(context, R.color.white))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 16
                    marginStart = 16
                }
                isClickable = true
                isFocusable = true
                isFocusableInTouchMode = true // Enable focusable in touch mode
                gravity = android.view.Gravity.CENTER
            }

            // Set the click listener for each dynamically created TextView
            textView.setOnClickListener {
                val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl)).apply {
                    `package` = "com.teamsmart.videomanager.tv"
                }
                val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl))

                try {
                    startActivity(intentApp)
                } catch (ex: ActivityNotFoundException) {
                    startActivity(intentBrowser)
                }
            }

            // Add the dynamically created TextView to the container
            channelContainer.addView(textView)
        }
    }
}
