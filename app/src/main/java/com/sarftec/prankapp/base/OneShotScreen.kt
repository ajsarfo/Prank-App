package com.sarftec.prankapp.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.sarftec.prankapp.BaseActivity
import com.sarftec.prankapp.R
import com.sarftec.prankapp.advertisement.AdCountManager
import com.sarftec.prankapp.advertisement.BannerManager
import com.sarftec.prankapp.advertisement.InterstitialManager

abstract class OneShotScreen : BaseActivity() {

    protected abstract fun getLayout(): ViewGroup

    protected abstract fun getBannerLayout() : LinearLayout

    protected abstract fun getOneShotViews() : List<View>

    protected abstract fun handleEvent(event: Screen.ScreenEvent)

    protected open fun showInterstitial() : Boolean = false

    protected open val viewModel by viewModels<OneShotViewModel>()

    protected var interstitialManager: InterstitialManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        viewModel.setContext(this)
        val adRequest = AdRequest.Builder().build()
        /** Load Admob Banner **/
        MobileAds.initialize(this) {}
        BannerManager(this, adRequest).attachBannerAd(
            getString(R.string.screen_one_shot_banner),
            getBannerLayout()
        )
        /** Load Interstitial If Necessary **/
        if(showInterstitial()) {
            interstitialManager = InterstitialManager(
                this,
                AdCountManager(listOf(1, 3)),
                adRequest
            )
            interstitialManager?.load(getString(R.string.interstitial_id))
        }
        getOneShotViews().forEach {
            it.setOnClickListener { viewModel.onClicked() }
        }
        viewModel.alive.liveData().observe(this) { event ->
            event.onHandle {
                if(it is Screen.ScreenEvent.NetworkError) {
                        Toast.makeText(
                            this,
                            getString(com.sarftec.prankapp.R.string.network_error),
                            Toast.LENGTH_SHORT
                        ).show()

                }
                else handleEvent(it)
            }
        }
    }
}