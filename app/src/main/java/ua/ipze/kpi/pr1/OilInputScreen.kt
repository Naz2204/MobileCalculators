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
fun OilInputScreen(navController: NavController) {
    var carbon by remember { mutableStateOf("") }
    var hydrogen by remember { mutableStateOf("") }
    var sulfur by remember { mutableStateOf("") }
    var oxygen by remember { mutableStateOf("") }
    var ashDry by remember { mutableStateOf("") }
    var wetness by remember { mutableStateOf("") }
    var lowBurnFireTemp by remember { mutableStateOf("") }
    var vanadiumDry by remember { mutableStateOf("") }


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Калькулятор") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InputField("C", superscript = "Г", measurement = "(%)", value = carbon) {
                carbon = it
            }
            InputField("H", superscript = "Г", measurement = "(%)", value = hydrogen) {
                hydrogen = it
            }
            InputField("S", superscript = "Г", measurement = "(%)", value = sulfur) {
                sulfur = it
            }
            InputField("V", superscript = "С", measurement = "(%)", value = vanadiumDry) {
                vanadiumDry = it
            }
            InputField("O", superscript = "Г", measurement = "(%)", value = oxygen) {
                oxygen = it
            }
            InputField("W", superscript = "P", measurement = "(%)", value = wetness) {
                wetness = it
            }
            InputField("A", superscript = "С", measurement = "(%)", value = ashDry) {
                ashDry = it
            }
            InputField("Q", superscript = "daf", subscript = "i", measurement = "(%)",
                value = lowBurnFireTemp) {
                lowBurnFireTemp = it
            }

            Button(
                onClick = {
                    val c = carbon.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val h = hydrogen.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val o = oxygen.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val s = sulfur.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val v = vanadiumDry.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val w = wetness.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val a = ashDry.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val q = lowBurnFireTemp.replace(",", ".").toDoubleOrNull() ?: 0.0

                    if ((c + h + s + o) != 100.0) {
                        scope.launch {
                            val job = launch {
                                snackbarHostState.showSnackbar(
                                    message = "Дані введені неправильно! Сума вуглецю, водню, кисню та сірки повинна бути 100%, а дані числами.",
                                    duration = SnackbarDuration.Indefinite)
                            }
                            delay(2500)
                            snackbarHostState.currentSnackbarData?.dismiss()
                            job.cancel()
                        }
                        return@Button
                    }

                    val res = calcFuelOil(c, h, s, o, a, w, v)

                    val jsonString = JSONObject(res).toString()
                    navController.navigate("result/oil/$jsonString")
                }
            ) {
                Text("Обчислити")
            }
        }

    }
}

@Composable
fun ResultScreenOil(result: Map<String, *>, navController: NavController) {
    val scrollState = rememberScrollState()

    val ashWork = (result["ashWork"] as? JsonPrimitive)?.doubleOrNull
    val vanadiumWork = (result["vanadiumWork"] as? JsonPrimitive)?.doubleOrNull
    val burnWorkTemp = (result["burnWorkTemp"] as? JsonPrimitive)?.doubleOrNull

    val workMass = result["workMass"] as Map<*, *>


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
                    append("Склад робочої маси мазуту становитиме:\n\tC")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("P")
                    }

                    append("= ${"%.2f".format((workMass["carbon_work"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tH")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("P")
                    }

                    append("= ${"%.2f".format((workMass["hydrogen_work"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tS")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("P")
                    }

                    append("= ${"%.2f".format((workMass["sulfur_work"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tO")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("P")
                    }

                    append("= ${"%.2f".format((workMass["oxygen_work"] as? JsonPrimitive)?.doubleOrNull)}%,\n\tA")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("P")
                    }

                    append("= ${"%.2f".format(ashWork)}%,\n\tV")
                    withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                        append("P")
                    }

                    append("= ${"%.2f".format(vanadiumWork)} мг/кг;")
                }
            )
            Spacer(Modifier.height(12.dp))

            Text (
                text = buildAnnotatedString {
                    append("Нижча теплота згоряння мазуту на робочу масу для робочої маси становить: ${"%.3f".format(burnWorkTemp)} МДж/кг.")
                }
            )
        }
    }
}

fun calcWorkMass(carbonFire: Double, hydrogenFire: Double, sulfurFire: Double,
                 oxygenFire: Double, fireToWorkMassCoef: Double): Map<String, Double> {
    val workMass = mutableMapOf<String, Double>()
    workMass["carbon_work"] = carbonFire * fireToWorkMassCoef
    workMass["hydrogen_work"] = hydrogenFire * fireToWorkMassCoef
    workMass["sulfur_work"] = sulfurFire * fireToWorkMassCoef
    workMass["oxygen_work"] = oxygenFire * fireToWorkMassCoef
    return workMass
}

fun calcFireToWorkMassCoef(wetness: Double, ash: Double): Double {
    return (100 - wetness - ash) / 100
}

fun calcBurnWorkTemp(wetness: Double, ashWork: Double): Double {
    return ((100 - wetness - ashWork) / 100) - 0.025 * wetness
}

fun calcFuelOil(carbon: Double, hydrogen: Double, sulfur: Double,
    oxygen: Double, ash: Double, wetness: Double, vanadium: Double): Map<String, *> {

    val ashWork = ash * ((100 - wetness) / 100)
    val vanadiumWork = vanadium * ((100 - wetness) / 100)

    val fireToWorkMassCoef = calcFireToWorkMassCoef(wetness, ashWork)
    val workMass = calcWorkMass(carbon, hydrogen, sulfur, oxygen, fireToWorkMassCoef)

    val burnWorkTemp = calcBurnWorkTemp(wetness, ashWork)

    return mapOf(
        "ashWork" to ashWork,
        "vanadiumWork" to vanadiumWork,
        "workMass" to workMass,
        "burnWorkTemp" to burnWorkTemp
    )
}