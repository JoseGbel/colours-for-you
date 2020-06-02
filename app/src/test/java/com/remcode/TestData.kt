package com.remcode

import com.remcode.coloursforyou.data.models.Colour

class TestData {
    companion object {
        val COLOURNAME: String = "aWord"
        val COLOURNAMES = listOf(COLOURNAME)
        val HEXCOLOUR: String = "#FFFFFF"
        val COLOUR: Colour = Colour(HEXCOLOUR, COLOURNAME)
    }
}