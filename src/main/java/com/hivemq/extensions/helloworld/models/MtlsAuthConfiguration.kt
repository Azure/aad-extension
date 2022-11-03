package com.hivemq.extensions.helloworld.models

import com.google.gson.annotations.SerializedName

data class MtlsAuthConfiguration(

    @SerializedName("id")
    var id: String,

    @SerializedName("deletedDateTime")
    var deletedDateTime: String? = null,

    @SerializedName("displayName")
    var displayName: String,

    @SerializedName("tlsClientAuthParameter")
    var tlsClientAuthParameter: TlsClientAuthParamter,

    @SerializedName("certificateAuthorities")
    var certificateAuthorities: ArrayList<CertificateAuthority> = arrayListOf()
)

enum class TlsClientAuthParamter {
    @SerializedName("tls_client_auth_subject_dn")
    SubjectDistinguishedName,

    @SerializedName("tls_client_auth_san_dns")
    SubjectAlternativeNameDNS,

    @SerializedName("tls_client_auth_san_uri")
    SubjectAlternativeNameUri,

    @SerializedName("tls_client_auth_san_ip")
    SubjectAlternativeNameIp,

    @SerializedName("tls_client_auth_san_email")
    SubjectAlternativeNameEmail,

    @SerializedName("unknownFutureValue")
    Unknown
}

data class CertificateAuthority(

    @SerializedName("isRootAuthority")
    var isRootAuthority: Boolean? = null,

    @SerializedName("certificateRevocationListUrl")
    var certificateRevocationListUrl: String? = null,

    @SerializedName("deltaCertificateRevocationListUrl")
    var deltaCertificateRevocationListUrl: String? = null,

    @SerializedName("certificate")
    var certificate: String? = null,

    @SerializedName("issuer")
    var issuer: String? = null,

    @SerializedName("issuerSki")
    var issuerSki: String? = null

)