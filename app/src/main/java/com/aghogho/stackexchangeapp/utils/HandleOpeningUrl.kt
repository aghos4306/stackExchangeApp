package com.aghogho.stackexchangeapp.utils

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.net.Uri

fun HandleOpeningUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}