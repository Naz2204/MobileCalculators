package ua.ipze.kpi.pr6

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun InputField(label: String, superscript: String? = null, subscript: String? = null,
               measurement: String, value: String, isString: Boolean = false,
               onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = {Text(
            buildAnnotatedString {
                append(label)
                withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript)) {
                    append(subscript ?: "")
                }
                withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                    append(superscript ?: "")
                }
                append(" $measurement")

            })
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (!isString) KeyboardType.Number else KeyboardType.Text
        ),
        modifier = Modifier.fillMaxWidth()

    )
}


@Composable
fun EpCard(
    cardData: CardData,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Характеристики ЕП", style = MaterialTheme.typography.titleMedium)

            InputField(
                label = "Назва ЕП",
                measurement = "",
                value = cardData.name,
                isString = true,
                onValueChange = {cardData.name = it}
            )

            Spacer(Modifier.height(8.dp))

            InputField(
                label = "η",
                subscript = "н",
                measurement = "",
                value = cardData.nominalEfficiency,
                onValueChange = { cardData.nominalEfficiency = it.replace(",", ".") }
            )

            Spacer(Modifier.height(8.dp))

            InputField(
                label = "cos φ",
                measurement = "",
                value = cardData.loadCowerFactor,
                onValueChange = { cardData.loadCowerFactor = it.replace(",", ".") }
            )

            Spacer(Modifier.height(8.dp))

            InputField(
                label = "n",
                measurement = "шт",
                value = cardData.number,
                onValueChange = { cardData.number = it }
            )

            Spacer(Modifier.height(8.dp))

            InputField(
                label = "P",
                superscript = "н",
                measurement = "кВт",
                value = cardData.nominalCapacity,
                onValueChange = { cardData.nominalCapacity = it.replace(",", ".") }
            )

            Spacer(Modifier.height(8.dp))

            InputField(
                label = "К",
                superscript = "В",
                measurement = "",
                value = cardData.utilizationFactor,
                onValueChange = { cardData.utilizationFactor = it.replace(",", ".") }
            )

            Spacer(Modifier.height(8.dp))

            InputField(
                label = "tg φ",
                measurement = "",
                value = cardData.reactivePowerFactor,
                onValueChange = { cardData.reactivePowerFactor = it.replace(",", ".") }
            )

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onRemove) {
                    Text("Видалити")
                }
            }
        }
    }
}