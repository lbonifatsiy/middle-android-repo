package com.example.androidpracticumcustomview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.androidpracticumcustomview.ui.theme.CustomContainer

class XmlActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum()
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this)
        val containerLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        customContainer.layoutParams = containerLayoutParams

        setContentView(customContainer)
        customContainer.setOnClickListener {
            finish()
        }

        val firstView = getOkTextView()
        val secondView = getOkTextView()

        customContainer.addView(firstView)

        Handler(Looper.getMainLooper()).postDelayed({
            customContainer.addView(secondView)
        }, 2000)
    }

    private fun getOkTextView(): TextView {
        return TextView(this).apply {
            text = getString(android.R.string.ok)
        }
    }
}