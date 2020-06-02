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
        // Given
        val colour = 0
        val expected = "#000"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertTinyIntegerToThreeDigitsHexColourString_edgeCase2() {
        // Given
        val colour = 9
        val expected = "#009"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertTinyIntegerToThreeDigitsHexColourString_edgeCase1() {
        // Given
        val colour = 10
        val expected = "#00A"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertVerySmallIntegerToThreeDigitsHexColourString_edgeCase2() {
        // Given
        val colour = 16
        val expected = "#010"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToThreeDigitsHexColourString_edgeCase1() {
        // Given
        val colour = 256
        val expected = "#100"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToThreeDigitsHexColourString_edgeCase2() {
        // Given
        val colour = 4095
        val expected = "#FFF"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToSixDigitsHexColourString_edgeCase1() {
        // Given
        val colour = 4096
        val expected = "#001000"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertSmallIntegerToSixDigitsHexColourString_edgeCase2() {
        // Given
        val colour = 65535
        val expected = "#00FFFF"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertMediumIntegerToSixDigitsHexColourString_edgeCase1() {
        // Given
        val colour = 65536
        val expected = "#010000"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertMediumIntegerToSixDigitsHexColourString_edgeCase2() {
        // Given
        val colour = 1048575
        val expected = "#0FFFFF"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertLargeIntegerToSixDigitsHexColourString_edgeCase1() {
        // Given
        val colour = 1048576
        val expected = "#100000"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun shouldConvertLargeIntegerToSixDigitsHexColourString_edgeCase2() {
        // Given
        val colour = 16777215
        val expected = "#FFFFFF"
        // When
        val actual = colour.toHexColourString()
        // Then
        assertEquals(expected, actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowException_whenIntegerIsNegativeNumber() {
        // Given
        val actual = -1
        // When
        actual.toHexColourString()
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowException_whenIntegerIsTooBigForAColour() {
        // Given
        val actual = 16777216
        // When
        actual.toHexColourString()
    }

    @Test
    fun shouldCapitalizeFirstLetterOfTheString() {
        // Given
        val testWord = "happy"
        val expected = "Happy"
        // When
        val actual = testWord.capitalize()
        // Then
        assertEquals(expected, actual)
    }

    @Test (expected = IllegalArgumentException::class)
    fun shouldThrowException_whenCapitalizeIsCalledWithEmptyString() {
        // Given
        val testWord = ""
        // When
        testWord.capitalize()
    }
}