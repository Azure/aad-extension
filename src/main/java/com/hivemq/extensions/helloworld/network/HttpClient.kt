package com.hivemq.extensions.helloworld.network

import okhttp3.OkHttpClient

class HttpClient {
    companion object {
        val WithAuthorizationHeader: OkHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpClient.Builder()
                .addNetworkInterceptor(AuthorizationHeaderInterceptor.INSTANCE)
                .build();
        }

        val WIthResponseInterceptor: OkHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpClient.Builder()
                .addNetworkInterceptor(ResponseInterceptor.INSTANCE)
                .build();
        }
    }
}