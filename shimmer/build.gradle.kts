plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.dhs.tools.shimmer"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.dhs.tools"  // 填写自己的groupId
            artifactId = "loadview"  //填写自己的artifactId
            version = "1.0.0"
            afterEvaluate {
                from(components["release"])
            }
        }

        register<MavenPublication>("Snapshot") {
            groupId = "com.dhs.tools"  // 填写自己的groupId
            artifactId = "loadview"  //填写自己的artifactId
            version = "1.0.0-SNAPSHOT-" + System.currentTimeMillis()
            println("snapshot version : " + version)

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            url = uri("https://www.xxxxx.com/")  // 填写自己的maven地址
            name = "MyRelease"  // 这个是Repository名称，即远端仓库的名称，这个根据自己喜好命名
            isAllowInsecureProtocol = true  // 如果url是http,需要填上这个，如果是https，则可以不填
            credentials {
                username = "mUserName"  // maven仓库的用户名称
                password = "mPassword"  // maven仓库的密码
            }
        }
    }
}

