package com.sarftec.prankapp.screen

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sarftec.prankapp.*
import com.sarftec.prankapp.base.OneShotScreen
import com.sarftec.prankapp.base.OneShotViewModel
import com.sarftec.prankapp.base.Screen
import com.sarftec.prankapp.databinding.ScreenFinishBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FinishScreen : OneShotScreen() {

    private val layoutBinding by lazy {
        ScreenFinishBinding.inflate(layoutInflater)
    }

    override val viewModel: OneShotViewModel by viewModels<FinishViewModel>()

    override fun getBannerLayout(): LinearLayout {
        return layoutBinding.bannerParent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            editSettings(IS_TIME_STARTED, true)
            editSettings(STOP_TIME, getStopTime())
        }
    }

    override fun getLayout(): ViewGroup = layoutBinding.root

    override fun getOneShotViews(): List<View> {
        return layoutBinding.run {
            listOf(home)
        }
    }

    override fun handleEvent(event: Screen.ScreenEvent) {
        when (event) {
            is Screen.ScreenEvent.Loading -> {
                //Do nothing
            }
            else -> interstitialManager?.show {
                navigateTo(
                    klass = ModelScreen::class.java,
                    slideIn = R.anim.no_anim,
                    slideOut = R.anim.no_anim
                )
            } ?: navigateTo(
                klass = ModelScreen::class.java,
                slideIn = R.anim.no_anim,
                slideOut = R.anim.no_anim
            )
        }
    }
}

class FinishViewModel : OneShotViewModel() {
    override fun shouldLoad() = false
}