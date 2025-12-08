package ua.ipze.kpi.pr6

import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

fun calculatePowerLoad(
    name: List<String>,
    nominalEfficiency: List<Double>,
    loadCowerFactor: List<Double>,
    number: List<Int>,
    nominalCapacity: List<Double>,
    utilizationFactor: List<Double>,
    reactivePowerFactor: List<Double>,
    loadVoltage: Double,
    totalNumber: Int,
    workshopNominalCapacity: Double,
    workshopAverageActiveLoad: Double,
    workshopAverageReactiveLoad: Double,
    totalSquaredPower: Double
): Map<String, Double> {
    val generalNominalCapacity = mutableListOf<Double>()
    val generalCalculatedCurrent = mutableListOf<Double>()
    for (i in number.indices) {
        generalNominalCapacity.add(calcAllNominalCapacity(number[i], nominalCapacity[i]))
        generalCalculatedCurrent.add(
            calculatedCurrent(
                generalNominalCapacity[i],
                loadVoltage,
                loadCowerFactor[i],
                nominalEfficiency[i]
            )
        )
    }

    val groupUtilizationFactor = calcGroupUtilizationFactor(generalNominalCapacity, utilizationFactor)
        .round(2)
    val effectiveNumber = calcGroupEffectiveNumber(number, nominalCapacity).round(2)
    val EPCalculationFactor = if (groupUtilizationFactor == 0.15) {
        getCalculationFactor(effectiveNumber.toInt(), groupUtilizationFactor)
    } else {
        getCalculationFactor(effectiveNumber.toInt(), groupUtilizationFactor.round(1))
    }
    val estimatedActiveLoad = calcEstimatedActiveLoad(generalNominalCapacity, utilizationFactor, EPCalculationFactor)
        .round(2)
    val estimatedReactiveLoad = calcEstimatedReactiveLoad(
        effectiveNumber,
        generalNominalCapacity,
        utilizationFactor,
        EPCalculationFactor,
        reactivePowerFactor
    ).round(2)
    val groupFullPower = calcFullPower(estimatedActiveLoad, estimatedReactiveLoad).round(2)
    val groupCurrent = calcGroupCurrent(estimatedActiveLoad, loadVoltage).round(2)

    val workshopUtilizationFactor = calcWorkshopUtilizationFactor(workshopNominalCapacity, workshopAverageActiveLoad)
        .round(2)
    val workshopEffectiveNumber = calcWorkshopEffectiveNumber(workshopNominalCapacity, totalSquaredPower).round(2)
    val workshopCalculationFactor = if (workshopUtilizationFactor == 0.15) {
        getAllCalculationFactor(workshopEffectiveNumber.toInt(), workshopUtilizationFactor)
    } else {
        getAllCalculationFactor(workshopEffectiveNumber.toInt(), workshopUtilizationFactor.round(1))
    }
    val estimatedActiveTyreLoad = calcEstimatedActiveTyreLoad(workshopCalculationFactor, workshopAverageActiveLoad)
        .round(2)
    val estimatedReactiveTyreLoad = calcEstimatedReactiveTyreLoad(workshopCalculationFactor, workshopAverageReactiveLoad)
        .round(2)
    val workshopFullPower = calcTyreFullPower(estimatedActiveTyreLoad, estimatedReactiveTyreLoad).round(2)
    val workshopCurrent = calcTyreCurrent(estimatedActiveTyreLoad, loadVoltage).round(2)

    return mapOf(
        "groupUtilizationFactor" to (groupUtilizationFactor.takeUnless { it.isNaN() }?: 0.0),
        "effectiveNumber" to (effectiveNumber.takeUnless { it.isNaN() }?: 0.0),
        "EPCalculationFactor" to (EPCalculationFactor.takeUnless { it.isNaN() }?: 0.0),
        "estimatedActiveLoad" to (estimatedActiveLoad.takeUnless { it.isNaN() }?: 0.0),
        "estimatedReactiveLoad" to (estimatedReactiveLoad.takeUnless { it.isNaN() }?: 0.0),
        "groupFullPower" to (groupFullPower.takeUnless { it.isNaN() }?: 0.0),
        "groupCurrent" to (groupCurrent.takeUnless { it.isNaN() }?: 0.0),
        "workshopUtilizationFactor" to (workshopUtilizationFactor.takeUnless { it.isNaN() }?: 0.0),
        "workshopEffectiveNumber" to (workshopEffectiveNumber.takeUnless { it.isNaN() }?: 0.0),
        "workshopCalculationFactor" to (workshopCalculationFactor.takeUnless { it.isNaN() }?: 0.0),
        "estimatedActiveTyreLoad" to (estimatedActiveTyreLoad.takeUnless { it.isNaN() }?: 0.0),
        "estimatedReactiveTyreLoad" to (estimatedReactiveTyreLoad.takeUnless { it.isNaN() }?: 0.0),
        "workshopFullPower" to (workshopFullPower.takeUnless { it.isNaN() }?: 0.0),
        "workshopCurrent" to (workshopCurrent.takeUnless { it.isNaN() }?: 0.0),
        "loadVoltage" to (loadVoltage.takeUnless { it.isNaN() }?: 0.0)
    )
}

private fun Double.round(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return round(this * factor) / factor
}

fun calcAllNominalCapacity(number: Int, nominalCapacity: Double): Double {
    return number * nominalCapacity
}

fun calculatedCurrent(allNominalCapacity: Double, loadVoltage: Double, loadCowerFactor: Double, nominalEfficiency: Double): Double {
    return allNominalCapacity / (sqrt(3.0) * loadVoltage * loadCowerFactor * nominalEfficiency)
}

fun calcGroupUtilizationFactor(generalNominalCapacity: List<Double>, utilizationFactor: List<Double>): Double {
    var numerator = 0.0
    var denominator = 0.0
    for (i in utilizationFactor.indices) {
        numerator += generalNominalCapacity[i] * utilizationFactor[i]
        denominator += generalNominalCapacity[i]
    }
    return numerator / denominator
}

fun calcGroupEffectiveNumber(number: List<Int>, nominalCapacity: List<Double>): Double {
    var numerator = 0.0
    var denominator = 0.0
    for (i in number.indices) {
        numerator += number[i] * nominalCapacity[i]
        denominator += number[i] * nominalCapacity[i].pow(2)
    }
    return ceil(numerator.pow(2) / denominator)
}

fun calcEstimatedActiveLoad(generalNominalCapacity: List<Double>, utilizationFactor: List<Double>, calculationFactor: Double): Double {
    var secondOperand = 0.0
    for (i in generalNominalCapacity.indices) {
        secondOperand += generalNominalCapacity[i] * utilizationFactor[i]
    }
    return calculationFactor * secondOperand
}

fun calcEstimatedReactiveLoad(
    effectiveNumber: Double, generalNominalCapacity: List<Double>,
    utilizationFactor: List<Double>, calculationFactor: Double, reactivePowerFactor: List<Double>
): Double {
    var secondOperand = 0.0
    for (i in generalNominalCapacity.indices) {
        secondOperand += generalNominalCapacity[i] * utilizationFactor[i] * reactivePowerFactor[i]
    }
    return if (effectiveNumber <= 10) {
        1.1 * secondOperand
    } else {
        secondOperand
    }
}

fun calcFullPower(activeLoad: Double, reactiveLoad: Double): Double {
    val underRoot = activeLoad.pow(2) + reactiveLoad.pow(2)
    return sqrt(underRoot)
}

fun calcGroupCurrent(activeLoad: Double, loadVoltage: Double): Double {
    return activeLoad / loadVoltage
}

fun calcWorkshopUtilizationFactor(allWorkshopNominalCapacity: Double, allWorkshopAverageActiveLoad: Double): Double {
    return allWorkshopAverageActiveLoad / allWorkshopNominalCapacity
}

fun calcWorkshopEffectiveNumber(allWorkshopNominalCapacity: Double, allSquaredWorkshopNominalCapacity: Double): Double {
    return ceil(allWorkshopNominalCapacity.pow(2) / allSquaredWorkshopNominalCapacity)
}

fun calcEstimatedActiveTyreLoad(calculationFactor: Double, allWorkshopAverageActiveLoad: Double): Double {
    return calculationFactor * allWorkshopAverageActiveLoad
}

fun calcEstimatedReactiveTyreLoad(calculationFactor: Double, allWorkshopAverageActiveLoadTg: Double): Double {
    return calculationFactor * allWorkshopAverageActiveLoadTg
}

fun calcTyreFullPower(estimatedActiveTyreLoad: Double, estimatedReactiveTyreLoad: Double): Double {
    val underRoot = estimatedActiveTyreLoad.pow(2) + estimatedReactiveTyreLoad.pow(2)
    return sqrt(underRoot)
}

fun calcTyreCurrent(estimatedActiveTyreLoad: Double, loadTireVoltage: Double): Double {
    return estimatedActiveTyreLoad / loadTireVoltage
}