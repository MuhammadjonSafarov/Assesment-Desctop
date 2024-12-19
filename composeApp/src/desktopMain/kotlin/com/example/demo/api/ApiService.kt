package com.example.demo.api

import com.example.demo.data.BaseResponse
import com.example.demo.data.LoginRequest
import com.example.demo.data.LoginResponse
import com.example.demo.data.listening.ListeningData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.FileOutputStream

class ApiService {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
            }
        }
        install(Logging) {
            level = LogLevel.ALL // Log all requests and responses
            logger = Logger.DEFAULT // Default logger prints to console
        }
    }

    suspend fun getFetchData(): String {
        val result = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "dummyjson.com"
                path("test")
            }
        }
        return if (result.status.isSuccess()) {
            result.bodyAsText()
        } else {
            result.status.description
        }
    }

    suspend fun getListening(id: Long, token: String): ListeningData? = withContext(Dispatchers.IO) {
        val result = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "test.assessmenttest.uz"
                path("api/v1/listening/1")
            }
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        if (result.status.isSuccess()) {
            result.body()
        } else {
            println(result.bodyAsText())
            null
        }
    }

    suspend fun login(username: String, password: String): BaseResponse<LoginResponse>? {
        val loginRequest = LoginRequest(username = username, password = password)
        val result = httpClient.post {
            url {
                protocol = URLProtocol.HTTPS
                host = "test.assessmenttest.uz"
                path("api/v1/auth-payload")
            }
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }
        return if (result.status.isSuccess()) {
            result.body()
        } else {
            null
        }
    }

    suspend fun downloadMusic(url: String, outputFile: String) = withContext(Dispatchers.IO) {
        val client = HttpClient(CIO) {
            install(ContentNegotiation)
            install(Logging) {
                level = LogLevel.ALL
            }
        }
        try {
            val fileBytes = client.get(url).readBytes()
            FileOutputStream(outputFile).use { outputStream ->
                outputStream.write(fileBytes)
            }
            println("Audio file downloaded successfully to $outputFile")
        } catch (e: Exception) {
            println("Failed to download the file: ${e.message}")
        } finally {
            client.close()
        }
        delay(1000)
    }
}