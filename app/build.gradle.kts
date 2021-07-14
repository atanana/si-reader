plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.getkeepsafe.dexcount")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("io.gitlab.arturbosch.detekt").version("1.16.0-RC2")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId("atanana.com.sireader")
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")
        testInstrumentationRunner("android.support.test.runner.AndroidJUnitRunner")
        vectorDrawables.useSupportLibrary = true

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("debug") {
            multiDexEnabled = true
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro", getDefaultProguardFile("proguard-android.txt"))
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dexcount {
    format = "list"
    includeClasses = false
    includeClassCount = false
    includeFieldCount = true
    includeTotalMethodCount = false
    orderByMethodCount = false
    verbose = false
    maxTreeDepth = Integer.MAX_VALUE
    teamCityIntegration = false
    teamCitySlug = null
    runOnEachPackage = true
    maxMethodCount = 640000
}

detekt {
    toolVersion = "1.17.0-RC3"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra.get("kotlinVersion")}")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("androidx.fragment:fragment-ktx:1.3.5")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    implementation("com.github.Zhuinden:fragmentviewbindingdelegate-kt:1.0.0")

    val hiltVersion = rootProject.extra.get("hiltVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    val roomVersion = "2.2.6"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    val navVersion = rootProject.extra.get("navVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("org.apache.poi:poi-scratchpad:3.17")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
