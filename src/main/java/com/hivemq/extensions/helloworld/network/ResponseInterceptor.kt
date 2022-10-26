package com.hivemq.extensions.helloworld.network

import com.hivemq.extensions.helloworld.models.AzureAuthContext
import okhttp3.Interceptor
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ResponseInterceptor private constructor(): Interceptor {

    private val log: Logger = LoggerFactory.getLogger(
        ResponseInterceptor::class.java
    )

    private var authToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        log.info("ResponseCode: " + response.code())

        return response
    }
    companion object {
        val INSTANCE: Interceptor by lazy() { ResponseInterceptor() }
    }

}