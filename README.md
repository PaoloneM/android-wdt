# Watchdog timer library for Android

Simple WatchDog Timer library for Android. If not periodically reset, the ```wdt``` object will fire a callback after a period of time defined when object is instantiated.

## Installation

Add Jitpack in Project's gradle file

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }      <-- add this line
    }
}
```
and add dependency in app's gradle file
```
    implementation 'com.github.PaoloneM:android-wdt:1.0.0'
```

## Usage

Let your class implement the ```WdtCallback``` interface:
```kotlin
override fun onWdtExpired() {
    // Do something when Wdt Expires
}
```
Create a watchdog timer 

```kotlin
val timeout: Int = 10              // Timeout in seconds
val callback: WdtCallback = this   // The callback to fire when WDT expires

val wdt: Wdt = Wdt(context, timeout, callback)

```

The timer starts counting, so you have to periodically reset it by calling

```kotlin
wdt.reset()
```

When you have done with it, remember to disable it by calling
```kotlin
wdt.clear()
```
