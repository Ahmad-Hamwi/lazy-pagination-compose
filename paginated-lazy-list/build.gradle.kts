plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.ahmadhamwi.paginated_lazy_list"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get().toString()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions.apply {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.bundles.android)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.testing)

    testImplementation(platform(libs.compose.bom))
    testImplementation(libs.bundles.composeTesting)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.composeTesting)

    debugImplementation(libs.bundles.composeDebug)
}

koverReport {
    defaults {
        mergeWith("debug")
        html {
            onCheck = false
            setReportDir(layout.buildDirectory.dir("artifacts/reports/kover/coverageResults"))
        }
    }
}