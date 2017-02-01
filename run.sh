#!/bin/bash

./gradlew :blink:assembleDebug
adb -s Android.local shell pm uninstall com.example.androidthings.simplepio

#adb -s Android.local install ./blink/build/outputs/apk/blink-release.apk
adb -s Android.local install ./blink/build/outputs/apk/blink-debug.apk


adb -s Android.local shell am start -n com.example.androidthings.simplepio/.BlinkActivity