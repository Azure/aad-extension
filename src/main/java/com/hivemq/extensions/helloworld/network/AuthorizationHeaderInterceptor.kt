package com.hivemq.extensions.helloworld.network

import com.hivemq.extensions.helloworld.models.AzureAuthContext
import okhttp3.Interceptor
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AuthorizationHeaderInterceptor private constructor(): Interceptor {
    private val azureAuthenticator: AzureAuthenticator by lazy {
        AzureAuthenticator(
            AzureAuthContext(
                hostname = "https://login.windows-ppe.net/",
                tenantId = "bf66f47a-6971-4c90-a7a9-99be82dea167",
                clientId = "6ea23442-5896-44ff-b773-49405922bfc8",
                clientSecret = System.getenv("GRAPH_CLIENT_SECRET"),
                scope = "https://graph.microsoft-ppe.com/.default",
                grantType = "client_credentials"
            )
        )
    }
    private val log: Logger = LoggerFactory.getLogger(
        AuthorizationHeaderInterceptor::class.java
    )

    private var authToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        synchronized(this) {
            if (this.authToken == null) {
                this.authToken = this.azureAuthenticator.authenticate().accessToken;
            }
        }

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${this.authToken}")
            .build()
        val response = chain.proceed(request)
        log.info("ResponseCode: " + response.code())
        if (response.code() == 401) {
            synchronized(this) {
                if (this.authToken == null) {
                    this.authToken = this.azureAuthenticator.authenticate().accessToken;
                }
            }
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${this.authToken}")
                .build()
            log.info("Retry ResponseCode: " + response.code())
            return chain.proceed(request)
        }
        return response
    }
    companion object {
        val INSTANCE: Interceptor by lazy() { AuthorizationHeaderInterceptor() }
    }

}