package com.hivemq.extensions.helloworld

import com.hivemq.extension.sdk.api.annotations.NotNull
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput
import com.hivemq.extension.sdk.api.client.parameter.ConnectionInformation
import com.hivemq.extensions.helloworld.models.AzureAuthResponse
import com.hivemq.extensions.helloworld.models.Device
import com.hivemq.extensions.helloworld.network.AzureAuthenticator
import com.hivemq.extensions.helloworld.network.MSGraphClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.cert.CertificateParsingException

class AADAuthenticator (
    private val graphClient: MSGraphClient
) : SimpleAuthenticator {
    override fun onConnect(simpleAuthInput: SimpleAuthInput, simpleAuthOutput: SimpleAuthOutput) {
        val hostNameList = extractSANList(simpleAuthInput.connectionInformation)
        try {
            val device = getDeviceInfo(simpleAuthInput.connectPacket.clientId)
            simpleAuthOutput.authenticateSuccessfully()
        } catch (exception: Exception) {
            simpleAuthOutput.failAuthentication()
        }
    }

    private fun getDeviceInfo(deviceId: String): Device {
        return this.graphClient.getDeviceById(deviceId)
    }

    private fun extractSANList(connectInfo: ConnectionInformation): ArrayList<String> {

        val tlsInfo = connectInfo.clientTlsInformation.get()
        try {
            val clientCert = tlsInfo.clientCertificate.get()
            val subAltNames = clientCert.subjectAlternativeNames
            val hostNameList = ArrayList<String>()
            if (subAltNames != null) {
                for (altName in subAltNames) {
                    if (altName.size < 2) continue
                    when (altName[0] as Int?) {
                        2, 7 -> {
                            val data = altName[1]!!
                            if (data is String) {
                                hostNameList.add(data)
                            }
                        }

                        else -> {}
                    }
                }
            }
            println("Parsed hostNames: " + java.lang.String.join(", ", hostNameList))
            return hostNameList
        } catch (e: CertificateParsingException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val log: @NotNull Logger? = LoggerFactory.getLogger(
            AADAuthenticator::class.java
        )
    }
}