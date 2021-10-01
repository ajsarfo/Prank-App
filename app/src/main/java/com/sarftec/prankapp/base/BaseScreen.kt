package com.sarftec.prankapp.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.sarftec.prankapp.*
import com.sarftec.prankapp.advertisement.AdCountManager
import com.sarftec.prankapp.advertisement.BannerManager
import com.sarftec.prankapp.advertisement.InterstitialManager
import com.sarftec.prankapp.databinding.ScreenMainBinding
import com.sarftec.prankapp.dialog.ProcessingDialog
import com.sarftec.prankapp.dialog.ResultDialog
import com.sarftec.prankapp.screen.BundleScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseScreen : BaseActivity() {

    protected val layoutBinding by lazy {
        ScreenMainBinding.inflate(layoutInflater)
    }

    protected abstract val viewModel: BaseViewModel

    protected var interstitialManager: InterstitialManager? = null

    protected open fun showInterstitial(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutBinding.root)
        val adRequest = AdRequest.Builder().build()
        /** Load Admob Banner **/
        MobileAds.initialize(this) {}
        BannerManager(this, adRequest).attachBannerAd(
            getString(R.string.screen_main_banner),
            layoutBinding.bannerParent
        )
        /** Load Interstitial If Necessary **/
        if (showInterstitial()) {
            interstitialManager = InterstitialManager(
                this,
                AdCountManager(listOf(1, 3)),
                adRequest
            )
            interstitialManager?.load(getString(R.string.interstitial_id))
        }
        setupToolbar()
        setupListeners()
        viewModel.setContext(this)
        viewModel.alive.liveData().observe(this) { event ->
            event.onHandle { handleScreenEvent(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            lifecycleScope.launchWhenCreated {
                if (computeSecondsLeft(this@BaseScreen) <= 0L)
                    editSettings(IS_TIME_COMPLETE, true)
            }
        }
        layoutBinding.progressBar.visibility = View.GONE
    }

    protected abstract fun navigate(text: String)

    private fun setupToolbar() {
        layoutBinding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.reward) {
                lifecycleScope.launch {
                    if (readSettings(IS_TIME_STARTED, false).first()) {
                        if (readSettings(IS_TIME_COMPLETE, false).first()) {
                            ResultDialog(this@BaseScreen, layoutBinding.root).show()
                        } else {
                            ProcessingDialog(
                                this@BaseScreen,
                                lifecycleScope,
                                layoutBinding.root
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@BaseScreen,
                            "Please fill all required INFORMATION!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            } else false
        }
    }

    private fun setupListeners() {
        layoutBinding.okButton.setOnClickListener {
            viewModel.onClicked()
        }
        layoutBinding.textField.doOnTextChanged { text, _, _, _ ->
            text?.let { viewModel.setText(it.toString()) }
        }
    }

    private fun handleScreenEvent(event: Screen.ScreenEvent) {
        when (event) {
            is Screen.ScreenEvent.Text -> {
                layoutBinding.headerText.text = event.header
                layoutBinding.okButton.text = event.button
                layoutBinding.textField.hint = event.hint
            }
            is Screen.ScreenEvent.Navigate -> interstitialManager?.show { navigate("Nothing!!") }
                ?: navigate("Nothing!!")
            is Screen.ScreenEvent.Error -> {
                layoutBinding.apply {
                    progressBar.visibility = View.GONE
                    layoutBinding.errorLayout.visibility = View.VISIBLE
                }
            }
            is Screen.ScreenEvent.Loading -> {
                layoutBinding.apply {
                    progressBar.visibility = View.VISIBLE
                    layoutBinding.errorLayout.visibility = View.GONE
                }
            }
            is Screen.ScreenEvent.NetworkError -> {
                Toast.makeText(
                    this,
                    getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                layoutBinding.apply {
                    progressBar.visibility = View.GONE
                    layoutBinding.errorLayout.visibility = View.GONE
                }
            }
        }
    }
}