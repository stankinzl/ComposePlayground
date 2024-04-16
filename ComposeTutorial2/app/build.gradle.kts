plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    kotlin("kapt")
}

android {
    buildFeatures.buildConfig = true

    namespace = "com.stanislavkinzl.composeplayground"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stanislavkinzl.composeplayground"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

        buildConfigField("String", "API_BASE_URL", project.properties["BASE_URL_ALL_ENV"].toString())
        buildConfigField("String", "CAT_API_KEY", project.properties["CAT_API_KEY"].toString())
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val destinationsVersion = "1.10.1"
    val hiltVersion = "2.50"
    val koinVersion = "3.5.3"
    val lifecycleVersion = "2.7.0"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    // https://developer.android.com/jetpack/compose/bom/bom-mapping
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    // For xml navigation
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // https://google.github.io/accompanist/navigation-animation/
    implementation("androidx.navigation:navigation-compose") // Native compose navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7") // XML navigation (for compose)
    implementation("io.github.raamcosta.compose-destinations:animations-core:$destinationsVersion") // Destinations navigation
    ksp("io.github.raamcosta.compose-destinations:ksp:$destinationsVersion") // Destinations navigation

    // Image loading library backed by kotlin coroutines
    implementation("io.coil-kt:coil-compose:2.6.0") // https://coil-kt.github.io/coil/compose/

    // https://google.github.io/accompanist/systemuicontroller/
    // https://search.maven.org/search?q=g:com.google.accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

    // Dagger-Hilt vs Koin:
    // https://medium.com/@aruncse2k20/dagger-hilt-vs-koin-e17af98427da
    // Koin - service locator pattern,
    // Dagger-Hilt (Hilt = boilerplate wrapper for Dagger) - Dependency injection pattern
    // DI
    // https://insert-koin.io/docs/quickstart/android-compose/
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")


    //Network
    // Retrofit vs Ktor https://medium.com/@ttdevelopment/retrofit-vs-ktor-for-android-development-a-surface-level-comparison-a354b236920b
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.moshi:moshi-adapters:1.13.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
//    kapt "com.squareup.moshi:moshi-kotlin-codegen:$moshi_version"

    // Dagger hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // https://dagger.dev/dev-guide/ksp.html
    ksp("com.google.dagger:dagger-compiler:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-guava:$roomVersion")
    // TODO: Don't need autobindings atm, but might be useful

//    val autobindingsVersion = "1.1-beta05"
//    implementation("io.github.kaustubhpatange:autobindings-room:$autobindingsVersion") // Type converter annotations
//    kapt("io.github.kaustubhpatange:autobindings-compiler:$autobindingsVersion")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.jakewharton.timber:timber:5.0.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


}