@file:OptIn(ExperimentalMaterial3Api::class)

package ua.ipze.kpi.pr5

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import org.json.JSONObject
import kotlin.math.ceil

@Composable
fun InputScreen(navController: NavController) {
    var w_os by remember { mutableStateOf("") }
    var t_w_os by remember { mutableStateOf("") }

    var Z_p_a by remember { mutableStateOf("") }
    var Z_p_p by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Scaffold(
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
            Card() {
                Column(
                    Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Порівняння надійності",
                        style = MaterialTheme.typography.titleMedium
                    )
                    InputField("ω", subscript = "ос", measurement = buildAnnotatedString {
                        append("рік")
                        withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript)) {
                            append("-1")
                        } }.toString(), value = w_os) {
                        w_os = it
                    }
                    InputField("t", subscript = "в.ос", measurement = "год", value = t_w_os) {
                        t_w_os = it
                    }
                }
            }

            Card() {
                Column(
                    Modifier.padding(20.dp)
                ) {
                    Text (
                        text = "Розрахунок збитків",
                        style = MaterialTheme.typography.titleMedium
                    )
                    InputField("Z", subscript = "пер.а", measurement = "грн/кВт*год", value = Z_p_a) {
                        Z_p_a = it
                    }
                    InputField("Z", subscript = "пер.п", measurement = "грн/кВт*год", value = Z_p_p) {
                        Z_p_p = it
                    }
                }
            }

            Button(
                onClick = {
                    val w_os = w_os.replace(",", ".").toDoubleOrNull()?: 0.00
                    val t_w_os = t_w_os.replace(",", ".").toDoubleOrNull()?: 0.00
                    val Z_p_a = Z_p_a.replace(",", ".").toDoubleOrNull()?: 0.00
                    val Z_p_p = Z_p_p.replace(",", ".").toDoubleOrNull()?: 0.00

                    val w_ds = calcTask1(w_os, t_w_os)
                    val M_Z_p = calcTask2(Z_p_a, Z_p_p)

                    var compare = "";
                    if (w_os == w_ds) {
                        compare = "Надійність одноколової і двоколової систем рівна";
                    }
                    else if (w_os > w_ds) {
                        compare = "Надійність двоколової системи вища ніж одноколової";
                    }
                    else {
                        compare = "Надійність одноколової системи вища ніж двоколової";
                    }

                    val res = mapOf(
                        "w_os" to w_os,
                        "w_ds" to w_ds,
                        "M_Z_p" to M_Z_p,
                        "compare" to compare
                    )

                    Log.d("Test",res.toString())
                    val jsonString = JSONObject(res).toString()
                    Log.d("JsonData", jsonString)
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

    val w_os = (result["w_os"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val w_ds = (result["w_ds"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val M_Z_p = (result["M_Z_p"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val compare = (result["compare"] as? JsonPrimitive)?.content ?: ""

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
            Card(
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text (
                        text = "Порівняння надійності",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text (
                        text = buildAnnotatedString {
                            append("Частота відмов одноколової системи ")
                            append("${"%.3f".format(w_os)} рік")
                            withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                                append("-1")
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Частота відмов двоколової системи з секційним перемикачем ")
                            append("${"%.5f".format(w_ds)} рік")
                            withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                                append("-1")
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (text = compare)
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = "Розрахунок збитків від перерв електропостачання",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text (
                        text = buildAnnotatedString {
                            append("Математичне сподівання збитків від переривання електропостачання у разі застосування однотрансформаторної ГПП: ${"%.2f".format(M_Z_p)} грн")
                        }
                    )
                }
            }
        }
    }
}

fun calcTask1(w_oc: Double, t_w_oc: Double): Double{
    val k_p_max = 43
    val w_sv = 0.02


    val k_aoc = (w_oc * t_w_oc / 8760).safeFormat(5)
    val k_poc = (1.2 * k_p_max / 8760).safeFormat(5)

    val w_dk = (2 * w_oc * (k_aoc + k_poc)).safeFormat(5)
    val w_ds = w_sv + w_dk

    return w_ds
}

fun calcTask2(Z_p_a: Double, Z_p_p: Double): Double{
    val omega = 0.01
    val t_v = 0.045
    val k_p = 0.004
    val P_m = 5120
    val T_m = 6451

    val M_W_n_a = ceil(omega * t_v * P_m * T_m)
    val M_W_n_p = ceil(k_p * P_m * T_m)
    val M_Z_p = ceil((Z_p_a * M_W_n_a) + (Z_p_p * M_W_n_p))

    return M_Z_p
}

fun Double.safeFormat(dec_points: Int = 2): Double =
    String.format("%.${dec_points}f", this).replace(",", ".").toDoubleOrNull() ?: 0.0
