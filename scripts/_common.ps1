$ErrorActionPreference = 'Stop'
$script:ProjectRoot = Split-Path -Parent $PSScriptRoot
$script:GradleWrapper = Join-Path $script:ProjectRoot 'gradlew.bat'

if (-not (Test-Path -LiteralPath $script:GradleWrapper)) {
    throw "Gradle Wrapper not found: $script:GradleWrapper"
}

if (-not $env:JAVA_HOME -or -not (Test-Path -LiteralPath (Join-Path $env:JAVA_HOME 'bin\java.exe'))) {
    $studioJbr = Join-Path $env:ProgramFiles 'Android\Android Studio\jbr'
    if (Test-Path -LiteralPath (Join-Path $studioJbr 'bin\java.exe')) {
        $env:JAVA_HOME = $studioJbr
    } else {
        throw 'Set JAVA_HOME to a compatible JDK (Android Studio bundled JBR is recommended).'
    }
}

if (-not $env:ANDROID_HOME -or -not (Test-Path -LiteralPath (Join-Path $env:ANDROID_HOME 'platform-tools\adb.exe'))) {
    $sdkDefault = Join-Path $env:LOCALAPPDATA 'Android\Sdk'
    if (Test-Path -LiteralPath (Join-Path $sdkDefault 'platform-tools\adb.exe')) {
        $env:ANDROID_HOME = $sdkDefault
    } else {
        throw 'Android SDK Platform-Tools not found. Set ANDROID_HOME to the installed SDK directory.'
    }
}

$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
$env:PATH = "$(Join-Path $env:JAVA_HOME 'bin');$(Join-Path $env:ANDROID_HOME 'platform-tools');$env:PATH"
Set-Location -LiteralPath $script:ProjectRoot

function Invoke-HydroGradle([string[]] $GradleArgs) {
    & $script:GradleWrapper @('--no-configuration-cache', '--console', 'plain') @GradleArgs
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}
