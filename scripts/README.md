# HydroNudge workstation commands (Windows)

Run these from PowerShell. Each script finds this project, uses its Gradle Wrapper, and selects the Android Studio JBR and Android SDK installed for this workstation.

## Build and checks

```powershell
powershell -ExecutionPolicy Bypass -File scripts/build-debug.ps1
powershell -ExecutionPolicy Bypass -File scripts/test-unit.ps1
powershell -ExecutionPolicy Bypass -File scripts/lint.ps1
powershell -ExecutionPolicy Bypass -File scripts/qa-summary.ps1
```

The Gradle scripts pass `--no-configuration-cache` because this project's current AAPT2 resource task fails configuration-cache serialization. The project setting itself remains unchanged.

## Samsung over Wi-Fi

```powershell
powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action list
```

If the phone has disconnected, unlock it, enable Wireless debugging on the trusted Wi-Fi network, and read the **connection** endpoint (IP and port) from Android Studio's **Device Manager > Pair Devices Using Wi-Fi** or from `adb mdns services`. Then run:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action connect -Endpoint 192.0.2.10:37000
powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action list
```

Use the endpoint displayed on your phone; the sample above is not a real device address. A new pairing code is only needed if Android forgets this computer. Never save a pairing code in project files.

## Install, launch, screenshot, and crash check

```powershell
powershell -ExecutionPolicy Bypass -File scripts/build-debug.ps1
powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action install -Serial <ADB-serial>
powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action launch -Serial <ADB-serial>
powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action screenshot -Serial <ADB-serial>
powershell -ExecutionPolicy Bypass -File scripts/device.ps1 -Action crashlog -Serial <ADB-serial>
```

`install` uses `adb install -r`; it does not uninstall or clear app data. Screenshots are saved under `qa-reports/local-device/` after the script confirms HydroNudge is in the foreground. Crash reports are limited to lines identifying HydroNudge.

## Instrumentation safety

The first real-phone `connectedDebugAndroidTest` attempt removed `app.hydronudge`; the app has Android backup disabled, so some private app data may no longer be recoverable. The instrumentation script is therefore blocked unless `-AllowPossibleTargetAppRemoval` is explicitly supplied. Prefer an emulator with no personal data. Do not run it on the Samsung again without a separate data backup and explicit decision to accept the risk.

```powershell
powershell -ExecutionPolicy Bypass -File scripts/instrumentation.ps1 -DeviceSerial <ADB-serial> -AllowPossibleTargetAppRemoval
```

The default instrumentation classes are dashboard rendering and quick-amount validation. The script refuses to start without the safety switch. `qa-summary.ps1` reads actual Gradle XML results and reports both passed and failed tests.
