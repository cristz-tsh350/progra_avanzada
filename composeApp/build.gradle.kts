import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // --- ¡BORRA LA LÍNEA DE 'kotlinx-html-js' DE AQUÍ! ---
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }

        // --- ¡AÑADE LA LÍNEA AQUÍ! ---
        jsMain.dependencies {
                // ¡Este bloque debe tener las mismas dependencias de UI que commonMain!
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

        }

        wasmJsMain.dependencies {
            // Wasm (webMain) se queda vacío por ahora
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "com.example.demo.MainKt" // (O 'org.example.s2proyecto.MainKt' si usas ese)
            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "com.example.demo"
                packageVersion = "1.0.0"
            }

            jvmArgs("--enable-native-access=ALL-UNNAMED")
        }
    }

    // --- LÍNEA ERRÓNEA ELIMINADA ---
    // (La línea 'web.application {}' se ha borrado de aquí)
}