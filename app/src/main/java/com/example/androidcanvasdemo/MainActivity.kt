package com.example.androidcanvasdemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.androidcanvasdemo.BadgeView.BadgeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mBadges = ArrayList<BadgeView>()
    private var mSizeMin = 0
    private var mOffsetXY = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val badge = BadgeView.build(this).bind(button)
        val badge2 = BadgeView.build(this).bind(layoutFrame)

        mBadges.add(badge)
        mBadges.add(badge2)
        button3.setOnClickListener {
            var txt = editText2.text.toString()

            for (badge in mBadges) {
                badge.setBadgeText(txt)
            }
        }

        seekBarTxtSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val size = getTxtSize(progress)
                for (b in mBadges) {
                    b.setTextSizeSp(size.toFloat())
                }
                txtSize.text = "" + size + "sp"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        seekBarOffsetX.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val offset = getOffset(progress)
                for (b in mBadges) {
                    b.setOffsetX(offset)
                }
                offsetX.text = "" + offset + "px"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekBarOffsetY.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val offset = getOffset(progress)
                    for (b in mBadges) {
                        b.setOffsetY(offset)
                    }
                    offsetY.text = "" + offset + "px"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        var offset = 0
        var min = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            min = 0
            seekBarTxtSize.min = 5
            seekBarOffsetX.min = -50
            seekBarOffsetY.min = -50
        } else {
            offset = 50
            min = 5
        }
        seekBarTxtSize.progress = 13 - min
        seekBarOffsetX.progress = BadgeView.DEFAULT_OFFSET_X_Y + offset
        seekBarOffsetY.progress = BadgeView.DEFAULT_OFFSET_X_Y + offset

        mSizeMin = min
        mOffsetXY = offset
    }

    fun getTxtSize(progress: Int) : Int {
        return progress + mSizeMin
    }

    fun getOffset(progress: Int) : Int {
        return progress - mOffsetXY
    }

    fun onClickST(view: View) {
        for (badge in mBadges) {
            badge.setGravity(BadgeView.BadgeGravity.StartTop)
        }
    }
    fun onClickSB(view: View) {
        for (badge in mBadges) {
            badge.setGravity(BadgeView.BadgeGravity.StartBottom)
        }
    }
    fun onClickET(view: View) {
        for (badge in mBadges) {
            badge.setGravity(BadgeView.BadgeGravity.EndTop)
        }
    }
    fun onClickEB(view: View) {
        for (badge in mBadges) {
            badge.setGravity(BadgeView.BadgeGravity.EndBottom)
        }

    }
}
