package com.adobe.aem_kmp_boilerplate.utils

import android.content.Intent
import android.net.Uri
import com.adobe.aem_kmp_boilerplate.AndroidApp

actual fun openUrl(url: String) {
    val context = AndroidApp.applicationContext
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Log or handle error
        e.printStackTrace()
    }
}

