package com.example.equipoonce.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View

fun Context.crearDialogReto(view: View): AlertDialog =
    AlertDialog.Builder(this)
        .setView(view)
        .create()
        .also {
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
