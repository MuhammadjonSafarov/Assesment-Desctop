package com.example.demo.util

import java.util.*

object ResourceLoader {
    private var currentLocale: Locale = Locale("uz")
    private lateinit var bundle: ResourceBundle

    init {
        loadBundle()
    }

    fun setLocale(locale: Locale) {
        currentLocale = locale
        loadBundle()
    }

    private fun loadBundle() {
        bundle = ResourceBundle.getBundle("strings", currentLocale)
    }

    fun getString(key: String): String {
        return bundle.getString(key)
    }
}