package com.example.demo.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.NetworkInterface

suspend fun getLocalIpAddress(): String {
    return withContext(Dispatchers.IO){
        try {
            val inetAddress: InetAddress = InetAddress.getLocalHost()
            inetAddress.hostAddress
        } catch (e: Exception) {
            "IP manzilni olishning iloji bo'lmadi"
        }
    }
}

// MAC manzilni olish funksiyasi
suspend fun getMacAddress(): String {
    return withContext(Dispatchers.IO){
        try {
        val inetAddress: InetAddress = InetAddress.getLocalHost()
        val network: NetworkInterface = NetworkInterface.getByInetAddress(inetAddress)
        val macBytes = network.hardwareAddress ?: return@withContext "MAC manzilni olishning iloji bo'lmadi"
        macBytes.joinToString(separator = ":") { String.format("%02X", it) }
    } catch (e: Exception) {
        "MAC manzilni olishning iloji bo'lmadi"
    }
    }
}

suspend fun isInternetAvailable(): Boolean {
    return withContext(Dispatchers.IO){
        try {
            val address: InetAddress = InetAddress.getByName("google.com")
            !address.equals("") // Agar manzil bo'sh bo'lmasa, ulanish mavjud
        } catch (e: Exception) {
            false // Istisno yuz berganda internet yo'q deb hisoblanadi
        }
    }
}