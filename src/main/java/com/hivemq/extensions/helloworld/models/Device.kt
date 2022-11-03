package com.hivemq.extensions.helloworld.models

import com.google.gson.annotations.SerializedName

data class Device  (
    @SerializedName("@odata.context")
    val odataContext: String,

    val id: String,
    val deletedDateTime: Any? = null,
    val accountEnabled: Boolean,
    val approximateLastSignInDateTime: String,
    val complianceExpirationDateTime: Any? = null,
    val createdDateTime: String,
    val deviceCategory: Any? = null,

    @SerializedName("deviceId")
    val deviceID: String,

    val deviceMetadata: Any? = null,
    val deviceOwnership: Any? = null,
    val deviceVersion: Long,
    val displayName: Any? = null,
    val domainName: Any? = null,
    val enrollmentProfileName: Any? = null,
    val enrollmentType: Any? = null,
    val externalSourceName: String,
    val isCompliant: Any? = null,
    val isManaged: Any? = null,
    val isManagementRestricted: Any? = null,
    val isRooted: Any? = null,
    val managementType: Any? = null,
    val manufacturer: String,

    @SerializedName("mdmAppId")
    val mdmAppID: Any? = null,

    val model: String,
    val onPremisesLastSyncDateTime: Any? = null,
    val onPremisesSyncEnabled: Any? = null,
    val operatingSystem: String,
    val operatingSystemVersion: String,
    val hostnames: List<Any?>,

    @SerializedName("physicalIds")
    val physicalIDS: List<Any?>,

    val profileType: String,
    val registrationDateTime: Any? = null,
    val sourceType: String,
    val systemLabels: List<Any?>,
    val trustType: Any? = null,

    @SerializedName("alternativeSecurityIds")
    val alternativeSecurityIDS: List<AlternativeSecurityID>,

    val extensionAttributes: ExtensionAttributes,
    val deviceTemplate: List<DeviceTemplate>
)

data class AlternativeSecurityID (
    val type: Long,
    val identityProvider: Any? = null,
    val key: String
)

data class DeviceTemplate (
    val id: String,
    val deletedDateTime: Any? = null,

    @SerializedName("mutualTlsOauthConfigurationId")
    val mutualTLSOauthConfigurationID: String,

    @SerializedName("mutualTlsOauthConfigurationTenantId")
    val mutualTLSOauthConfigurationTenantID: String,

    val deviceAuthority: String,
    val manufacturer: String,
    val model: String,
    val operatingSystem: String
)

data class ExtensionAttributes (
    val extensionAttribute1: Any? = null,
    val extensionAttribute2: Any? = null,
    val extensionAttribute3: Any? = null,
    val extensionAttribute4: Any? = null,
    val extensionAttribute5: Any? = null,
    val extensionAttribute6: Any? = null,
    val extensionAttribute7: Any? = null,
    val extensionAttribute8: Any? = null,
    val extensionAttribute9: Any? = null,
    val extensionAttribute10: Any? = null,
    val extensionAttribute11: Any? = null,
    val extensionAttribute12: Any? = null,
    val extensionAttribute13: Any? = null,
    val extensionAttribute14: Any? = null,
    val extensionAttribute15: Any? = null
)
