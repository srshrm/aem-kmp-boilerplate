package com.adobe.aem_kmp_boilerplate.utils

import java.awt.Desktop
import java.net.URI

actual fun openUrl(url: String) {
    try {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI(url))
        }
    } catch (e: Exception) {
        // Log or handle error
        e.printStackTrace()
    }
}

