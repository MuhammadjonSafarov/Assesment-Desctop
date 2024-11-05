package com.example.demo.util

import java.util.*


object Localization {
    private var currentLocale: Locale = Locale("uz")

    private val supportedLocales = mapOf(
        "en" to Locale("en"),
        "uz" to Locale("uz")
    )

    private val resources: ResourceBundle
        get() = ResourceBundle.getBundle("strings", currentLocale)

    fun getString(key: String): String {
        return resources.getString(key)
    }

    fun switchLocale(languageCode: String) {
        currentLocale = supportedLocales[languageCode] ?: Locale("en")
    }
}
