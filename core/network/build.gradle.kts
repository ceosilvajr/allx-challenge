plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp.library)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.galton.network"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

detekt {
    toolVersion = libs.versions.detektVersion.get()
    source.setFrom("src/main/java", "src/main/kotlin")
    parallel = true
    config.setFrom("${rootProject.projectDir}/detekt/config.yml")
    buildUponDefaultConfig = true
    allRules = false
    baseline = file("${rootProject.projectDir}/detekt/baseline.xml")
    disableDefaultRuleSets = false
    ignoreFailures = true
    basePath = projectDir.absolutePath
}

dependencies {
    implementation(project(":core:models"))

    implementation(libs.timber)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(project.dependencies.platform(libs.koin))
    implementation(libs.koin.core)
    implementation(libs.koin.coroutines)
    implementation(libs.koin.android)

    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    debugApi(libs.chucker)
    releaseApi(libs.chucker.noop)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    detekt(libs.detekt.cli)
    detektPlugins(libs.detekt.plugins)
}