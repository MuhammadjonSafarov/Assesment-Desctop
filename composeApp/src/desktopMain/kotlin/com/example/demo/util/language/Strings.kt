package com.example.demo.util.language

import androidx.compose.ui.text.intl.Locale

sealed class Strings {
    abstract val app_welcome: String
    abstract val app_next: String
    abstract val mac_address:String
    abstract val ip_address:String
    abstract val status_network:String
    abstract val app_version:String
    abstract val active:String
    abstract val not_active:String
    abstract val uzbek:String
    abstract val russion:String
    abstract val english:String
    abstract val where_are_you_located:String
    abstract val select_number_computer:String

    object English : Strings() {
        override val app_welcome = "Welcome to the application settings page"
        override val app_next = "Continue"

        override val mac_address="MAC address"
        override val ip_address="IP address"
        override val status_network="Status network"
        override val app_version="Version"
        override val active="Avtive"
        override val not_active="Not avtive"

        override val english = "English"
        override val uzbek = "Uzbek"
        override val russion = "Russion"

        override val where_are_you_located = "Where are you located"
        override val select_number_computer = "Select the number of this computer"
    }

    object Russian : Strings() {
        override val app_welcome = "Добро пожаловать на страницу настроек приложения!"
        override val app_next: String = "Продолжать"

        override val mac_address="MAC адрес"
        override val ip_address="IP адрес"
        override val status_network="Статус сети"
        override val app_version="Версия"
        override val active="Активный"
        override val not_active="Не активен"

        override val english = "English"
        override val uzbek = "Uzbek"
        override val russion = "Russion"

        override val where_are_you_located = "Где вы находитесь"
        override val select_number_computer = "Выберите номер этого компьютера"
    }

    object Uzbek : Strings() {
        override val app_welcome = "Xush kelibsiz ilovaning sozlash sahifasiga "
        override val app_next: String = "Davom etish"

        override val mac_address="MAC manzil"
        override val ip_address="IP manzil"
        override val status_network="Tarmoq holati"
        override val app_version="Veriyasi"
        override val active="Faol"
        override val not_active="Faol emas"

        override val english = "English"
        override val uzbek = "Uzbek"
        override val russion = "Russion"

        override val where_are_you_located = "Siz qayerda joylashgansiz ?"
        override val select_number_computer = "Bu kompyuterning no\'merini tanlanng"
    }
}

// Tanlangan tilga asoslangan matnlarni olish
fun getStrings(locale: Locale): Strings {
    return when (locale.language) {
        "ru" -> Strings.Russian
        "uz" -> Strings.Uzbek
        else -> Strings.English
    }
}