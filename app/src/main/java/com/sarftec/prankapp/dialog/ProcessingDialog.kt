package com.sarftec.prankapp.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sarftec.prankapp.databinding.DialogProcessingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class ProcessingDialog(
    context: Context,
    coroutineScope: CoroutineScope,
    parent: ViewGroup
) : AlertDialog(context) {

    private val timer = Timer(context, coroutineScope)

    init {
        val layoutBinding = DialogProcessingBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        setView(layoutBinding.root)
        coroutineScope.launch {
            val timeChannel = timer.start()
            while (true) {
                setTime(layoutBinding,timeChannel.receive())
            }
        }
    }

    private fun setTime(layoutBinding: DialogProcessingBinding, seconds: Long) {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        layoutBinding.estimatedTime.text =
            String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
    }

    override fun cancel() {
        timer.cancel()
        super.cancel()
    }
}