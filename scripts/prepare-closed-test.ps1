# Local unsigned validation only. Never signs, uploads or changes a connected device.
. "$PSScriptRoot/_common.ps1"
Invoke-HydroGradle -GradleArgs @('testDebugUnitTest','lintDebug','lintRelease','assembleDebug','assembleRelease','bundleRelease','--max-workers=1')
Write-Host 'Unsigned release output: app/build/outputs/bundle/release/app-release.aab'
Write-Host 'This is not an owner-signed Play upload. Final policy and signing still require owner action.'
