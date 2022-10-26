package com.hivemq.extensions.helloworld.models

data class AzureAuthContext (
    val tenantId: String,
    val clientId: String,
    val clientSecret: String,
    val scope: String,
    val grantType: String,
    val hostname: String
)