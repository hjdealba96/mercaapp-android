package com.co.mercapp.ui.utilities

import android.app.Activity
import android.content.Intent
import androidx.core.net.toUri

/**
 * Creates an implicit intent for sharing plain text to other apps. It requests a chooser dialog.
 *
 * @param title Title for the chooser modal
 * @param contentToShare Text to share
 *
 * */

fun Activity.shareText(title: String, contentToShare: String) {
    startActivity(Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, contentToShare)
        type = "text/plain"
    }, title))
}

/**
 * Creates an implicit intent for open a web link into a web browser app. It requests a chooser dialog (if there's
 * more than one app that can handle the link)
 *
 * @param title Title for the chooser modal
 * @param url Link to open
 *
 * */

fun Activity.openWebPage(title: String, url: String) {
    startActivity(Intent.createChooser(Intent().apply {
        action = Intent.ACTION_VIEW
        data = url.toUri()
    }, title))
}