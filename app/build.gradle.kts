plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.wechattheme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wechattheme"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Xposed 编译期依赖（必须使用 compileOnly，运行时由宿主环境提供）
    compileOnly("de.robv.android.xposed:api:82")
    implementation("com.google.code.gson:gson:2.10.1")
}
