package com.sarftec.prankapp.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sarftec.prankapp.databinding.DialogResultBinding

class ResultDialog(
    context: Context,
    parent: ViewGroup
) : AlertDialog(context) {

    init {
        val layoutBinding = DialogResultBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        setView(layoutBinding.root)
    }
}