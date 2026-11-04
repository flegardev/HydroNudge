param(
    [string] $ClassName = 'app.hydronudge.DashboardTest,app.hydronudge.qa.QuickAmountValidationTest',
    [Parameter(Mandatory=$true)][string] $DeviceSerial,
    [switch] $AllowPossibleTargetAppRemoval
)
# Never invoke Gradle connected tests: that task previously removed the phone app.
if ($DeviceSerial -notmatch '^emulator-\d+$') { throw 'Instrumentation is emulator-only. Physical devices are prohibited.' }
& "$PSScriptRoot/redesign-emulator.ps1" -Serial $DeviceSerial -TestClasses $ClassName
