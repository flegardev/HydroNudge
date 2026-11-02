param(
    [string] $ClassName = 'app.hydronudge.DashboardTest,app.hydronudge.qa.QuickAmountValidationTest',
    [string] $DeviceSerial,
    [switch] $AllowPossibleTargetAppRemoval
)
. (Join-Path $PSScriptRoot '_common.ps1')

if (-not $AllowPossibleTargetAppRemoval) {
    throw 'Safety gate: connectedDebugAndroidTest removed app.hydronudge from the Samsung during the previous run. Use an emulator, or explicitly pass -AllowPossibleTargetAppRemoval only after protecting phone data.'
}

$adb = Join-Path $env:ANDROID_HOME 'platform-tools\adb.exe'
$online = @(& $adb devices | Select-Object -Skip 1 | Where-Object { $_ -match '\sdevice$' })
if ($DeviceSerial) {
    if (-not ($online | Where-Object { $_ -match "^$([regex]::Escape($DeviceSerial))\s" })) {
        throw "Selected ADB device is not online: $DeviceSerial"
    }
} elseif ($online.Count -ne 1) {
    throw "Found $($online.Count) online ADB device(s). Set -DeviceSerial explicitly before running instrumentation."
}

Write-Host "Running only these instrumentation classes: $ClassName"
Write-Host 'The selected validation test restores its original HydroNudge preferences. The Gradle connected-test task may still remove the target package; protect phone data first.'
Invoke-HydroGradle @(':app:connectedDebugAndroidTest', "-Pandroid.testInstrumentationRunnerArguments.class=$ClassName")
