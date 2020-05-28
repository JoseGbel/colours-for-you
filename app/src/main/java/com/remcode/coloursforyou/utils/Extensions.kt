package com.remcode.coloursforyou.utils

import java.lang.IllegalArgumentException
import java.lang.StringBuilder

/**
 * Extension function that converts an integer to a Hexadecimal string to represent an RGB colour
 */
fun Int.toHexColourString() : String {

    var result =  "#${this.toString(16)}"

    if(this < 0 || this > 16777215) throw IllegalArgumentException()

    if (result.count() == 2) {
        result = "#00${this.toString(16)}"
    }
    if (result.count() == 3) {
        result = "#0${this.toString(16)}"
    }
    if (result.count() == 5) {
        result = "#00${this.toString(16)}"
    }
    if (result.count() == 6) {
        result = "#0${this.toString(16)}"
    }

    return result.toUpperCase()
}

fun String.capitalize() : String {
    val sb = StringBuilder()

    if (this == "") throw IllegalArgumentException()

    for (i in this.indices){
        if(i == 0)
            sb.append(this[i].toUpperCase())
        else
            sb.append(this[i])
    }
    return sb.toString()
}