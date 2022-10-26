package com.hivemq.extensions.helloworld.models

import com.google.gson.annotations.SerializedName

data class AzureAuthResponse(
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("access_token")
    val accessToken: String
)