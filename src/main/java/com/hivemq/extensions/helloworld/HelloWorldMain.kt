/*
 * Copyright 2018-present HiveMQ GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hivemq.extensions.helloworld

import com.hivemq.extension.sdk.api.ExtensionMain
import com.hivemq.extension.sdk.api.annotations.NotNull
import com.hivemq.extension.sdk.api.client.ClientContext
import com.hivemq.extension.sdk.api.client.parameter.InitializerInput
import com.hivemq.extension.sdk.api.events.client.parameters.ClientLifecycleEventListenerProviderInput
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput
import com.hivemq.extension.sdk.api.services.Services
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This is the main class of the extension,
 * which is instantiated either during the HiveMQ start up process (if extension is enabled)
 * or when HiveMQ is already started by enabling the extension.
 *
 * @author Florian Limpöck
 * @since 4.0.0
 */
class HelloWorldMain : ExtensionMain {
    override fun extensionStart(
        extensionStartInput: @NotNull ExtensionStartInput?,
        extensionStartOutput: @NotNull ExtensionStartOutput?
    ) {
        try {
//            addClientLifecycleEventListener();
//            addPublishModifier();
            val extensionInformation = extensionStartInput!!.extensionInformation
            log!!.info("Started " + extensionInformation.name + ":" + extensionInformation.version)
            Services.securityRegistry().setAuthenticatorProvider(AADAuthenticationProvider())
        } catch (e: Exception) {
            log!!.error("Exception thrown at extension start: ", e)
        }
    }

    override fun extensionStop(
        extensionStopInput: @NotNull ExtensionStopInput?,
        extensionStopOutput: @NotNull ExtensionStopOutput?
    ) {
        val extensionInformation = extensionStopInput!!.extensionInformation
        log!!.info("Stopped " + extensionInformation.name + ":" + extensionInformation.version)
    }

    private fun addClientLifecycleEventListener() {
        val eventRegistry = Services.eventRegistry()
        val helloWorldListener = HelloWorldListener()
        eventRegistry.setClientLifecycleEventListener { input: ClientLifecycleEventListenerProviderInput? -> helloWorldListener }
    }

    private fun addPublishModifier() {
        val initializerRegistry = Services.initializerRegistry()
        val helloWorldInterceptor = HelloWorldInterceptor()
        initializerRegistry.setClientInitializer { initializerInput: InitializerInput?, clientContext: ClientContext ->
            clientContext.addPublishInboundInterceptor(
                helloWorldInterceptor
            )
        }
    }

    companion object {
        private val log: @NotNull Logger? = LoggerFactory.getLogger(
            HelloWorldMain::class.java
        )
    }
}