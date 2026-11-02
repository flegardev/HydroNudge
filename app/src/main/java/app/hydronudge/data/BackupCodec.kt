package app.hydronudge.data

import app.hydronudge.data.local.*
import org.json.JSONArray
import org.json.JSONObject

/** Portable schema. Volumes are integer mL, timestamps UTC epoch milliseconds. IDs survive merges. */
object BackupCodec {
    private fun integer(value: JSONObject, key: String): Long {
        val number = value.get(key)
        require(number is Int || number is Long) { "$key must be an integer." }
        return (number as Number).toLong()
    }
    private fun amount(value: JSONObject): Int {
        val ml = integer(value,"amountMl")
        require(ml in 1L..20000L) { "Invalid backup volume." }
        return ml.toInt()
    }
    fun encode(backup: DrinkBackup): String = JSONObject().apply {
        put("format","HydroNudge"); put("schemaVersion",1)
        put("exportedAt",java.time.Instant.now().toString())
        put("entries",JSONArray().apply { backup.entries.forEach { e -> put(JSONObject().apply {
            put("id",e.id); put("name",e.name); put("category",e.category); put("amountMl",e.amountMl)
            put("timestamp",e.timestamp); put("note",e.note); put("enteredUnit",e.enteredUnit)
        }) } })
        put("favorites",JSONArray().apply { backup.favorites.forEach { f -> put(JSONObject().apply {
            put("id",f.id); put("name",f.name); put("category",f.category); put("amountMl",f.amountMl)
        }) } })
    }.toString(2)
    fun decode(json: String): DrinkBackup {
        val root = JSONObject(json)
        require(root.getString("format") == "HydroNudge" && integer(root,"schemaVersion") == 1L) { "This is not a supported HydroNudge backup." }
        val array = root.getJSONArray("entries")
        val entries = List(array.length()) { i -> array.getJSONObject(i).let { e ->
            DrinkEntry(e.getString("id"),e.getString("name"),e.getString("category"),amount(e),integer(e,"timestamp"),e.optString("note",""),e.optString("enteredUnit","ML"))
        } }
        val favoriteArray = root.optJSONArray("favorites") ?: JSONArray()
        val favorites = List(favoriteArray.length()) { i -> favoriteArray.getJSONObject(i).let { f ->
            FavoriteDrink(f.getString("id"),f.getString("name"),f.getString("category"),amount(f))
        } }
        require(entries.map { it.id }.distinct().size == entries.size && favorites.map { it.id }.distinct().size == favorites.size) { "Backup contains duplicate IDs." }
        return DrinkBackup(entries,favorites)
    }
}
