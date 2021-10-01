package com.sarftec.prankapp.screen

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.sarftec.prankapp.R
import com.sarftec.prankapp.base.OneShotScreen
import com.sarftec.prankapp.base.OneShotViewModel
import com.sarftec.prankapp.base.Screen
import com.sarftec.prankapp.databinding.ScreenSocialBinding

class SocialScreen : OneShotScreen() {

    private val layoutBinding by lazy {
        ScreenSocialBinding.inflate(layoutInflater)
    }

    override val viewModel: OneShotViewModel by viewModels<SocialViewModel>()

    override fun getLayout(): ViewGroup = layoutBinding.root

    override fun getBannerLayout(): LinearLayout {
        return layoutBinding.bannerParent
    }

    override fun getOneShotViews(): List<View> {
        return layoutBinding.run {
            listOf(button, button2, button3, button4)
        }
    }

    override fun handleEvent(event: Screen.ScreenEvent) {
        when(event) {
            is Screen.ScreenEvent.Loading -> {
                //Do nothing
            }
            else -> interstitialManager?.show {
                navigateTo(
                    klass = BundleScreen::class.java,
                    slideIn = R.anim.no_anim,
                    slideOut = R.anim.no_anim
                )
            } ?: navigateTo(
                klass = BundleScreen::class.java,
                slideIn = R.anim.no_anim,
                slideOut = R.anim.no_anim
            )
        }
    }
}

class SocialViewModel : OneShotViewModel() {
    override fun shouldLoad(): Boolean = false
}