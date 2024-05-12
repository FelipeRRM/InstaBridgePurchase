package com.feliperrm.instabridge

import java.util.Locale


fun <T> List<T>.findClosestIndexNotAbove(target: T): Int where T : Number, T : Comparable<T> {
    var closestIndex = -1
    var closestDifference = when (target) {
        is Int -> Int.MAX_VALUE as T
        is Long -> Long.MAX_VALUE as T
        is Float -> Float.MAX_VALUE as T
        is Double -> Double.MAX_VALUE as T
        is Short -> Short.MAX_VALUE as T
        is Byte -> Byte.MAX_VALUE as T
        else -> throw IllegalArgumentException("Unsupported numeric type")
    }

    val minDiference = when (target) {
        is Int -> 0 as T
        is Long -> 0L as T
        is Float -> 0f as T
        is Double -> 0.0 as T
        is Short -> 0.toShort() as T
        is Byte -> 0.toByte() as T
        else -> throw IllegalArgumentException("Unsupported numeric type")
    }

    for (i in indices) {
        val difference = subtract(target, this[i])
        if (difference >= minDiference && difference < closestDifference) {
            closestIndex = i
            closestDifference = difference
        }
    }

    return closestIndex
}

fun <T> subtract(a: T, b: T): T where T : Number, T : Comparable<T> {
    return when (a) {
        is Int -> (a.toInt() - b.toInt()) as T
        is Long -> (a.toLong() - b.toLong()) as T
        is Float -> (a.toFloat() - b.toFloat()) as T
        is Double -> (a.toDouble() - b.toDouble()) as T
        is Short -> (a.toShort() - b.toShort()) as T
        is Byte -> (a.toByte() - b.toByte()) as T
        else -> throw IllegalArgumentException("Unsupported numeric type")
    }
}

fun Float.formatWithOneDecimal() = "%.1f".format(this).removeSuffix(".0").removeSuffix(",0")

fun countryCodeToEmojiFlag(countryCode: String): String {
    return countryCode
        .uppercase(Locale.US)
        .map { char ->
            Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6
        }
        .map { codePoint ->
            Character.toChars(codePoint)
        }
        .joinToString(separator = "") { charArray ->
            String(charArray)
        }
}