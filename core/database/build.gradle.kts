plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp.library)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.galton.database"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(project.dependencies.platform(libs.koin))
    implementation(libs.koin.core)
    implementation(libs.koin.viewmodel)
    implementation(libs.koin.coroutines)
    implementation(libs.koin.android)

    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.moshi)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    detekt(libs.detekt.cli)
    detektPlugins(libs.detekt.plugins)
}