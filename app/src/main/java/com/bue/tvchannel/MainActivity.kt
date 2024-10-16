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

class MainActivity : AppCompatActivity() {

    // Local JSON-like data (a map in Kotlin)
    private val channelMap: Map<String, String> = mapOf(
        "Channel 24" to "https://www.youtube.com/channel/UCHLqIOMPk20w-6cFgkA90jw/live",
        "Jamuna TV" to "https://www.youtube.com/channel/UCN6sm8iHiPd0cnoUardDAnw/live",
        "Independent TV" to "https://www.youtube.com/channel/UCATUkaOHwO9EP_W87zCiPbA/live",
        "Somoy TV" to "https://www.youtube.com/channel/UCxHoBXkY88Tb8z1Ssj6CWsQ/live",
        "News 24" to "https://www.youtube.com/channel/UCPREnbhKQP-hsVfsfKP-mCw/live",
        "Channel i" to "https://www.youtube.com/channel/UC8NcXMG3A3f2aFQyGTpSNww/live",
        "ATN News" to "https://www.youtube.com/channel/UCt8llfhkf9LRzjTEnbG2qnQ/live",
        "RTV" to "https://www.youtube.com/channel/UC2P5Fd5g41Gtdqf0Uzh8Qaw/live",
        "DBC News" to "https://www.youtube.com/channel/UCUvXoiDEKI8VZJrr58g4VAw/live",
        "NTV" to "https://www.youtube.com/channel/UC0V3IJCnr6ZNjB9t_GLhFFA/live",
        "Ekhon TV" to "https://www.youtube.com/channel/UCWVqdPTigfQ-cSNwG7O9MeA/live"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Directly create dynamic TextViews with the local data
        createDynamicChannelViews()
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
                    `package` = "com.teamsmart.videomanager.tv" // Use YouTube app package name
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
