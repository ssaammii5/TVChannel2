package com.bue.tvchannel

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Map the TextView IDs with the corresponding YouTube links
        val channelMap = mapOf(
            R.id.channel24 to "https://www.youtube.com/live/V4zZa7Z_0Fs?si=mw5K1Qblp34kuXw6",
            R.id.jamunatv to "https://www.youtube.com/live/F-gv4XykDwA?si=hQrMS_qYWHnydiRj",
            R.id.independenttv to "https://www.youtube.com/live/1YhmjNQlnP8?si=p8ngNT7qshssCE4B",
            R.id.somoytv to "https://www.youtube.com/live/lKNjRobSmZE?si=L0wQJkLkq67q80ye",
            R.id.news24 to "https://www.youtube.com/live/CQWfy3QWYhQ?si=MfWBbAJKOpCZIbZc",
            R.id.channeli to "https://www.youtube.com/live/NMoPgBD-NLM?si=Sg-DJ9JMdmVXs5ye",
            R.id.atnnews to "https://www.youtube.com/live/j_4QRtisF2Q?si=IobyDnNbkt4NhhIm",
            R.id.rtv to "https://www.youtube.com/live/hZkcxTcjhvg?si=cUCCIiQ_nnoTpS5z",
            R.id.dbcnews to "https://www.youtube.com/live/0tz39MJclv0?si=uAOzfqpTYTke5yFT",
            R.id.ntv to "https://www.youtube.com/live/5i1xU01UtYc?si=UPmNVci403cwcYNf"
        )

        // Set the onClickListeners for each channel TextView
        channelMap.forEach { (textViewId, liveUrl) ->
            findViewById<TextView>(textViewId).setOnClickListener {
                val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(liveUrl)).apply {
                    `package` = "com.teamsmart.videomanager.tv"
                }
                val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(liveUrl))

                try {
                    startActivity(intentApp)
                } catch (ex: ActivityNotFoundException) {
                    startActivity(intentBrowser)
                }
            }
        }
    }
}
