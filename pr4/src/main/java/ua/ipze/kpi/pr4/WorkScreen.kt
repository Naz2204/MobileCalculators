@file:OptIn(ExperimentalMaterial3Api::class)

package ua.ipze.kpi.pr4

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
import kotlin.math.pow
import kotlin.math.sqrt


@Composable
fun InputScreen(navController: NavController) {
    var S_m by remember { mutableStateOf("") }
    var U_nom by remember { mutableStateOf("") }
    var I_k by remember { mutableStateOf("") }
    var t_f by remember { mutableStateOf("") }

    var S_k by remember { mutableStateOf("") }
    var U_cn by remember { mutableStateOf("") }
    var U_k by remember { mutableStateOf("") }
    var S_nom_t by remember { mutableStateOf("") }

    var l by remember { mutableStateOf("") }
    var U_k_max by remember { mutableStateOf("") }
    var U_vn by remember { mutableStateOf("") }
    var U_nn by remember { mutableStateOf("") }
    var R_sn by remember { mutableStateOf("") }
    var X_sn by remember { mutableStateOf("") }
    var R_s_min by remember { mutableStateOf("") }
    var X_s_min by remember { mutableStateOf("") }

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
                        text = "Розрахунок перерізу кабеля",
                        style = MaterialTheme.typography.titleMedium
                    )
                    InputField("S", subscript = "м", measurement = "кВ*А", value = S_m) {
                        S_m = it
                    }
                    InputField("U", subscript = "ном", measurement = "кВ", value = U_nom) {
                        U_nom = it
                    }
                    InputField("I", subscript = "k", measurement = "кА", value = I_k) {
                        I_k = it
                    }
                    InputField("t", subscript = "ф", measurement = "с", value = t_f) {
                        t_f = it
                    }
                }
            }
            Card() {
                Column(
                    Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Розрахунок струму трифазного КЗ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    InputField("S", subscript = "к", measurement = "МВ*А", value = S_k) {
                        S_k = it
                    }
                    InputField("U", subscript = "с.н", measurement = "кВ", value = U_cn) {
                        U_cn = it
                    }
                    InputField("U", subscript = "к%", measurement = "%", value = U_k) {
                        U_k = it
                    }
                    InputField("S", subscript = "ном.т", measurement = "МВ*А", value = S_nom_t) {
                        S_nom_t = it
                    }
                }
            }

            Card() {
                Column(
                    Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Розрахунок струмів для ХПнЕМ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    InputField("l", measurement = "км", value = l) {
                        l = it
                    }
                    InputField("U", subscript = "к.max", measurement = "%", value = U_k_max) {
                        U_k_max = it
                    }
                    InputField("U", subscript = "в.н", measurement = "кВ", value = U_vn) {
                        U_vn = it
                    }
                    InputField("U", subscript = "н.н", measurement = "кВ", value = U_nn) {
                        U_nn = it
                    }
                    InputField("R", subscript = "с.н", measurement = "Ом", value = R_sn) {
                        R_sn = it
                    }
                    InputField("X", subscript = "с.н", measurement = "Ом", value = X_sn) {
                        X_sn = it
                    }
                    InputField("R", subscript = "c.min", measurement = "Ом", value = R_s_min) {
                        R_s_min = it
                    }
                    InputField("X", subscript = "c.min", measurement = "Ом", value = X_s_min) {
                        X_s_min = it
                    }
                }
            }

            Button(
                onClick = {
                    val S_m = S_m.replace(",", ".").toDoubleOrNull()?: 0.00
                    val U_nom = U_nom.replace(",", ".").toDoubleOrNull()?: 0.00
                    val I_k = I_k.replace(",", ".").toDoubleOrNull()?: 0.00
                    val t_f = t_f.replace(",", ".").toDoubleOrNull()?: 0.00

                    val S_k = S_k.replace(",", ".").toDoubleOrNull()?: 0.00
                    val U_cn = U_cn.replace(",", ".").toDoubleOrNull()?: 0.00
                    val U_k = U_k.replace(",", ".").toDoubleOrNull()?: 0.00
                    val S_nom_t = S_nom_t.replace(",", ".").toDoubleOrNull()?: 0.00

                    val l = l.replace(",", ".").toDoubleOrNull()?: 0.00
                    val U_k_max = U_k_max.replace(",", ".").toDoubleOrNull()?: 0.00
                    val U_vn = U_vn.replace(",", ".").toDoubleOrNull()?: 0.00
                    val U_nn = U_nn.replace(",", ".").toDoubleOrNull()?: 0.00
                    val R_sn = R_sn.replace(",", ".").toDoubleOrNull()?: 0.00
                    val X_sn = X_sn.replace(",", ".").toDoubleOrNull()?: 0.00
                    val R_s_min = R_s_min.replace(",", ".").toDoubleOrNull()?: 0.00
                    val X_s_min = X_s_min.replace(",", ".").toDoubleOrNull()?: 0.00

                    val cable = chooseCable(S_m, U_nom, I_k, t_f)
                    val kz = calculateKZ(S_k, U_cn, U_k, S_nom_t)
                    val stationKz = calculateKZonStation(l, U_k_max, U_vn, U_nn, S_nom_t, R_sn, X_sn, R_s_min, X_s_min)

                    val res = mapOf(
                        "cable" to cable.toList(),
                        "kz" to kz,
                        "stationKz" to stationKz.map { it.toList() }.toList()
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

    val cable: List<Double> = (result["cable"] as? List<*>)?.map {
        it.toString().toDoubleOrNull() ?: 0.00
    } ?: listOf()
    val kz = (result["kz"] as? JsonPrimitive)?.doubleOrNull ?: 0.00

    Log.d("Debug", kz.toString())
    Log.d("Debug", cable.toString())

    val stationKz: List<List<Double>> = (result["stationKz"] as? List<*>)?.map { row ->
        (row as? List<*>)?.map { it.toString().toDoubleOrNull() ?: 0.00 } ?: listOf()
    } ?: listOf()

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
                        text = buildAnnotatedString {
                            append("Економічний переріз броньованого кабеля з паперовою ізоляцією в алюмінієвій оболонці типу ААБ ")
                            append("${"%.2f".format(cable[1])} мм")
                            withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                                append("2")
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Оптимальний переріз броньованого кабеля з паперовою ізоляцією в алюмінієвій оболонці типу ААБ ")
                            append("${"%.2f".format(cable[0])} мм")
                            withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                                append("2")
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Початкове діюче значення струму трифазного КЗ: ${"%.2f".format(kz)} кА")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм трифазного КЗ на шинах 10 кВ у нормальному режимі: ${"%.2f".format(stationKz[0][0])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм двофазного КЗ на шинах 10 кВ у нормальному режимі: ${"%.2f".format(stationKz[0][1])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм трифазного КЗ на шинах 10 кВ у мінімальному режимі: ${"%.2f".format(stationKz[0][2])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм двофазного КЗ на шинах 10 кВ у мінімальному режимі: ${"%.2f".format(stationKz[0][3])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Дійсний струм трифазного КЗ на шинах 10 кВ у нормальному режимі: ${"%.2f".format(stationKz[1][0])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Дійсний струм двофазного КЗ на шинах 10 кВ у нормальному режимі: ${"%.2f".format(stationKz[1][1])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Дійсний струм трифазного КЗ на шинах 10 кВ у мінімальному режимі: ${"%.2f".format(stationKz[1][2])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Дійсний струм двофазного КЗ на шинах 10 кВ у мінімальному режимі: ${"%.2f".format(stationKz[1][3])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм трифазного КЗ на ділянці з найвищим опором у нормальному режимі: ${"%.2f".format(stationKz[2][0])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм двофазного КЗ на ділянці з найвищим опором у нормальному режимі: ${"%.2f".format(stationKz[2][1])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм трифазного КЗ на ділянці з найвищим опором у мінімальному режимі: ${"%.2f".format(stationKz[2][2])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    Text (
                        text = buildAnnotatedString {
                            append("Струм двофазного КЗ на ділянці з найвищим опором у мінімальному режимі: ${"%.2f".format(stationKz[2][3])} А")
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

fun chooseCable(S_m: Double, U_nom: Double, I_k: Double, t_f: Double): Pair<Double, Double> {
    val j_ek = 1.4;
    val C_t = 92;

    val I_m = ((S_m / 2) / (sqrt(3.0) * U_nom)).safeFormat()
    val I_m_pa = (2 * I_m).safeFormat()
    var S_ek = (I_m / j_ek).safeFormat()
    var S = ((I_k * sqrt(t_f)) / C_t).safeFormat()

    if(S.isNaN()){
        S = 0.00
    }
    if(S_ek.isNaN()){
        S_ek = 0.00
    }

    return Pair(S, S_ek)
}

fun calculateKZ(S_k: Double, U_cn: Double, U_k: Double, S_nom_t: Double): Double {

    val X_c = (U_cn.pow(2) / S_k).safeFormat()
    val X_t = ((U_k / 100) * (U_cn.pow(2) / S_nom_t)).safeFormat()
    val X_sum = (X_c + X_t).safeFormat()
    var I_p0 = (U_cn / (sqrt(3.0) * X_sum)).safeFormat()

    if(I_p0.isNaN()){
        I_p0 = 0.00
    }

    return I_p0
}

fun calculateKZonStation(l: Double, U_k_max: Double, U_vn: Double, U_nn: Double, S_nom_t: Double,
                         R_sn: Double, X_sn: Double, R_s_min: Double, X_s_min: Double): Array<Array<Double>> {
    val R0 = 0.64
    val X0 = 0.363

    val X_t = ((U_k_max * U_vn.pow(2)) / (100 * S_nom_t)).safeFormat()

    val R_h = R_sn
    val X_h = (X_sn + X_t).safeFormat()
    val Z_h = sqrt(R_h.pow(2) + X_h.pow(2)).safeFormat()

    val R_h_min = R_s_min
    val X_h_min = (X_s_min + X_t).safeFormat()
    val Z_h_min = sqrt(R_h_min.pow(2) + X_h_min.pow(2)).safeFormat()

    val I_h3 = ((U_vn * 1e3) / (sqrt(3.0) * Z_h)).safeFormat()
    val I_h2 = (I_h3 * (sqrt(3.0) / 2)).safeFormat()
    val I_h3_min = ((U_vn * 1e3) / (sqrt(3.0) * Z_h_min)).safeFormat()
    val I_h2_min = (I_h3_min * (sqrt(3.0) / 2)).safeFormat()

    val k_pr = (U_nn.pow(2) / U_vn.pow(2)).safeFormat(3)

    val R_h_n = (R_h * k_pr).safeFormat()
    val X_h_n = (X_h * k_pr).safeFormat()
    val Z_h_n = sqrt(R_h_n.pow(2) + X_h_n.pow(2)).safeFormat()

    val R_h_n_min = (R_h_min * k_pr).safeFormat()
    val X_h_n_min = (X_h_min * k_pr).safeFormat()
    val Z_h_n_min = sqrt(R_h_n_min.pow(2) + X_h_n_min.pow(2)).safeFormat()

    val I_h_n3 = ((U_nn * 1e3) / (sqrt(3.0) * Z_h_n)).safeFormat()
    val I_h_n2 = (I_h_n3 * (sqrt(3.0) / 2)).safeFormat()
    val I_h_n3_min = ((U_nn * 1e3) / (sqrt(3.0) * Z_h_n_min)).safeFormat()
    val I_h_n2_min = (I_h_n3_min * (sqrt(3.0) / 2)).safeFormat()

    val R_l = (l * R0).safeFormat()
    val X_l = (l * X0).safeFormat()

    val R_sum_n = (R_l + R_h_n).safeFormat()
    val X_sum_n = (X_l + X_h_n).safeFormat()
    val Z_sum_n = sqrt(R_sum_n.pow(2) + X_sum_n.pow(2)).safeFormat()

    val R_sum_n_min = (R_l + R_h_n_min).safeFormat()
    val X_sum_n_min = (X_l + X_h_n_min).safeFormat()
    val Z_sum_n_min = sqrt(R_sum_n_min.pow(2) + X_sum_n_min.pow(2)).safeFormat()

    val I_l_n3 = ((U_nn * 1e3) / (sqrt(3.0) * Z_sum_n)).safeFormat()
    val I_l_n2 = (I_l_n3 * (sqrt(3.0) / 2)).safeFormat()
    val I_l_n3_min = ((U_nn * 1e3) / (sqrt(3.0) * Z_sum_n_min)).safeFormat()
    val I_l_n2_min = (I_l_n3_min * (sqrt(3.0) / 2)).safeFormat()

    val result = arrayOf(
        arrayOf(I_h3, I_h2, I_h3_min, I_h2_min),
        arrayOf(I_h_n3, I_h_n2, I_h_n3_min, I_h_n2_min),
        arrayOf(I_l_n3, I_l_n2, I_l_n3_min, I_l_n2_min)
    )

    for (i in 0..<result.size) {
        for (j in 0..<result[i].size) {
            if(result[i][j].isNaN()){
                result[i][j] = 0.00
            }
        }
    }

    return result
}

fun Double.safeFormat(dec_points: Int = 2): Double =
    String.format("%.${dec_points}f", this).replace(",", ".").toDoubleOrNull() ?: 0.0
