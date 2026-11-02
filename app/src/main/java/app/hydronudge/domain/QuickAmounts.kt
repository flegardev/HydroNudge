package app.hydronudge.domain

/** A malformed serving must invalidate the whole list, never disappear silently. */
fun parseQuickAmounts(text: String, unit: VolumeUnit): List<Int>? {
    val tokens=text.split(',')
    if(tokens.size!=4) return null
    return tokens.map {token ->
        val value=token.trim().toDoubleOrNull() ?: return null
        if(!value.isFinite() || value<=0 || value>20000/unit.millilitres) return null
        unit.toMl(value).takeIf {it in 1..20000} ?: return null
    }
}
