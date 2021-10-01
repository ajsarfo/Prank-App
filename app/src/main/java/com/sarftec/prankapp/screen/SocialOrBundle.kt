package com.sarftec.prankapp.screen

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.sarftec.prankapp.utils.Alive
import com.sarftec.prankapp.BaseActivity
import com.sarftec.prankapp.R
import com.sarftec.prankapp.advertisement.AdCountManager
import com.sarftec.prankapp.advertisement.BannerManager
import com.sarftec.prankapp.advertisement.InterstitialManager
import com.sarftec.prankapp.utils.Event
import com.sarftec.prankapp.databinding.ScreenSocialOrBundleBinding
import com.sarftec.prankapp.manager.NetworkManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SocialOrBundle : BaseActivity() {

    private val viewModel by viewModels<SocialOrBundleViewModel>()

    private val layoutBinding by lazy {
        ScreenSocialOrBundleBinding.inflate(layoutInflater)
    }

    var interstitialManager: InterstitialManager? = null

    private val showInterstitial = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutBinding.root)
        viewModel.setContext(this)
        setupAdvertisement()
        layoutBinding.apply {
            socialButton.setOnClickListener { viewModel.onSocialMedia() }
            generalButton.setOnClickListener { viewModel.onGeneralBundle() }
        }
        viewModel.alive.liveData().observe(this) { event ->
            event.onHandle { observeLiveData(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        layoutBinding.progressBar3.visibility = View.GONE
    }

    private fun setupAdvertisement() {
        val adRequest = AdRequest.Builder().build()
        /** Load Admob Banner **/
        MobileAds.initialize(this) {}
        BannerManager(this, adRequest).attachBannerAd(
            getString(R.string.screen_special_banner),
            layoutBinding.bannerParent
        )
        /** Load Interstitial If Necessary **/
        if(showInterstitial) {
            interstitialManager = InterstitialManager(
                this,
                AdCountManager(listOf(1, 3)),
                adRequest
            )
            interstitialManager?.load(getString(R.string.interstitial_id))
        }
    }

    private fun observeLiveData(response: Response) {
        when(response) {
            is Response.Forward -> interstitialManager?.show { navigateTo(BundleScreen::class.java) }
                ?: navigateTo(BundleScreen::class.java)
            is Response.ToSocialMedia -> navigateTo(SocialScreen::class.java)
            is Response.Loading -> {
                layoutBinding.progressBar3.visibility = View.VISIBLE
            }
            is Response.NetworkError -> {
                Toast.makeText(
                    this,
                    getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

sealed class Response {
    object ToSocialMedia : Response()
    object Forward : Response()
    object Loading: Response()
    object NetworkError : Response()
}

class SocialOrBundleViewModel : ViewModel() {

    val alive = Alive<Event<Response>>()

    private var networkManager: NetworkManager? = null

    private suspend fun navigate() {
        alive.publish(Event.OneShot(Response.Loading))
        delay(1000)
        alive.publish(Event.OneShot(Response.Forward))
    }

    fun setContext(context: Context) {
        networkManager = NetworkManager(context)
    }

    fun onSocialMedia() {
        viewModelScope.launch {
            alive.publish(Event.OneShot(Response.Loading))
            delay(1000)
            alive.publish(Event.OneShot(Response.ToSocialMedia))
        }
    }

    fun onGeneralBundle() {
        viewModelScope.launch {
            networkManager?.let {
                if (!it.isNetworkAvailable()) alive.publish(
                    Event.OneShot(Response.NetworkError)
                )
                else navigate()
            } ?: navigate()
        }
    }
}