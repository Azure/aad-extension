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

import com.hivemq.extension.sdk.api.annotations.NotNull
import com.hivemq.extension.sdk.api.events.client.ClientLifecycleEventListener
import com.hivemq.extension.sdk.api.events.client.parameters.AuthenticationSuccessfulInput
import com.hivemq.extension.sdk.api.events.client.parameters.ConnectionStartInput
import com.hivemq.extension.sdk.api.events.client.parameters.DisconnectEventInput
import com.hivemq.extension.sdk.api.packets.general.MqttVersion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This is a very simple [ClientLifecycleEventListener]
 * which logs the MQTT version and identifier of every connecting client.
 *
 * @author Florian Limpöck
 * @since 4.0.0
 */
class HelloWorldListener : ClientLifecycleEventListener {
    override fun onMqttConnectionStart(connectionStartInput: @NotNull ConnectionStartInput?) {
        val version = connectionStartInput!!.connectPacket.mqttVersion
        when (version) {
            MqttVersion.V_5 -> log!!.info(
                "MQTT 5 client connected with id: {} ",
                connectionStartInput.clientInformation.clientId
            )

            MqttVersion.V_3_1_1 -> log!!.info(
                "MQTT 3.1.1 client connected with id: {} ",
                connectionStartInput.clientInformation.clientId
            )

            MqttVersion.V_3_1 -> log!!.info(
                "MQTT 3.1 client connected with id: {} ",
                connectionStartInput.clientInformation.clientId
            )
        }
    }

    override fun onAuthenticationSuccessful(authenticationSuccessfulInput: @NotNull AuthenticationSuccessfulInput?) {}
    override fun onDisconnect(disconnectEventInput: @NotNull DisconnectEventInput?) {
        log!!.info("Client disconnected with id: {} ", disconnectEventInput!!.clientInformation.clientId)
    }

    companion object {
        private val log: @NotNull Logger? = LoggerFactory.getLogger(
            HelloWorldListener::class.java
        )
    }
}