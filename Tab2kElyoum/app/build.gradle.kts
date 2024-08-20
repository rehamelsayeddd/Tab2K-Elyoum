plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

    id ("androidx.navigation.safeargs")

}

android {
    namespace = "com.example.tab2kelyoum"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tab2kelyoum"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.base)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    //retrofit and gson
    implementation ("com.google.code.gson:gson:2.8.5")
    implementation  ("com.squareup.retrofit2:retrofit:2.4.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.4.0")

    //Glide
    implementation   ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    // Import the Firebase BoM
    implementation ("com.google.firebase:firebase-bom:31.1.1")

    //Navigation fragment
    implementation ("androidx.navigation:navigation-fragment:2.3.5")
    implementation ("androidx.navigation:navigation-ui:2.3.5")
    //Circular Image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Lottie
    implementation ("com.airbnb.android:lottie:5.0.3")

    //googleplay service to enable signing in using google
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //RXJAVA
    implementation ("io.reactivex.rxjava3:rxjava:2.5.0")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")

    //rx retrofit
    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    val roomVersion = "2.5.0"
    //Room
    implementation ("androidx.room:room-runtime:$roomVersion")
    annotationProcessor ("androidx.room:room-compiler:$roomVersion")

    //RX Room
    implementation("androidx.room:room-rxjava3:$roomVersion")
    implementation ("androidx.sqlite:sqlite:2.3.0")
    testImplementation("androidx.room:room-testing:$roomVersion")

    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    //Firestore
    implementation ("com.google.firebase:firebase-firestore:24.4.1")


    implementation ("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")

    //for rounded items
    implementation ("com.makeramen:roundedimageview:2.3.0")


}