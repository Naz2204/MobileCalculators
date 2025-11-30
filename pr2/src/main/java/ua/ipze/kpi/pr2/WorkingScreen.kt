@file:OptIn(ExperimentalMaterial3Api::class)

package ua.ipze.kpi.pr2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow


@Composable
fun WorkingScreen() {

    var selectedFuelType by remember { mutableStateOf(0) }

    // Text fields state
    var Qi by remember { mutableStateOf("") }
    var A_w by remember { mutableStateOf("") }
    var A_r by remember { mutableStateOf("") }
    var G_w by remember { mutableStateOf("") }
    var n_z by remember { mutableStateOf("") }
    var k_TBS by remember { mutableStateOf("") }
    var fuelMass by remember { mutableStateOf("") }

    var k_TV by remember { mutableStateOf("") }
    var E_TV by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    val scrollState = rememberScrollState()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Калькулятор валового викиду палива", textAlign = TextAlign.Center)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Виберіть тип палива")
                    Spacer(Modifier.width(20.dp))
                    DropdownSelector(
                        items = listOf("Користувацький ввід", "Донецьке газове вугілля марки ГР",
                            "Високосірчистий мазут марки 40", "Природний газ із газопроводу Уренгой-Ужгород"),
                        selected = selectedFuelType,
                        onSelectedChange = { selectedFuelType = it }
                    )
                }
            }

            if (selectedFuelType == 0) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        InputField(label = "Q", subscript = "i", superscript = "r", measurement = "МДж/кг", value = Qi, onValueChange = { Qi = it })

                        InputField(label = "a", subscript = "вин", measurement = "", value = A_w, onValueChange = { A_w = it })

                        InputField(label = "A", subscript = "r", measurement = "%", value = A_r, onValueChange = { A_r = it })

                        InputField(label = "Г", subscript = "вин", measurement = "%", value = G_w, onValueChange = { G_w = it })

                        InputField(label = "η", subscript = "зу", measurement = "", value = n_z, onValueChange = { n_z = it })

                        InputField(label = "k", subscript = "твS", measurement = "г/ГДж", value = k_TBS, onValueChange = { k_TBS = it })
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    InputField(label = "Маса палива", measurement = "т", value = fuelMass, onValueChange = { fuelMass = it })
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val result: Pair<Double, Double>?
                    when (selectedFuelType) {
                        0 -> result = calculateUserInput(
                            fuelMass.toDoubleOrNull(), Qi.toDoubleOrNull(),
                            A_w.toDoubleOrNull(), A_r.toDoubleOrNull(), G_w.toDoubleOrNull(),
                            n_z.toDoubleOrNull(), k_TBS.toDoubleOrNull(),
                            notice = {message -> noticeMassage(scope, snackbarHostState, message)},
                        )
                        1 -> result = calculateCoal(fuelMass.toDoubleOrNull(),
                            notice = {message -> noticeMassage(scope, snackbarHostState, message)})
                        2 -> result = calculateMazut(fuelMass.toDoubleOrNull(),
                            notice = {message -> noticeMassage(scope, snackbarHostState, message)})
                        3 -> result = calculateGas(fuelMass.toDoubleOrNull(),
                            notice = {message -> noticeMassage(scope, snackbarHostState, message)})
                        else -> {
                            noticeMassage(scope, snackbarHostState, "Невідомий тип палива")
                            return@Button
                        }
                    }
                    if (result == null) {
                        return@Button
                    }
                    else {
                        k_TV = result.first.toString()
                        E_TV = result.second.toString()
                    }
                }
            ) {
                Text("Розрахувати")
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text (
                            text = buildAnnotatedString {
                                append("k")
                                withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript)) {
                                    append("TB")
                                }
                                if (k_TV.toDoubleOrNull() == null) {
                                    append("")
                                }
                                else {
                                    append("\t\t\t${"%.2f".format(k_TV.toDoubleOrNull())} ")
                                }
                            }
                        )
                        Text(text = "г/ГДж;")
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text (
                            text = buildAnnotatedString {
                                append("E")
                                withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript)) {
                                    append("TB")
                                }
                                if (E_TV.toDoubleOrNull() == null) {
                                    append("")
                                }
                                else {
                                    append("\t\t\t${"%.2f".format(E_TV.toDoubleOrNull())}")
                                }

                            }
                        )
                        Text ("т;")
                    }

                }
            }
        }
    }

}

fun noticeMassage(scope: CoroutineScope, snackbarHostState: SnackbarHostState, message: String) {
    scope.launch {
        val job = launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite)
        }
        delay(2500)
        snackbarHostState.currentSnackbarData?.dismiss()
        job.cancel()
    }
}

fun calculateCoal(mass: Double?, notice: ((String) -> Unit)): Pair<Double, Double>?{
    val lowBurnTemp = 20.47
    val ashPart = 0.8
    val ashMass = 25.2
    val burnableEjection = 1.5
    val cleaningEfficiency = 0.985
    val solidEmissionWithSulfur = 0.0

    if (mass == null) {
        notice("Потрібно заповнити поле маси")
        return null
    }
    if (mass < 0.0) {
        notice("Було введене від'ємне значення")
        return null
    }

    val emission = calculateEmission(lowBurnTemp, ashPart, ashMass, burnableEjection, cleaningEfficiency, solidEmissionWithSulfur)
    val ejection = calculateEjection(emission, lowBurnTemp, mass)

    return Pair(emission, ejection)
}

fun calculateMazut(mass: Double?, notice: ((String) -> Unit)): Pair<Double, Double>? {
    val lowBurnTemp = 39.48
    val ashPart = 1.0
    val ashMass = 0.15
    val burnableEjection = 0.0
    val cleaningEfficiency = 0.985
    val solidEmissionWithSulfur = 0.0

    if (mass == null) {
        notice("Потрібно заповнити поле маси")
        return null
    }
    if (mass < 0.0) {
        notice("Було введене від'ємне значення")
        return null
    }

    val emission = calculateEmission(lowBurnTemp, ashPart, ashMass, burnableEjection, cleaningEfficiency, solidEmissionWithSulfur)
    val ejection = calculateEjection(emission, lowBurnTemp, mass)

    return Pair(emission, ejection)
}

fun calculateGas(mass: Double?, notice: ((String) -> Unit)): Pair<Double, Double>? {
    val lowBurnTemp = 33.08
    val ashPart = 0.0
    val ashMass = 0.0
    val burnableEjection = 0.0
    val cleaningEfficiency = 0.985
    val solidEmissionWithSulfur = 0.0

    if (mass == null) {
        notice("Потрібно заповнити поле маси")
        return null
    }
    if (mass < 0.0) {
        notice("Було введене від'ємне значення")
        return null
    }

    val emission = calculateEmission(lowBurnTemp, ashPart, ashMass, burnableEjection, cleaningEfficiency, solidEmissionWithSulfur)
    val ejection = calculateEjection(emission, lowBurnTemp, mass)

    return Pair(emission, ejection)
}

fun calculateUserInput(mass: Double?, lowBurnTemp: Double?, ashPart: Double?, ashMass: Double?, burnableEjection: Double?,
    cleaningEfficiency: Double?, solidEmissionWithSulfur: Double?, notice: ((String) -> Unit)): Pair<Double, Double>? {

    val inputs = listOf(
        mass, lowBurnTemp, ashPart, ashMass,
        burnableEjection, cleaningEfficiency, solidEmissionWithSulfur
    )

    if (inputs.any { it == null }) {
        notice("Потрібно заповнити поля")
        return null
    }

    if (inputs.any { it!! < 0.0 }) {
        notice("Було введене від'ємне значення")
        return null
    }

    val emission = calculateEmission(lowBurnTemp!!, ashPart!!, ashMass!!, burnableEjection!!, cleaningEfficiency!!, solidEmissionWithSulfur!!)

    val ejection = calculateEjection(emission, lowBurnTemp, mass!!)

    return Pair(emission, ejection)

}

fun calculateEmission(lowBurnTemp: Double, ashPart: Double, ashMass: Double, burnableEjection: Double, cleaningEfficiency: Double, solidEmissionWithSulfur: Double): Double {
    return (10.0.pow(6.0) / lowBurnTemp) * ashPart * (ashMass / (100.0 - burnableEjection)) * (1.0 - cleaningEfficiency) + solidEmissionWithSulfur
}

fun calculateEjection(emission: Double, lowBurnTemp: Double, mass: Double): Double {
    return 10.0.pow(-6.0) * emission * lowBurnTemp * mass
}

