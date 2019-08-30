# Watchdog timer library for Android

Simple WatchDog Timer library for Android. If not periodically reset, the ```wdt``` object will fire a callback after a period of time defined when object is instantiated.

## Usage

Add dependency

```

```

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
wdl.clear()
```
