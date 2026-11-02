. (Join-Path $PSScriptRoot '_common.ps1')
Invoke-HydroGradle @(':app:testDebugUnitTest')
