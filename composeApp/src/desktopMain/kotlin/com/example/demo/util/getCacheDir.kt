package com.example.demo.util

import java.io.File

fun getCacheDir(): File {
    val os = System.getProperty("os.name").lowercase()

    return when {
        os.contains("win") -> File(System.getenv("LOCALAPPDATA"), "Assesment/Cache")
        os.contains("mac") -> File(System.getProperty("user.home"), "Library/Caches/MyApp")
        else -> File(System.getProperty("user.home"), ".myapp/cache")
    }
}