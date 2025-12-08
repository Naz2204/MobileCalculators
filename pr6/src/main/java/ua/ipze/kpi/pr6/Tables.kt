package ua.ipze.kpi.pr6

fun getCalculationFactor(effectiveNumber: Int, utilizationFactor: Double): Double {
    return when (effectiveNumber) {
        1 -> when (utilizationFactor) {
            0.1 -> 8.00
            0.15 -> 5.33
            0.2 -> 4.00
            0.3 -> 2.67
            0.4 -> 2.00
            0.5 -> 1.60
            0.6 -> 1.33
            0.7 -> 1.14
            0.8 -> 1.0
            else -> 1.0
        }

        2 -> when (utilizationFactor) {
            0.1 -> 6.22
            0.15 -> 4.33
            0.2 -> 3.39
            0.3 -> 2.45
            0.4 -> 1.98
            0.5 -> 1.60
            0.6 -> 1.33
            0.7 -> 1.14
            0.8 -> 1.0
            else -> 1.0
        }

        3 -> when (utilizationFactor) {
            0.1 -> 4.06
            0.15 -> 2.89
            0.2 -> 2.31
            0.3 -> 1.74
            0.4 -> 1.45
            0.5 -> 1.34
            0.6 -> 1.22
            0.7 -> 1.14
            0.8 -> 1.0
            else -> 1.0
        }

        4 -> when (utilizationFactor) {
            0.1 -> 3.24
            0.15 -> 2.35
            0.2 -> 1.91
            0.3 -> 1.47
            0.4 -> 1.25
            0.5 -> 1.21
            0.6 -> 1.12
            0.7 -> 1.06
            0.8 -> 1.0
            else -> 1.0
        }

        5 -> when (utilizationFactor) {
            0.1 -> 2.84
            0.15 -> 2.09
            0.2 -> 1.72
            0.3 -> 1.35
            0.4 -> 1.16
            0.5 -> 1.16
            0.6 -> 1.08
            0.7 -> 1.03
            0.8 -> 1.0
            else -> 1.0
        }

        6 -> when (utilizationFactor) {
            0.1 -> 2.64
            0.15 -> 1.96
            0.2 -> 1.62
            0.3 -> 1.28
            0.4 -> 1.14
            0.5 -> 1.13
            0.6 -> 1.06
            0.7 -> 1.01
            0.8 -> 1.0
            else -> 1.0
        }

        7 -> when (utilizationFactor) {
            0.1 -> 2.49
            0.15 -> 1.86
            0.2 -> 1.54
            0.3 -> 1.23
            0.4 -> 1.12
            0.5 -> 1.10
            0.6 -> 1.04
            else -> 1.0
        }

        8 -> when (utilizationFactor) {
            0.1 -> 2.37
            0.15 -> 1.78
            0.2 -> 1.48
            0.3 -> 1.19
            0.4 -> 1.10
            0.5 -> 1.08
            0.6 -> 1.02
            else -> 1.0
        }

        9 -> when (utilizationFactor) {
            0.1 -> 2.27
            0.15 -> 1.71
            0.2 -> 1.43
            0.3 -> 1.16
            0.4 -> 1.09
            0.5 -> 1.07
            0.6 -> 1.01
            else -> 1.0
        }

        10 -> when (utilizationFactor) {
            0.1 -> 2.18
            0.15 -> 1.65
            0.2 -> 1.39
            0.3 -> 1.13
            0.4 -> 1.07
            0.5 -> 1.05
            else -> 1.0
        }

        11 -> when (utilizationFactor) {
            0.1 -> (2.18 + 2.04) / 2
            0.15 -> (1.65 + 1.56) / 2
            0.2 -> (1.39 + 1.32) / 2
            0.3 -> (1.13 + 1.08) / 2
            0.4 -> (1.07 + 1.05) / 2
            0.5 -> (1.05 + 1.03) / 2
            else -> 1.0
        }

        12 -> when (utilizationFactor) {
            0.1 -> 2.04
            0.15 -> 1.56
            0.2 -> 1.32
            0.3 -> 1.08
            0.4 -> 1.05
            0.5 -> 1.03
            else -> 1.0
        }

        13 -> when (utilizationFactor) {
            0.1 -> (2.04 + 1.94) / 2
            0.15 -> (1.56 + 1.49) / 2
            0.2 -> (1.32 + 1.27) / 2
            0.3 -> (1.08 + 1.05) / 2
            0.4 -> (1.05 + 1.02) / 2
            0.5 -> (1.03 + 1.00) / 2
            else -> 1.0
        }

        14 -> when (utilizationFactor) {
            0.1 -> 1.94
            0.15 -> 1.49
            0.2 -> 1.27
            0.3 -> 1.05
            0.4 -> 1.02
            else -> 1.0
        }

        15 -> when (utilizationFactor) {
            0.1 -> (1.94 + 1.85) / 2
            0.15 -> (1.49 + 1.43) / 2
            0.2 -> (1.27 + 1.23) / 2
            0.3 -> (1.05 + 1.02) / 2
            0.4 -> (1.02 + 1.00) / 2
            else -> 1.0
        }

        16 -> when (utilizationFactor) {
            0.1 -> 1.85
            0.15 -> 1.43
            0.2 -> 1.23
            0.3 -> 1.02
            else -> 1.0
        }

        17 -> when (utilizationFactor) {
            0.1 -> (1.85 + 1.78) / 2
            0.15 -> (1.43 + 1.39) / 2
            0.2 -> (1.23 + 1.19) / 2
            0.3 -> (1.02 + 1.00) / 2
            else -> 1.0
        }

        18 -> when (utilizationFactor) {
            0.1 -> 1.78
            0.15 -> 1.39
            0.2 -> 1.19
            else -> 1.0
        }

        19 -> when (utilizationFactor) {
            0.1 -> (1.78 + 1.72) / 2
            0.15 -> (1.39 + 1.35) / 2
            0.2 -> (1.19 + 1.16) / 2
            else -> 1.0
        }

        20 -> when (utilizationFactor) {
            0.1 -> 1.72
            0.15 -> 1.35
            0.2 -> 1.16
            else -> 1.0
        }

        in 21..24 -> when (utilizationFactor) {
            0.1 -> (1.72 + 1.60) / 2
            0.15 -> (1.35 + 1.27) / 2
            0.2 -> (1.16 + 1.10) / 2
            else -> 1.0
        }

        25 -> when (utilizationFactor) {
            0.1 -> 1.60
            0.15 -> 1.27
            0.2 -> 1.10
            else -> 1.0
        }

        in 26..29 -> when (utilizationFactor) {
            0.1 -> (1.60 + 1.51) / 2
            0.15 -> (1.27 + 1.21) / 2
            0.2 -> (1.10 + 1.05) / 2
            else -> 1.0
        }

        30 -> when (utilizationFactor) {
            0.1 -> 1.51
            0.15 -> 1.21
            0.2 -> 1.05
            else -> 1.0
        }

        in 31..34 -> when (utilizationFactor) {
            0.1 -> (1.51 + 1.44) / 2
            0.15 -> (1.21 + 1.16) / 2
            0.2 -> (1.05 + 1.00) / 2
            else -> 1.0
        }

        35 -> when (utilizationFactor) {
            0.1 -> 1.44
            0.15 -> 1.16
            else -> 1.0
        }

        in 36..39 -> when (utilizationFactor) {
            0.1 -> (1.44 + 1.40) / 2
            0.15 -> (1.16 + 1.13) / 2
            else -> 1.0
        }

        40 -> when (utilizationFactor) {
            0.1 -> 1.40
            0.15 -> 1.13
            else -> 1.0
        }

        in 41..49 -> when (utilizationFactor) {
            0.1 -> (1.40 + 1.30) / 2
            0.15 -> (1.13 + 1.07) / 2
            else -> 1.0
        }

        50 -> when (utilizationFactor) {
            0.1 -> 1.30
            0.15 -> 1.07
            else -> 1.0
        }

        in 51..59 -> when (utilizationFactor) {
            0.1 -> (1.30 + 1.25) / 2
            0.15 -> (1.07 + 1.03) / 2
            else -> 1.0
        }

        60 -> when (utilizationFactor) {
            0.1 -> 1.25
            0.15 -> 1.03
            else -> 1.0
        }

        in 61..79 -> when (utilizationFactor) {
            0.1 -> (1.25 + 1.16) / 2
            0.15 -> (1.03 + 1.00) / 2
            else -> 1.0
        }

        80 -> if (utilizationFactor == 0.1) 1.16 else 1.0

        in 81..99 -> when (utilizationFactor) {
            0.1 -> (1.16 + 1.00) / 2
            else -> 1.0
        }

        else -> 1.0
    }
}

fun getAllCalculationFactor(effectiveNumber: Int, utilizationFactor: Double): Double {
    return when (effectiveNumber) {
        1 -> when (utilizationFactor) {
            0.1 -> 8.00
            0.15 -> 5.33
            0.2 -> 4.00
            0.3 -> 2.67
            0.4 -> 2.00
            0.5 -> 1.60
            0.6 -> 1.33
            0.7 -> 1.14
            else -> 1.14
        }

        2 -> when (utilizationFactor) {
            0.1 -> 5.01
            0.15 -> 3.44
            0.2 -> 2.69
            0.3 -> 1.90
            0.4 -> 1.52
            0.5 -> 1.24
            0.6 -> 1.11
            0.7 -> 1.00
            else -> 1.0
        }

        3 -> when (utilizationFactor) {
            0.1 -> 2.4
            0.15 -> 2.17
            0.2 -> 1.8
            0.3 -> 1.42
            0.4 -> 1.23
            0.5 -> 1.14
            0.6 -> 1.08
            0.7 -> 1.00
            else -> 1.0
        }

        4 -> when (utilizationFactor) {
            0.1 -> 2.28
            0.15 -> 1.73
            0.2 -> 1.46
            0.3 -> 1.19
            0.4 -> 1.06
            0.5 -> 1.04
            0.6 -> 1.00
            0.7 -> 0.97
            else -> 0.97
        }

        5 -> when (utilizationFactor) {
            0.1 -> 1.31
            0.15 -> 1.12
            0.2 -> 1.02
            0.3 -> 1.00
            0.4 -> 0.98
            0.5 -> 0.96
            0.6 -> 0.94
            0.7 -> 0.93
            else -> 0.93
        }

        in 6..8 -> when (utilizationFactor) {
            0.1 -> 1.2
            0.15 -> 1.0
            0.2 -> 0.96
            0.3 -> 0.95
            0.4 -> 0.94
            0.5 -> 0.93
            0.6 -> 0.92
            0.7 -> 0.91
            else -> 0.91
        }

        in 9..10 -> when (utilizationFactor) {
            0.1 -> 1.1
            0.15 -> 0.97
            0.2 -> 0.91
            0.3 -> 0.90
            else -> 0.90
        }

        in 11..25 -> when (utilizationFactor) {
            0.1 -> 0.8
            0.15 -> 0.8
            0.2 -> 0.8
            0.3 -> 0.85
            0.4 -> 0.85
            0.5 -> 0.85
            0.6 -> 0.9
            0.7 -> 0.9
            else -> 0.9
        }

        in 26..50 -> when (utilizationFactor) {
            0.1 -> 0.75
            0.15 -> 0.75
            0.2 -> 0.75
            0.3 -> 0.75
            0.4 -> 0.75
            0.5 -> 0.80
            0.6 -> 0.85
            0.7 -> 0.85
            else -> 0.85
        }

        else -> when (utilizationFactor) {
            0.1 -> 0.65
            0.15 -> 0.65
            0.2 -> 0.65
            0.3 -> 0.70
            0.4 -> 0.70
            0.5 -> 0.75
            0.6 -> 0.80
            0.7 -> 0.80
            else -> 0.80
        }
    }
}
