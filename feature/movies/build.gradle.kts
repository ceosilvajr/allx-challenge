import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit


plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.galton.movies"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

// This is required for mockK issue https://mockk.io/doc/md/jdk16-access-exceptions.html
tasks.withType<Test>().all {
    jvmArgs(
        "--add-opens", "java.base/java.time=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED"
    )
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

kover {
    reports {
        verify {
            rule {
                bound {
                    minValue = 95
                    coverageUnits = CoverageUnit.LINE
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                }
            }
        }
        filters {
            includes {
                classes(
                    "*Repository"
                )
            }
        }
    }
}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:utils"))
    implementation(project(":core:test"))

    implementation(libs.timber)

    implementation(project.dependencies.platform(libs.koin))
    implementation(libs.koin.core)
    implementation(libs.koin.viewmodel)
    implementation(libs.koin.coroutines)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.navigation)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.fragment.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.room.paging)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.coil)
    implementation(libs.compose.coil.network)

    ksp(libs.moshi.codegen)
    implementation(libs.moshi)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.koin.core)
    testImplementation(libs.test.koin)
    testImplementation(libs.test.mockServer)
    testImplementation(libs.test.kotest.assertions.core)
    testImplementation(libs.test.androidx.ext.junit)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.mockk.android)
    testImplementation(libs.test.mockk.agent)
    testImplementation(libs.test.androidx.arch.core)
    testImplementation(libs.test.androidx.core.ktx)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.turbine)

    androidTestImplementation(libs.androidx.espresso.core)

    detekt(libs.detekt.cli)
    detektPlugins(libs.detekt.plugins)
}