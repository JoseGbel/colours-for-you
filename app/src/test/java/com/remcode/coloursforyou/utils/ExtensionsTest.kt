package com.remcode.coloursforyou.utils

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.IllegalArgumentException

@RunWith(JUnit4::class)
class ExtensionsTest {

    @Test
    fun shouldConvertOneDigitIntegerToHexColourString_edgeCase1() {
        val expected = "#000"

        // given
        val colour = 0

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertTinyIntegerToThreeDigitsHexColourString_edgeCase2() {
        val expected = "#009"

        // given
        val colour = 9

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertTinyIntegerToThreeDigitsHexColourString_edgeCase1() {
        val expected = "#00A"

        // given
        val colour = 10

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertVerySmallIntegerToThreeDigitsHexColourString_edgeCase2() {
        val expected = "#010"

        // given
        val colour = 16

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToThreeDigitsHexColourString_edgeCase1() {
        val expected = "#100"

        // given
        val colour = 256

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToThreeDigitsHexColourString_edgeCase2() {
        val expected = "#FFF"

        // given
        val colour = 4095

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToSixDigitsHexColourString_edgeCase1() {
        val expected = "#001000"

        // given
        val colour = 4096

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToSixDigitsHexColourString_edgeCase2() {
        val expected = "#00FFFF"

        // given
        val colour = 65535

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertMediumIntegerToSixDigitsHexColourString_edgeCase1() {
        val expected = "#010000"

        // given
        val colour = 65536

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertMediumIntegerToSixDigitsHexColourString_edgeCase2() {
        val expected = "#0FFFFF"

        // given
        val colour = 1048575

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertLargeIntegerToSixDigitsHexColourString_edgeCase1() {
        val expected = "#100000"

        // given
        val colour = 1048576

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertLargeIntegerToSixDigitsHexColourString_edgeCase2() {
        val expected = "#FFFFFF"

        // given
        val colour = 16777215

        // when
        val actual = colour.toHexColourString()

        // then
        assertEquals(expected, actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowExceptionWhenIntegerIsNegativeNumber() {
        // given
        val actual = -1

        actual.toHexColourString()
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowExceptionWhenIntegerIsTooBigForAColour() {
        // given
        val actual = 16777216

        actual.toHexColourString()
    }

    @Test
    fun shouldCapitalizeFirstLetterInTheString() {
        val expected = "Happy"

        // given
        val testWord = "happy"

        // when
        val actual = testWord.capitalize()

        // then
        assertEquals(expected, actual)
    }

    @Test (expected = IllegalArgumentException::class)
    fun shouldThrowException_WhenCapitalizeIsCalledWithEmptyString() {
        // given
        val testWord = ""

        // when
        testWord.capitalize()
    }
}