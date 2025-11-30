@file:OptIn(ExperimentalMaterial3Api::class)

package ua.ipze.kpi.pr3

import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import org.json.JSONObject


@Composable
fun InputScreen(navController: NavController) {
    var sig1 by remember { mutableStateOf("") }
    var sig2 by remember { mutableStateOf("") }
    var P_s by remember { mutableStateOf("") }
    var B by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Калькулятор прибутку") })
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
            InputField("σ", subscript = "1", measurement = "МВт", value = sig1) {
                sig1 = it
            }
            InputField("σ", subscript = "2", measurement = "МВт", value = sig2) {
                sig2 = it
            }
            InputField("P", subscript = "С", measurement = "МВт", value = P_s) {
                P_s = it
            }
            InputField("B", measurement = "грн/кВт*год", value = B) {
                B = it
            }

            Button(
                onClick = {
                    val vsig1 = sig1.replace(",", ".").toDoubleOrNull()?: 0.0
                    val vsig2 = sig2.replace(",", ".").toDoubleOrNull()?: 0.0
                    val vP_s = P_s.replace(",", ".").toDoubleOrNull()?: 0.0
                    val vB = B.replace(",", ".").toDoubleOrNull()?: 0.0

                    val res = calculate(vsig1, vsig2, vP_s, vB)
                    Log.d("Test",res.toString())
                    val jsonString = JSONObject(res).toString()
                    navController.navigate("result/$jsonString")
                }
            ) {
                Text("Обчислити")
            }
        }

    }
}

@Composable
fun ResultScreen(result: Map<String, *>, navController: NavController) {
    val scrollState = rememberScrollState()

    val deltaW1 = (result["deltaW1"] as? JsonPrimitive)?.doubleOrNull ?: 0.0
    val P1 = (result["P1"] as? JsonPrimitive)?.intOrNull ?: 0
    val H1 = (result["H1"] as? JsonPrimitive)?.intOrNull ?: 0
    val deltaW2 = (result["deltaW2"] as? JsonPrimitive)?.doubleOrNull ?: 0.0
    val P2 = (result["P2"] as? JsonPrimitive)?.intOrNull ?: 0
    val H2 = (result["H2"] as? JsonPrimitive)?.intOrNull ?: 0


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
            Card() {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text (
                        text = "Станція з стандартною системою прогнозування сонячної потужності",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))


                    Text (
                        text = buildAnnotatedString {
                            append("Частка електроенегрії, що генерується без небалансів: ${"%d".format((deltaW1 * 100).toInt())}%;")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Прибуток електростанції без врахування штрафів: ${"%d".format(P1)} тис. грн;")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Штраф: ${"%d".format(H1)} тис. грн;")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Чистий прибуток електростанції: ${"%d".format(P1-H1)} тис. грн;")
                        }
                    )
                }

            }

            Spacer(Modifier.height(12.dp))

            Card(
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text (
                        text = "Станція з вдосконаленою системою прогнозування сонячної потужності",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Частка електроенегрії, що генерується без небалансів: ${"%d".format((deltaW2 * 100).toInt())}%;")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Прибуток електростанції без врахування штрафів: ${"%d".format(P2)} тис. грн;")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Штраф: ${"%d".format(H2)} тис. грн;")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Чистий прибуток електростанції: ${"%d".format(P2-H2)} тис. грн;")
                        }
                    )
                }
            }
        }
    }
}

fun calculate(sig1: Double, sig2: Double, P_s: Double, B: Double): Map<String, *> {
    val delta = 0.05
    val pMin = P_s - P_s * delta
    val pMax = P_s + P_s * delta

    val deltaW1 = integrate(pMin, pMax, sig1, P_s)
    val W1 = kotlin.math.round(P_s * 24 * deltaW1).toInt()
    val P1 = kotlin.math.round(W1 * B).toInt()
    val W2 = kotlin.math.round(P_s * 24 * (1 - deltaW1)).toInt()
    val H1 = kotlin.math.round(W2 * B).toInt()

    val deltaW2 = integrate(pMin, pMax, sig2, P_s)
    val W3 = kotlin.math.round(P_s * 24 * deltaW2).toInt()
    val P2 = kotlin.math.round(W3 * B).toInt()
    val W4 = kotlin.math.round(P_s * 24 * (1 - deltaW2)).toInt()
    val H2 = kotlin.math.round(W4 * B).toInt()

    return mapOf(
        "deltaW1" to deltaW1,
        "P1" to P1,
        "H1" to H1,
        "deltaW2" to deltaW2,
        "P2" to P2,
        "H2" to H2
    )
}

fun integrate(pStart: Double, pEnd: Double, sig: Double, P_s: Double): Double {

    fun toIntegrate(sig: Double, P_s: Double, p: Double): Double {
        val dif = p - P_s
        val power = (dif * dif) / (2 * sig * sig)
        val numerator = kotlin.math.exp(power)
        val denominator = sig * kotlin.math.sqrt(6.28)
        return numerator / denominator
    }

    var sum = 0.5 * toIntegrate(sig, P_s, pStart) + 0.5 * toIntegrate(sig, P_s, pEnd)
    val iterations = 1000
    val delta = (pEnd - pStart) / iterations

    for (i in 1..<iterations) {
        sum += toIntegrate(sig, P_s, pStart + i * delta)
    }

    sum *= delta

    return if(sum.isNaN()) {
        0.0
    }
    else {
        sum
    }
}


