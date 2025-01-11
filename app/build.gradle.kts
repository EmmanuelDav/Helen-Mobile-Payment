apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'com.google.gms.google-services'
apply plugin: 'kotlin-kapt'


android {
    compileSdkVersion 34
    namespace = "com.iyke.onlinebanking"

    defaultConfig {
        applicationId "com.iyke.onlinebanking"
        minSdkVersion 19
        targetSdkVersion 34
        versionCode 1
        versionName "1.0"
        multiDexEnabled true

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = '11'
    }
    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
    kapt {
        generateStubs = true
    }
}

dependencies {

    // General settings for .jar libraries in the `libs` folder
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin Standard Library
    implementation "org.jetbrains.kotlin:kotlin-stdlib:2.1.0" // Updated to the latest stable Kotlin version

    // AndroidX Libraries
    implementation 'androidx.appcompat:appcompat:1.7.0' // Latest version of AppCompat
    implementation 'androidx.constraintlayout:constraintlayout:2.2.0' // Beta for the latest updates
    implementation 'androidx.recyclerview:recyclerview:1.3.1'
    implementation 'androidx.cardview:cardview:1.0.0' // No new updates
    implementation 'androidx.gridlayout:gridlayout:1.0.0' // No new updates

    // Material Design
    implementation 'com.google.android.material:material:1.12.0' // Latest Material library

    // Firebase Libraries
    implementation platform('com.google.firebase:firebase-bom:32.0.0') // Use BOM for version alignment
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-analytics-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'

   // Glide
    implementation 'com.github.bumptech.glide:glide:4.16.0' // Updated version
    kapt 'com.github.bumptech.glide:compiler:4.16.0' // Glide compiler for annotation processing

    // MPAndroidChart
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0' // Latest version (as of now)

    // ZXing Barcode Scanner
    implementation 'com.journeyapps:zxing-android-embedded:4.3.0' // Latest version

    // Firebase UI for Firestore
    implementation 'com.firebaseui:firebase-ui-firestore:8.0.0' // Latest version

    // Groupie for RecyclerView
    implementation "com.xwray:groupie:2.9.0" // Updated to the latest stable version
    implementation "com.xwray:groupie-viewbinding:2.9.0" // Extensions for ViewBinding (replaces Kotlin Android Extensions)

    // Navigation Components
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.3' // Replaced with AndroidX Navigation
    implementation 'androidx.navigation:navigation-ui-ktx:2.8.5'

    // Multidex
    implementation 'androidx.multidex:multidex:2.0.1' // Replaced with AndroidX Multidex

    // Lifecycle Components
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7' // Updated ViewModel library
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.2' // Lifecycle runtime
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.8.7' // LiveData library

    // PinLockView
    implementation 'com.andrognito.pinlockview:pinlockview:2.1.0' // No updates available

    // Droid GIF Library
    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.25' // Updated GIF library

   // Testing Libraries
    testImplementation 'junit:junit:4.13.2' // Latest stable JUnit
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.2'

    //Room Database
    implementation "androidx.room:room-runtime:2.6.1"
    kapt "androidx.room:room-compiler:2.6.1"

    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.8.7"



}
