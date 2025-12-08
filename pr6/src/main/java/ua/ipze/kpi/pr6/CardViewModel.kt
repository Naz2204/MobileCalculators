package ua.ipze.kpi.pr6

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CardData(
    initialName: String = "",
    initialNominalEfficiency: String = "",
    initialLoadCowerFactor: String = "",
    initialNumber: String = "",
    initialNominalCapacity: String = "",
    initialUtilizationFactor: String = "",
    initialReactivePowerFactor: String = "",
) {
    var name by mutableStateOf(initialName)
    var nominalEfficiency by mutableStateOf(initialNominalEfficiency)
    var loadCowerFactor by mutableStateOf(initialLoadCowerFactor)
    var number by mutableStateOf(initialNumber)
    var nominalCapacity by mutableStateOf(initialNominalCapacity)
    var utilizationFactor by mutableStateOf(initialUtilizationFactor)
    var reactivePowerFactor by mutableStateOf(initialReactivePowerFactor)
}

class CalcViewModel : ViewModel() {

    var cards = mutableStateListOf<CardData>()
        private set

    var totalNumber by mutableStateOf("")
    var workshopNominalCapacity by mutableStateOf("")
    var workshopAverageActiveLoad by mutableStateOf("")
    var workshopAverageReactiveLoad by mutableStateOf("")
    var totalSquaredPower by mutableStateOf("")
    var loadVoltage by mutableStateOf("")

    fun addCard() {
        cards.add(CardData())
    }

    fun removeCard(index: Int) {
        if (index in cards.indices) cards.removeAt(index)
    }

    fun grabValues(): Map<String, List<*>>{
        var results = mapOf(
            "name" to cards.map { it.name },
            "nominalEfficiency" to cards.map { it.nominalEfficiency.toDoubleOrNull()?: 0.0 },
            "loadCowerFactor" to cards.map { it.loadCowerFactor.toDoubleOrNull()?: 0.0 },
            "number" to cards.map { it.number.toIntOrNull()?: 0 },
            "nominalCapacity" to cards.map { it.nominalCapacity.toDoubleOrNull()?: 0.0 },
            "utilizationFactor" to cards.map { it.utilizationFactor.toDoubleOrNull()?: 0.0 },
            "reactivePowerFactor" to cards.map { it.reactivePowerFactor.toDoubleOrNull()?: 0.0 },
        )
        return results
    }
}

