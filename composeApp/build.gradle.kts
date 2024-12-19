import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            //ktor
            implementation("io.ktor:ktor-client-logging:2.3.0")
            implementation("io.ktor:ktor-client-serialization:2.3.0")
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            //voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            implementation("com.github.sarxos:webcam-capture:0.3.12")
            implementation("org.bytedeco:javacv-platform:1.5.7")
            implementation("javazoom:jlayer:1.0.1")

            implementation("com.google.zxing:core:3.5.2")
            implementation("com.google.zxing:javase:3.5.2")

            //coil
            //implementation("io.coil-kt:coil-compose:2.0.0")
            //implementation ("org.jcodec:jcodec:0.2.5")
            //implementation("org.openjfx:javafx-media:17.0.0")
            //implementation("com.github.finnkuusisto:tinysound:1.1.1")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.example.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Msi, TargetFormat.Exe)
            packageName = "com.example.demo"
            packageVersion = "1.0.0"

            windows {
                // Windowsga tegishli sozlamalar
                menuGroup = "Demo"
                upgradeUuid = "123e4567-e89b-12d3-a456-426614174000"  // Unique UUID
                shortcut = true  // Ishga tushirish uchun shortcut yaratish
                menu = true
                iconFile.set(project.file("resources/ic_launcher_playstore.ico"))
            }
        }

        buildTypes.release.proguard {
            optimize.set(false)
        }
    }
}