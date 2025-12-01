package ua.ipze.kpi.pr4

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle

@Composable
fun InputField(label: String, superscript: String? = null, subscript: String? = null,
               measurement: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = {Text(
            buildAnnotatedString {
                append(label)
                withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript)) {
                    append(subscript ?: "") // Нижній індекс
                }
                withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                    append(superscript ?: "") // Верхній індекс
                }
                append(" $measurement")

            })
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        modifier = Modifier.fillMaxWidth()

    )
}