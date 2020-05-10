package com.lulixue.android

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.lulixue.android.BadgeView.BadgeView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mBadges = ArrayList<BadgeView>()
    private var mSizeMin = 0
    private var mOffsetXY = 0
    private val tag = MainActivity::class.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animation.isChecked = true
        editText2.clearFocus()
        mBadges.add(BadgeView.build(this).bind(button))
        mBadges.add(BadgeView.build(this).bind(mButton))
        mBadges.add(BadgeView.build(this).bind(mTxtButton))
        mBadges.add(BadgeView.build(this).bind(layoutFrame))
        button3.setOnClickListener {
            val txt = editText2.text.toString()

            Log.d(tag, "thread: " + Thread.currentThread().toString())
            for (badge in mBadges) {
                badge.setBadgeText(txt)
            }
        }

        btnEndTop.performClick()
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

        switchPaddingType.setOnClickListener {
            setTextPadding(seekBarTxtPadding.progress)
        }

        seekBarTxtPadding.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setTextPadding(progress)
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
                    b.offsetX = offset
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
                        b.offsetY = offset
                    }
                    offsetY.text = "" + offset + "px"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSizeMin = 0
            mOffsetXY = 0
            seekBarTxtSize.min = 5
            seekBarOffsetX.min = -50
            seekBarOffsetY.min = -50
        } else {
            seekBarTxtSize.max = 45
            seekBarOffsetX.max = 100
            seekBarOffsetY.max = 100
            mOffsetXY = 50
            mSizeMin = 5
        }
        seekBarTxtSize.progress = BadgeView.DEFAULT_TEXT_SIZE_SP - mSizeMin
        seekBarOffsetX.progress = BadgeView.DEFAULT_OFFSET_X_PX + mOffsetXY
        seekBarOffsetY.progress = BadgeView.DEFAULT_OFFSET_Y_PX + mOffsetXY
        seekBarTxtPadding.progress = BadgeView.DEFAULT_TEXT_PADDING

    }

    private fun setTextPadding(progress: Int) {
        val dp = switchPaddingType.isChecked
        for (b in mBadges) {
            b.setTextPadding(progress, dp)
        }
        val unit = when (dp) {
            true -> "dp"
            false -> "px"
        }
        txtPadding.text = "" + progress + unit
    }

    fun getTxtSize(progress: Int) : Int {
        return progress + mSizeMin
    }

    fun getOffset(progress: Int) : Int {
        return progress - mOffsetXY
    }
    fun getOffsetOtherwise(progress: Int) : Int {
        return progress + mOffsetXY
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

    fun onClickVisible(view: View) {

        for (badge in mBadges) {
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.interpolator =  (AccelerateDecelerateInterpolator()); //and this
            anim.duration = (500);
            badge.startAnimation(anim)
            badge.visibility = View.VISIBLE
        }
    }
    fun onClickInvisible(view: View) {

        for (badge in mBadges) {

            val anim = AlphaAnimation(1.0f, 0.0f)
            anim.interpolator =  (AccelerateDecelerateInterpolator()); //and this
            anim.duration = (500);
            badge.startAnimation(anim)
            badge.visibility = View.INVISIBLE
        }
    }

    fun onFadeAnimation(view: View) {
        for (badge in mBadges) {
            badge.setFadeAnimation(animation.isChecked)
        }
    }

    fun onAutoFit(view: View) {
        for (badge in mBadges) {
            badge.setAutoFit(autoFit.isChecked)
        }
    }

    fun onReset(view: View) {
        animation.isChecked = true
        autoFit.isChecked = false
        switchPaddingType.isChecked = false

        seekBarTxtSize.progress = BadgeView.DEFAULT_TEXT_SIZE_SP - mSizeMin
        seekBarOffsetX.progress = BadgeView.DEFAULT_OFFSET_X_PX + mOffsetXY
        seekBarOffsetY.progress = BadgeView.DEFAULT_OFFSET_Y_PX + mOffsetXY
        seekBarTxtPadding.progress = BadgeView.DEFAULT_TEXT_PADDING
        for (badge in mBadges) {
            badge.badgeText = BadgeView.DEFAULT_TEXT
            badge.setFadeAnimation(true)
            badge.offsetX = BadgeView.DEFAULT_OFFSET_X_PX + mOffsetXY
            badge.offsetY = BadgeView.DEFAULT_OFFSET_Y_PX + mOffsetXY
            badge.setTextSizeSp(seekBarTxtSize.progress.toFloat())
            badge.setTextPadding(seekBarTxtPadding.progress, false)
        }
    }

    fun onLeft(view: View) {
        var offset = 0
        for (badge in mBadges) {
            badge.addOffsetX(-1)
            offset = badge.offsetX
        }
        seekBarOffsetX.progress = getOffsetOtherwise(offset)
    }
    fun onRight(view: View) {
        var offset = 0
        for (badge in mBadges) {
            badge.addOffsetX(1)
            offset = badge.offsetX
        }
        seekBarOffsetX.progress = getOffsetOtherwise(offset)
    }
    fun onTop(view: View) {
        var offset = 0
        for (badge in mBadges) {
            badge.addOffsetY(1)
            offset = badge.offsetY
        }
        seekBarOffsetY.progress = getOffsetOtherwise(offset)
    }
    fun onBottom(view: View) {
        var offset = 0
        for (badge in mBadges) {
            badge.addOffsetY(-1)
            offset = badge.offsetY
        }
        seekBarOffsetY.progress = getOffsetOtherwise(offset)
    }

    fun onNumberAdd(view: View) {
        for (badge in mBadges) {
            badge.badgeNumber = ++badge.badgeNumber;
        }
    }
    fun onNumberMinus(view: View) {
        for (badge in mBadges) {
            badge.badgeNumber = --badge.badgeNumber;
        }

    }
}
