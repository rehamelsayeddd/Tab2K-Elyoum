buildscript {

    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.0") // or the latest version
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")



    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false


}