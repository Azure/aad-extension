package com.hivemq.extensions.helloworld.network

import com.hivemq.extensions.helloworld.models.Device
import com.hivemq.extensions.helloworld.services.GraphDeviceService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MSGraphClient {
    private val service: GraphDeviceService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://graph.microsoft-ppe.com/beta/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(HttpClient.WithAuthorizationHeader)
            .build()

        this.service = retrofit.create(GraphDeviceService::class.java)
    }

    fun getDeviceById(deviceId: String): Device {
        var response = this.service.getDeviceById(deviceId).execute();
        return response.body() ?: throw RuntimeException("No such device found")
    }

}