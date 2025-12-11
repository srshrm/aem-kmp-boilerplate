package com.adobe.aem_kmp_boilerplate.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun createPlatformHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }
    }
}

