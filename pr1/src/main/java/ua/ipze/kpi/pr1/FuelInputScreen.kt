@file:OptIn(ExperimentalMaterial3Api::class)

package ua.ipze.kpi.pr1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import org.json.JSONObject


@Composable
fun FuelInputScreen(navController: NavController) {
    var carbon by remember { mutableStateOf("") }
    var hydrogen by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var nitrogen by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var ash by remember { mutableStateOf("") }
    var wetness by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Калькулятор палива") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(top = 10.dp, end = 20.dp, start = 20.dp, bottom = 20.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {navController.navigate("input/oil")}
            ) {
                Text("Перейти до Мазут")
            }

            InputField("C", superscript = "P", measurement = "(%)", value = carbon) {
                carbon = it
            }
            InputField("H", superscript = "P", measurement = "(%)", value =hydrogen) {
                hydrogen = it
            }
            InputField("S", superscript = "P", measurement = "(%)", value =sulfur) {
                sulfur = it
            }
            InputField("N", superscript = "P", measurement = "(%)", value =nitrogen) {
                nitrogen = it
            }
            InputField("O", superscript = "P", measurement = "(%)", value =oxygen) {
                oxygen = it
            }
            InputField("W", superscript = "P", measurement = "(%)", value =wetness) {
                wetness = it
            }
            InputField("A", superscript = "P", measurement = "(%)", value =ash) {
                ash = it
            }

            Button(
                onClick = {
                    val c = carbon.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val h = hydrogen.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val o = oxygen.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val s = sulfur.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val n = nitrogen.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val w = wetness.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val a = ash.replace(",", ".").toDoubleOrNull() ?: 0.0

                    if ((c + h + s + n + o + w + a) != 100.0) {
                        scope.launch {
                            val job = launch {
                                snackbarHostState.showSnackbar(
                                    message = "Дані введені неправильно! Сума повинна бути 100%, а дані числами.",
                                    duration = SnackbarDuration.Indefinite)
                            }
                            delay(2500)
                            snackbarHostState.currentSnackbarData?.dismiss()
                            job.cancel()
                        }
                        return@Button
                    }

                    val res = calculate(c, h, o, s, n, w, a)

                    val jsonString = JSONObject(res).toString()
                    navController.navigate("result/fuel/$jsonString")
                }
            ) {
                Text("Обчислити")
            }
        }

    }
}

@Composable
fun ResultScreenFuel(result: Map<String, *>, navController: NavController) {
    val scrollState = rememberScrollState()

    val wetDryCoef = (result["wetDryCoef"] as? JsonPrimitive)?.doubleOrNull
    val wetFireCoef = (result["wetFireCoef"] as? JsonPrimitive)?.doubleOrNull

    val dryValues = result["dryValues"] as Map<*, *>
    val fireValues = result["fireValues"] as Map<*, *>

    val lowBurnTemp = (result["lowBurnTemp"] as? JsonPrimitive)?.doubleOrNull
    val lowBurnDryTemp = (result["lowBurnDryTemp"] as? JsonPrimitive)?.doubleOrNull
    val lowBurnFireTemp = (result["lowBurnFireTemp"] as? JsonPrimitive)?.doubleOrNull


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text (
                text = buildAnnotatedString {
                    append("Коефіцієнт переходу від робочої до сухої маси становить: ${"%.2f".format(wetDryCoef)};")
                }
            )
            Spacer(Modifier.height(12.dp))

            Text (
                text = buildAnnotatedString {
                    append("Коефіцієнт переходу від робочої до горючої маси становить: ${"%.2f".format(wetFireCoef)};")
                }
            )
            Spacer(Modifier.height(12.dp))

            Text (
                text = buildAnnotatedString {
                    append("Склад сухої маси палива становитиме:\n\tC")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("C")
                    }

                    append("= ${"%.2f".format((dryValues["carbon_dry"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tH")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("C")
                    }

                    append("= ${"%.2f".format((dryValues["hydrogen_dry"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tS")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("C")
                    }

                    append("= ${"%.2f".format((dryValues["sulfur_dry"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tN")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("C")
                    }

                    append("= ${"%.2f".format((dryValues["nitrogen_dry"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tO")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("C")
                    }

                    append("= ${"%.2f".format((dryValues["oxygen_dry"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tA")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("C")
                    }
                    append("= ${"%.2f".format((dryValues["ash_dry"] as? JsonPrimitive)?.doubleOrNull)}%;")
                }
            )
            Spacer(Modifier.height(12.dp))

            Text (
                text = buildAnnotatedString {
                    append("Склад горючої маси палива становитиме:\n\tC")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("Г")
                    }

                    append("= ${"%.2f".format((fireValues["carbon_fire"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tH")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("Г")
                    }

                    append("= ${"%.2f".format((fireValues["hydrogen_fire"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tS")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("Г")
                    }

                    append("= ${"%.2f".format((fireValues["sulfur_fire"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tN")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("Г")
                    }

                    append("= ${"%.2f".format((fireValues["nitrogen_fire"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tO")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("Г")
                    }

                    append("= ${"%.2f".format((fireValues["oxygen_fire"] as? JsonPrimitive)?.doubleOrNull)}%;")
                }
            )
            Spacer(Modifier.height(12.dp))

            Text (
                text = buildAnnotatedString {
                    append("Нижча теплота згоряння для робочої маси становить: ${"%.3f".format(lowBurnTemp)} МДж/кг;")
                }
            )
            Spacer(Modifier.height(12.dp))

            Text (
                text = buildAnnotatedString {
                    append("Нижча теплота згоряння для сухої маси становить: ${"%.3f".format(lowBurnDryTemp)} МДж/кг;")
                }
            )
            Spacer(Modifier.height(12.dp))

            Text (
                text = buildAnnotatedString {
                    append("Нижча теплота згоряння для горючої маси за становить: ${"%.3f".format(lowBurnFireTemp)} МДж/кг.")
                }
            )
        }
    }
}



fun calculate(carbon: Double, hydrogen: Double, oxygen: Double, sulfur: Double,
              nitrogen: Double, wetness: Double, ash: Double): Map<String, *>{

    val wetDryCoef = calcDryMassCoef(wetness)
    val wetFireCoef = calcFireMassCoef(wetness, ash)

    val dryValues = calcDryMass(carbon, hydrogen, sulfur, nitrogen, oxygen, ash, wetDryCoef)
    val fireValues = calcFireMass(carbon, hydrogen, sulfur, nitrogen, oxygen, wetFireCoef)

    val lowBurnTemp = calcLowBurnTemp(carbon, hydrogen, oxygen, sulfur, wetness) / 1000.0
    val lowBurnDryTemp = calcLowBurnDryTemp(lowBurnTemp, wetness, wetDryCoef)
    val lowBurnFireTemp = calcLowBurnFireTemp(lowBurnTemp, wetness, wetFireCoef)

    return mapOf(
        "wetDryCoef" to wetDryCoef,
        "wetFireCoef" to wetFireCoef,
        "dryValues" to dryValues,
        "fireValues" to fireValues,
        "lowBurnTemp" to lowBurnTemp,
        "lowBurnDryTemp" to lowBurnDryTemp,
        "lowBurnFireTemp" to lowBurnFireTemp
    )
}

fun calcDryMassCoef(wetness: Double): Double {
    return 100.0 / (100.0 - wetness)
}

fun calcFireMassCoef(wetness: Double, ash: Double): Double {
    return 100.0 / (100.0 - wetness - ash)
}

fun calcDryMass(carbon: Double, hydrogen: Double, sulfur: Double, nitrogen: Double,
    oxygen: Double, ash: Double, wetDryCoef: Double): Map<String, Double> {
    return mapOf(
        "carbon_dry" to carbon * wetDryCoef,
        "hydrogen_dry" to hydrogen * wetDryCoef,
        "sulfur_dry" to sulfur * wetDryCoef,
        "nitrogen_dry" to nitrogen * wetDryCoef,
        "oxygen_dry" to oxygen * wetDryCoef,
        "ash_dry" to ash * wetDryCoef
    )
}

fun calcFireMass(carbon: Double, hydrogen: Double, sulfur: Double, nitrogen: Double,
    oxygen: Double, wetFireCoef: Double): Map<String, Double> {
    return mapOf(
        "carbon_fire" to carbon * wetFireCoef,
        "hydrogen_fire" to hydrogen * wetFireCoef,
        "sulfur_fire" to sulfur * wetFireCoef,
        "nitrogen_fire" to nitrogen * wetFireCoef,
        "oxygen_fire" to oxygen * wetFireCoef
    )
}

fun calcLowBurnTemp(carbon: Double, hydrogen: Double, oxygen: Double,
    sulfur: Double, wetness: Double): Double {
    return 339 * carbon + 1030 * hydrogen - 108.8 * (oxygen - sulfur) - 25 * wetness
}

fun calcLowBurnDryTemp(lowBurnTemp: Double, wetness: Double, wetDryCoef: Double): Double {
    return (lowBurnTemp + 0.025 * wetness) * wetDryCoef
}

fun calcLowBurnFireTemp(lowBurnTemp: Double, wetness: Double, wetFireCoef: Double): Double {
    return (lowBurnTemp + 0.025 * wetness) * wetFireCoef
}


