package com.example.androidcanvasdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidcanvasdemo.BadgeView.BadgeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val badge = BadgeView.build(this).bind(button)
        val badge2 = BadgeView.build(this).bind(layoutFrame)

        button3.setOnClickListener {
            var txt = editText2.text.toString()
            badge.setBadgeText(txt)
            badge2.setBadgeText(txt)
        }
    }
}
