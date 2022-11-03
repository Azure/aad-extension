package com.hivemq.extensions.helloworld.services

import com.hivemq.extensions.helloworld.models.Device
import com.hivemq.extensions.helloworld.models.MtlsAuthConfiguration
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GraphDeviceService {
    @GET("devices/{deviceId}?\$expand=deviceTemplate")
    fun getDeviceById(@Path("deviceId") deviceId: String): Call<Device>

    @GET("devices/?\$filter=alternativeSecurityIds/any(a:a/type eq 2 and a/key eq '{externalDeviceId}'")
    fun getDeviceByExternalId(@Path("externalDeviceId") deviceId: String): Call<List<Device>>
    @GET("directory/certificateAuthorities/mutualTlsOauthConfigurations/{tlsConfigId}")
    fun getMtlsConfigById(@Path("tlsConfigId") mutualTLSOauthConfigurationID: String): Call<MtlsAuthConfiguration>
}
