package com.hivemq.extensions.helloworld

import com.hivemq.extension.sdk.api.annotations.NotNull
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput
import com.hivemq.extensions.helloworld.models.Device
import com.hivemq.extensions.helloworld.network.MSGraphClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.security.GeneralSecurityException
import java.security.cert.CertificateFactory
import java.security.cert.CertificateParsingException
import java.security.cert.X509Certificate
import java.util.*
import javax.naming.ldap.LdapName
import kotlin.jvm.optionals.getOrElse


class AADAuthenticator(
    private val graphClient: MSGraphClient
) : SimpleAuthenticator {
    private val log: Logger = LoggerFactory.getLogger(
        AADAuthenticator::class.java
    )

    @OptIn(ExperimentalStdlibApi::class)
    override fun onConnect(simpleAuthInput: SimpleAuthInput, simpleAuthOutput: SimpleAuthOutput) {
        val packet = simpleAuthInput.connectionInformation;
        val tlsInfo = packet.clientTlsInformation.getOrElse { return simpleAuthOutput.failAuthentication() }
        val certificate = tlsInfo.clientCertificate.getOrElse { return simpleAuthOutput.failAuthentication() }
        try {
            val device = getDeviceInfo(simpleAuthInput.connectPacket.clientId)
            device.deviceTemplate.firstOrNull()?.let { template ->
                this.graphClient.getMtlsConfig(template.mutualTLSOauthConfigurationID)
            }?.let { tlsAuth ->
                try {
                    tlsAuth.certificateAuthorities.map { authority ->
                        authority.certificate?.let { cert ->
                            val decoded: ByteArray = Base64.getDecoder()
                                .decode(cert)
                            certificate.verify(
                                CertificateFactory.getInstance("X.509")
                                    .generateCertificate(
                                        ByteArrayInputStream(decoded)
                                    )
                                    .publicKey
                            )

                            val urisInSAN = this.extractSANList(certificate)
                            val externalDeviceIds = urisInSAN.mapNotNull {
                                if (it.startsWith("urn:uuid:")) {
                                    it.substring(9, it.length)
                                } else {
                                    null
                                }
                            }
                            if (externalDeviceIds.any {
                                    it.equals(simpleAuthInput.connectPacket.userName.get(), true)
                                }) {
                                return simpleAuthOutput.authenticateSuccessfully()
                            }
                        }
                    }

                } catch (exception: GeneralSecurityException) {
                    log.info("cert not valid, trying next..")
                }
            }
        } catch (exception: Exception) {
            log.info("Authentication Failed: $exception")
        }
        simpleAuthOutput.failAuthentication()
    }

    private fun getDeviceInfo(deviceId: String): Device {
        return this.graphClient.getDeviceById(deviceId)
    }

    private fun extractSANList(clientCert: X509Certificate): ArrayList<String> {

        try {
            val subAltNames = clientCert.subjectAlternativeNames
            val hostNameList = ArrayList<String>()

            if (subAltNames != null) {
                for (altName in subAltNames) {
                    if (altName.size < 2) continue
                    when (altName[0] as Int?) {
                        6 -> {
                            val data = altName[1]!!
                            if (data is String) {
                                hostNameList.add(data)
                            }
                        }
                        else -> {}
                    }
                }
            }
            log.info("Parsed hostNames: " + hostNameList.joinToString("; "))
            log.info(clientCert.subjectX500Principal.name)
            return hostNameList
        } catch (e: CertificateParsingException) {
            throw RuntimeException(e)
        }
    }

    private fun extractSubjectDN(clientCert: X509Certificate): String {
        val ldap = LdapName(clientCert.subjectX500Principal.name)
        val cn = ldap.rdns.first {
            it.type.lowercase() == "cn"
        }

        log.info("Found CN ${cn.value}")
        return cn.value.toString()
    }

    companion object {
        private val log: @NotNull Logger? = LoggerFactory.getLogger(
            AADAuthenticator::class.java
        )
    }
}