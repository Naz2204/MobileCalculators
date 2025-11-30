package ua.ipze.kpi.pr2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle

@Composable
fun InputField(label: String, superscript: String? = null, subscript: String? = null,
               measurement: String, value: String, onValueChange: (String) -> Unit,
               enabled: Boolean = true) {
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
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    )
}

@Composable
fun DropdownSelector(
    items: List<String>,
    selected: Int,
    onSelectedChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
        ) {
            Text(text = items[selected])
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Випадаюче меню",
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {Text(text = item)},
                    onClick = {
                        onSelectedChange(index)
                        expanded = false
                    })
            }
        }
    }
}
