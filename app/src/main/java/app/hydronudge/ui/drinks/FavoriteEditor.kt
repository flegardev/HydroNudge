package app.hydronudge.ui.drinks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.hydronudge.data.local.FavoriteDrink
import app.hydronudge.domain.VolumeUnit

@Composable
fun FavoriteEditor(favorite: FavoriteDrink, unit: VolumeUnit, dismiss: ()->Unit, save: (FavoriteDrink)->Unit) {
    var name by rememberSaveable(favorite.id) { mutableStateOf(favorite.name) }
    var amount by rememberSaveable(favorite.id) { mutableStateOf(unit.input(favorite.amountMl)) }
    val ml=amount.replace(',','.').toDoubleOrNull()?.takeIf { it.isFinite() && it>0 && it<=20000/unit.millilitres }?.let(unit::toMl)
    AlertDialog(onDismissRequest=dismiss,title={Text("Your favorite serving")},text={Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(name,{name=it.take(80)},label={Text("Name")},singleLine=true,modifier=Modifier.fillMaxWidth())
        OutlinedTextField(amount,{amount=it},label={Text("Serving (${unit.label})")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),singleLine=true,modifier=Modifier.fillMaxWidth())
    }},confirmButton={TextButton(onClick={save(favorite.copy(name=name.trim(),amountMl=ml!!));dismiss()},enabled=name.isNotBlank() && ml != null && ml in 1..20000) {Text("Save favorite")}},dismissButton={TextButton(onClick=dismiss) {Text("Cancel")}})
}
