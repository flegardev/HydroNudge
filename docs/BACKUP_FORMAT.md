# Drink backup schema 1

Export/import use Android's document picker. No storage permission is requested. The user chooses the destination/provider, which may store files in the cloud independently of HydroNudge.

JSON root fields:

- `format`: `HydroNudge`
- `schemaVersion`: integer `1`
- `exportedAt`: ISO 8601 UTC string, informational
- `entries`: array containing `id`, `name`, `category` (enum name), `amountMl` (positive integer), `timestamp` (UTC epoch milliseconds), `note`, `enteredUnit` (`ML` or `US_OZ`)
- `favorites`: array containing `id`, `name`, `category`, `amountMl`

Volumes are canonical integer mL. US fluid ounces use 29.5735295625 mL per ounce and round once when entered. Prepared drinks are counted once. Categories do not discount recorded volume. Timestamps represent instants; local day assignment uses the current device zone.

Import validates the whole file, then merges drinks and favorites in a single Room transaction. Existing IDs are retained unchanged. Importing the same backup again does not duplicate records. Unsupported schemas, malformed values and duplicate IDs within a file are rejected. Files over 32 MiB are rejected to avoid exhausting device memory during JSON parsing; the error leaves local data unchanged. Preferences are not part of drink backups.

The app database is schema version 1 with exported schema under `app/schemas`. Future schema changes must include explicit, tested Room migrations. Destructive migration fallbacks are intentionally absent.
