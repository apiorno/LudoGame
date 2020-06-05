package com.ludo.game.widgets.players

import com.ludo.game.widgets.board.BoardField
import com.ludo.game.widgets.board.Token
import com.badlogic.gdx.graphics.Color

class StartBaseFactory {

    companion object{fun createStartBase(type: Color,
                                         rows: Int,
                                         columns: Int,
                                         gridSize: Float,
                                         numOfStartLocations: Int,
                                         startField: BoardField?,
                                         tokens: Array<Token?>): StartBase? {
        val gridLeftMargin: Float
        val gridBottomMargin: Float
        val startBase: StartBase?
        when (type) {
            Color.RED -> {
                gridLeftMargin = 0f
                gridBottomMargin = 7 * gridSize
                startBase = RedStartBase(rows,
                        columns, gridSize, gridLeftMargin,
                        gridBottomMargin, numOfStartLocations, startField, tokens)
            }
            Color.GREEN -> {
                gridLeftMargin = 7 * gridSize
                gridBottomMargin = 0f
                startBase = GreenStartBase(rows,
                        columns, gridSize, gridLeftMargin,
                        gridBottomMargin, numOfStartLocations, startField, tokens)
            }
            Color.YELLOW -> {
                gridLeftMargin = 0f
                gridBottomMargin = 0f
                startBase = YellowStartBase(rows,
                        columns, gridSize, gridLeftMargin,
                        gridBottomMargin, numOfStartLocations, startField, tokens)
            }
            Color.BLUE -> {
                gridLeftMargin = 7 * gridSize
                gridBottomMargin = 7 * gridSize
                startBase = BlueStartBase(rows,
                        columns, gridSize, gridLeftMargin,
                        gridBottomMargin, numOfStartLocations, startField, tokens)
            }
            else -> startBase = null
        }
        return startBase
    }    }
}