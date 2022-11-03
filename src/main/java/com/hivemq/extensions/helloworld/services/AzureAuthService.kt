package com.hivemq.extensions.helloworld.services

import com.hivemq.extensions.helloworld.models.AzureAuthResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface AzureAuthService {
    @POST("/{tenantId}/oauth2/v2.0/token")
    @FormUrlEncoded
    fun authenticate(
        @Path("tenantId") tenantId: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String,
    ): Call<AzureAuthResponse>
}