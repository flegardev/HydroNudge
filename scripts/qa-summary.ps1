param([string] $OutputPath = 'qa-reports\local-device\QA_SUMMARY.md')
. (Join-Path $PSScriptRoot '_common.ps1')
$out = Join-Path $script:ProjectRoot $OutputPath
New-Item -ItemType Directory -Path (Split-Path -Parent $out) -Force | Out-Null
$xmlFiles = @(Get-ChildItem 'app\build\test-results\testDebugUnitTest' -Filter 'TEST-*.xml' -File -ErrorAction SilentlyContinue)
$total=0; $failures=0; $errors=0; $skipped=0
foreach ($file in $xmlFiles) {
    [xml]$xml = Get-Content -LiteralPath $file.FullName
    $suite = $xml.testsuite
    $total += [int]$suite.tests; $failures += [int]$suite.failures; $errors += [int]$suite.errors; $skipped += [int]$suite.skipped
}
$adb = Join-Path $env:ANDROID_HOME 'platform-tools\adb.exe'
$deviceLines = @(& $adb devices -l)
$onlineDevices = @($deviceLines | Where-Object { $_ -match '\sdevice\s' })
$samsung = @($onlineDevices | Where-Object { $_ -match 'model:SM_S928B' })
$devices = if ($samsung.Count -gt 0) { "$($samsung.Count) authorized Samsung SM-S928B connection(s); serials and local network addresses hidden." } elseif ($onlineDevices.Count -gt 0) { "$($onlineDevices.Count) authorized Android device connection(s); serials and local network addresses hidden." } else { 'No authorized Android device currently connected.' }
$lint = if (Test-Path 'app\build\reports\lint-results-debug.html') { 'Generated: `app/build/reports/lint-results-debug.html`' } else { 'Not available in current build outputs.' }
$apk = if (Test-Path 'app\build\outputs\apk\debug\app-debug.apk') { 'Present: `app/build/outputs/apk/debug/app-debug.apk`' } else { 'Not built.' }
$unit = if ($xmlFiles.Count -gt 0) { "$total tests; $failures failures; $errors errors; $skipped skipped." } else { 'No unit test result XML found.' }
$deviceXmlFiles = @(Get-ChildItem 'app\build\outputs\androidTest-results\connected\debug' -Filter 'TEST-*.xml' -File -ErrorAction SilentlyContinue)
$deviceTests=0; $deviceFailures=0; $deviceErrors=0; $deviceSkipped=0
foreach ($file in $deviceXmlFiles) {
    [xml]$xml = Get-Content -LiteralPath $file.FullName
    $suite = $xml.testsuites
    if (-not $suite) { $suite = $xml.testsuite }
    $deviceTests += [int]$suite.tests; $deviceFailures += [int]$suite.failures; $deviceErrors += [int]$suite.errors; $deviceSkipped += [int]$suite.skipped
}
$deviceReport = if ($deviceXmlFiles.Count -gt 0) { '{0} tests; {1} failures; {2} errors; {3} skipped. See app/build/reports/androidTests/connected/debug/index.html.' -f $deviceTests,$deviceFailures,$deviceErrors,$deviceSkipped } else { 'No connected instrumentation result XML found.' }
$deviceNotes = if (Test-Path 'qa-reports\local-device\device-results.txt') { 'Additional app QA notes: `qa-reports/local-device/device-results.txt`' } else { 'No additional app-side QA note file.' }
$content = @"
# HydroNudge QA summary

Generated: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss zzz')

- Unit tests: $unit
- Connected instrumentation: $deviceReport
- Lint: $lint
- Debug APK: $apk
- Device QA notes: $deviceNotes

## ADB devices

``````text
$devices
``````

This report reflects files and device state found at generation time. It does not infer that a phone test ran from an APK build alone.
"@
Set-Content -LiteralPath $out -Value $content -Encoding utf8
Write-Host "Wrote $out"
