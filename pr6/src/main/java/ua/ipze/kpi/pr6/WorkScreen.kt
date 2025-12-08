@file:OptIn(ExperimentalMaterial3Api::class)

package ua.ipze.kpi.pr6

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import org.json.JSONObject
@Composable
fun CardListScreen(
    navController: NavController,
    cardViewModel: CalcViewModel = viewModel()

) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.ime)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Калькулятор для розрахунку електричних навантажень об’єктів", style = MaterialTheme.typography.titleLarge)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(Modifier.padding(16.dp)) {

                Text("Характеристики цеху", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(8.dp))

                InputField(
                    label = "n",
                    measurement = "шт",
                    value = cardViewModel.totalNumber,
                    onValueChange = { cardViewModel.totalNumber = it }
                )

                Spacer(Modifier.height(8.dp))

                InputField(
                    label = "n*P",
                    subscript = "н",
                    measurement = "кВт",
                    value = cardViewModel.workshopNominalCapacity,
                    onValueChange = { cardViewModel.workshopNominalCapacity = it }
                )

                Spacer(Modifier.height(8.dp))

                InputField(
                    label = buildAnnotatedString{
                        append("n*P")
                        withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript)) {
                            append("н")
                        }
                        append("K")
                        withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                            append("B")
                        }
                    }.toString(),
                    measurement = "кВт",
                    value = cardViewModel.workshopAverageActiveLoad,
                    onValueChange = {cardViewModel.workshopAverageActiveLoad = it}
                )

                Spacer(Modifier.height(8.dp))

                InputField(
                    label = buildAnnotatedString{
                        append("n*P")
                        withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript)) {
                            append("н")
                        }
                        append("*K")
                        withStyle(style = SpanStyle( baselineShift = BaselineShift.Superscript)) {
                            append("B")
                        }
                        append("*tg φ")
                    }.toString(),
                    measurement = "квар",
                    value = cardViewModel.workshopAverageReactiveLoad,
                    onValueChange = {cardViewModel.workshopAverageReactiveLoad = it}
                )

                InputField(
                    label = "n*P",
                    subscript = "н",
                    superscript = "2",
                    measurement = buildAnnotatedString{
                        append("кВт")
                        withStyle(style = SpanStyle(baselineShift = BaselineShift.Superscript)) {
                            append("2")
                        }
                    }.toString(),
                    value = cardViewModel.totalSquaredPower,
                    onValueChange = { cardViewModel.totalSquaredPower = it }
                )

                Spacer(Modifier.height(8.dp))

                InputField(
                    label = "U",
                    subscript = "н",
                    measurement = "кВ",
                    value = cardViewModel.loadVoltage,
                    onValueChange = { cardViewModel.loadVoltage = it }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        cardViewModel.cards.forEachIndexed { index, card ->
            EpCard(
                cardData = card,
                onRemove = { cardViewModel.removeCard(index) }
            )
        }

        Button(
            onClick = { cardViewModel.addCard() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Додати ЕП")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val totalNumber = cardViewModel.totalNumber.toIntOrNull()?: 0
                val workshopNominalCapacity = cardViewModel.workshopNominalCapacity.replace(",", ".").toDoubleOrNull()?: 0.00
                val workshopAverageActiveLoad = cardViewModel.workshopAverageActiveLoad.replace(",", ".").toDoubleOrNull()?: 0.00
                val workshopAverageReactiveLoad = cardViewModel.workshopAverageReactiveLoad.replace(",", ".").toDoubleOrNull()?: 0.00
                val totalSquaredPower = cardViewModel.totalSquaredPower.replace(",", ".").toDoubleOrNull()?: 0.00
                val loadVoltage = cardViewModel.loadVoltage.replace(",", ".").toDoubleOrNull()?: 0.00
                val cardsValues = cardViewModel.grabValues()

                val result = calculatePowerLoad(
                    cardsValues["name"] as List<String>, cardsValues["nominalEfficiency"] as List<Double>,
                    cardsValues["loadCowerFactor"] as List<Double>, cardsValues["number"] as List<Int>,
                    cardsValues["nominalCapacity"] as List<Double>,cardsValues["utilizationFactor"] as List<Double>,
                    cardsValues["reactivePowerFactor"] as List<Double>, loadVoltage, totalNumber, workshopNominalCapacity,
                    workshopAverageActiveLoad, workshopAverageReactiveLoad, totalSquaredPower
                )

                Log.d("Test",result.toString())
                val jsonString = JSONObject(result).toString()
                Log.d("JsonData", jsonString)
                navController.navigate("result/$jsonString")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Розрахувати")
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun ResultScreen(
    result: Map<String, *>,
    navController: NavController,
) {
    val scrollState = rememberScrollState()

    val groupUtilizationFactor = (result["groupUtilizationFactor"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val effectiveNumber  = (result["effectiveNumber"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val EPCalculationFactor  = (result["EPCalculationFactor"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val estimatedActiveLoad  = (result["estimatedActiveLoad"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val estimatedReactiveLoad  = (result["estimatedReactiveLoad"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val groupFullPower  = (result["groupFullPower"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val groupCurrent  = (result["groupCurrent"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val workshopUtilizationFactor  = (result["workshopUtilizationFactor"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val workshopEffectiveNumber  = (result["workshopEffectiveNumber"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val workshopCalculationFactor  = (result["workshopCalculationFactor"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val estimatedActiveTyreLoad  = (result["estimatedActiveTyreLoad"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val estimatedReactiveTyreLoad  = (result["estimatedReactiveTyreLoad"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val workshopFullPower  = (result["workshopFullPower"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val workshopCurrent  = (result["workshopCurrent"] as? JsonPrimitive)?.doubleOrNull ?: 0.00
    val loadVoltage  = (result["loadVoltage"] as? JsonPrimitive)?.doubleOrNull ?: 0.00

    Log.d("groupUtilizationFactor", groupUtilizationFactor.toString())

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
                    Text (text = "Груповий коефіцієнт використання для ШР1=ШР2=ШР3: ${"%.2f".format(groupUtilizationFactor)}")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Ефективна кількість ЕП для ШР1=ШР2=ШР3: ${"%.2f".format(effectiveNumber)}")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахунковий коефіцієнт активної потужності для ШР1=ШР2=ШР3: ${"%.2f".format(EPCalculationFactor)}")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахункове активне навантаження для ШР1=ШР2=ШР3: ${"%.2f".format(estimatedActiveLoad)} кВт")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахункове реактивне навантаження для ШР1=ШР2=ШР3: ${"%.2f".format(estimatedReactiveLoad)} квар")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Повна потужність для ШР1=ШР2=ШР3: ${"%.2f".format(groupFullPower)} кВ*А")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахунковий груповий струм для ШР1=ШР2=ШР3: ${"%.2f".format(groupCurrent)} А")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Коефіцієнти використання цеху в цілому: ${"%.2f".format(workshopUtilizationFactor)}")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Ефективна кількість ЕП цеху в цілому: ${"%.2f".format(workshopEffectiveNumber)}")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахунковий коефіцієнт активної потужності цеху в цілому: ${"%.2f".format(workshopCalculationFactor)}")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахункове активне навантаження на шинах ${"%.2f".format(loadVoltage)} кВ ТП: ${"%.2f".format(estimatedActiveTyreLoad)} кВт")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахункове реактивне навантаження на шинах ${"%.2f".format(loadVoltage)} кВ ТП: ${"%.2f".format(estimatedReactiveTyreLoad)} квар")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Повна потужність на шинах ${"%.2f".format(loadVoltage)} кВ ТП: ${"%.2f".format(workshopFullPower)} кВ*А")

                    Spacer(Modifier.height(12.dp))

                    Text (text = "Розрахунковий груповий струм на шинах ${"%.2f".format(loadVoltage)} кВ ТП: ${"%.2f".format(workshopCurrent)} А")

                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}