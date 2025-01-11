buildscript {
    ext.kotlin_version = '1.9.10'
    repositories {
        google()
        jcenter() // Warning: this repository is going to shut down soon
        maven { url='https://jitpack.io'}

    }

    dependencies {
        classpath 'com.android.tools.build:gradle:8.0.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath 'com.google.gms:google-services:4.3.13'
    }
}


allprojects {
    repositories {
        google()
        jcenter() // Warning: this repository is going to shut down soon
        maven { url='https://jitpack.io'}
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}