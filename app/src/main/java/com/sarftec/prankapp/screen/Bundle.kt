package com.sarftec.prankapp.screen

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.sarftec.prankapp.R
import com.sarftec.prankapp.base.OneShotScreen
import com.sarftec.prankapp.base.Screen
import com.sarftec.prankapp.databinding.ScreenBundleBinding

class BundleScreen : OneShotScreen() {

    private val layoutBinding by lazy {
        ScreenBundleBinding.inflate(layoutInflater)
    }

    override fun getLayout(): ViewGroup = layoutBinding.root

    override fun getBannerLayout(): LinearLayout {
        return layoutBinding.bannerParent
    }

    override fun getOneShotViews(): List<View> {
        return layoutBinding.run {
            listOf(button7, button6, button5, button8)
        }
    }

    override fun onResume() {
        super.onResume()
        layoutBinding.progressBar2.visibility = View.GONE
    }

    override fun handleEvent(event: Screen.ScreenEvent) {
        when(event) {
            is Screen.ScreenEvent.Loading -> {
               layoutBinding.progressBar2.visibility = View.VISIBLE
            }
            else -> interstitialManager?.show {
                navigateTo(
                    klass = FinishScreen::class.java,
                    slideIn = R.anim.no_anim,
                    slideOut = R.anim.no_anim
                )
            } ?: navigateTo(
                klass = FinishScreen::class.java,
                slideIn = R.anim.no_anim,
                slideOut = R.anim.no_anim
            )
        }
    }
}