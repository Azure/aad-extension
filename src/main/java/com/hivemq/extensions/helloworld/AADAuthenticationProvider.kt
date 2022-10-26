package com.hivemq.extensions.helloworld

import com.hivemq.extension.sdk.api.annotations.NotNull
import com.hivemq.extension.sdk.api.annotations.Nullable
import com.hivemq.extension.sdk.api.auth.Authenticator
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider
import com.hivemq.extensions.helloworld.models.AzureAuthContext
import com.hivemq.extensions.helloworld.network.MSGraphClient
import com.hivemq.extensions.helloworld.network.AzureAuthenticator

class AADAuthenticationProvider : AuthenticatorProvider {
    private val graphClient: MSGraphClient = MSGraphClient()
    private val aadAuthenticator: AADAuthenticator = AADAuthenticator(this.graphClient)

    override fun getAuthenticator(authenticatorProviderInput: AuthenticatorProviderInput?): Authenticator {
        return this.aadAuthenticator
    }
}