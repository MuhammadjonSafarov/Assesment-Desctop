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
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            //voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            implementation("com.github.sarxos:webcam-capture:0.3.12")
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
            targetFormats(TargetFormat.Deb, TargetFormat.Msi, TargetFormat.Dmg)
            packageName = "com.example.demo"
            packageVersion = "1.0.0"

            windows {
                // Windowsga tegishli sozlamalar
                menuGroup = "Demo"
                upgradeUuid = "123e4567-e89b-12d3-a456-426614174000"  // Unique UUID
                shortcut = true  // Ishga tushirish uchun shortcut yaratish
                menu = true
                iconFile.set(project.file("resources/launcher.ico"))
            }
        }

        buildTypes.release.proguard {
            optimize.set(false)
        }
    }
}