buildscript {

    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")

        //for google sign in with firebase
        classpath ("com.google.gms:google-services:4.3.14")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}