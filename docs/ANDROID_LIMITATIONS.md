# Background behavior and privacy

References reviewed 2026-10-31:

- [WorkManager periodic requests](https://developer.android.com/reference/androidx/work/PeriodicWorkRequest): 15-minute minimum repeat interval; inexact. Doze, app standby, battery saver and vendor policies can delay work. No exact timing promise or exact alarms.
- [Android 14 ongoing-notification changes](https://developer.android.com/about/versions/14/behavior-changes-all#non-dismissable-notifications): ongoing does not guarantee undismissable. Never start a foreground service to fight user dismissal.
- [Broadcast restrictions](https://developer.android.com/develop/background-work/background-tasks/broadcasts) and [exceptions](https://developer.android.com/develop/background-work/background-tasks/broadcasts/broadcast-exceptions): USER_PRESENT is not an exempt manifest broadcast for modern targets. A runtime receiver lasts only while its process lives. Reliable background phone-unlock reminders are therefore not promised or implemented. No accessibility service, usage access, overlay, hidden receiver tricks or full-screen launches.

Closing a task from Recents differs from Android Settings → Force stop. Force stop prevents scheduled background work until the user launches/interacts with the app again. Reboot recovery uses WorkManager and widget host behavior, not a permanently running process.

Optional status notifications, lock-screen visibility under user control, scheduled reminders, widgets and Quick Settings tile are intended alternatives. No foreground service is needed for a status notification.

Status dismissal is remembered locally. Background refresh does not re-post a status that the user dismissed. Settings → Show status again restores it. Permission/channel controls and lock-screen visibility remain under Android’s control.

WorkManager contributes WAKE_LOCK and RECEIVE_BOOT_COMPLETED for scheduling/reboot recovery, plus ACCESS_NETWORK_STATE for its internal constraint tracking. ACCESS_NETWORK_STATE is not Internet access; there are no network-constrained requests or application network calls. WorkManager’s unused foreground service and permission are removed from the manifest. The app also declares POST_NOTIFICATIONS and VIBRATE for optional notifications. AndroidX adds an app-scoped signature permission for private runtime receivers.

Daily totals use local calendar boundaries, including 23/25-hour DST days. Timestamps are epoch instants, regrouped under the device’s current time zone. A zone change can move an entry to a different day. Historical goal comparisons explicitly use the current goal.
