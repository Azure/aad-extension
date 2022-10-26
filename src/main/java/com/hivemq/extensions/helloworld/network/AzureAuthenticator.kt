package com.hivemq.extensions.helloworld.network

import com.hivemq.extensions.helloworld.models.AzureAuthContext
import com.hivemq.extensions.helloworld.models.AzureAuthResponse
import com.hivemq.extensions.helloworld.services.AzureAuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.security.sasl.AuthenticationException

class AzureAuthenticator (
    private val context: AzureAuthContext
){
    private val service: AzureAuthService
    init {
        val retrofit = Retrofit.Builder().baseUrl(context.hostname)
            .addConverterFactory(GsonConverterFactory.create())
            .client(HttpClient.WIthResponseInterceptor)
            .build()

        this.service = retrofit.create(AzureAuthService::class.java)
    }

    internal fun authenticate(): AzureAuthResponse {
        var response = this.service.authenticate(
            tenantId = this.context.tenantId,
            clientId = this.context.clientId,
            clientSecret = this.context.clientSecret,
            scope = this.context.scope,
            grantType = this.context.grantType
        )
        var res = response.execute();
        System.out.println(res.errorBody()?.string())
        return res.body() ?: throw AuthenticationException("Authorization with AAD failed")
    }

}