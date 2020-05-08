package com.example.androidcanvasdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidcanvasdemo.BadgeView.BadgeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var badge = BadgeView.build(this).bind(button)
        BadgeView.build(this).bind(layoutFrame)
    }
}
