package com.ludo.game.widgets.players

import com.ludo.game.widgets.board.BoardField
import com.ludo.game.widgets.board.Token
import java.awt.Point

class RedStartBase(rows: Int, columns: Int, gridSize: Float, gridOX: Float,
                   gridOY: Float, numOfStartLocations: Int, startField: BoardField?, tokens: Array<Token?>) : StartBase(rows, columns, gridSize, gridOX, gridOY, numOfStartLocations, startField) {

    init {
        this.setUsableFields(tokens)
    }
    override fun setUsableFields(tokens: Array<Token?>) {
        /**
         * This is the our start field setup:
         * O X X
         * X O X
         * X X X
         */
        tokens[0]?.currLocation = Point(0, 2)
        this.mStartBase.get(0).get(2)?.token = tokens[0]
        tokens[1]?.currLocation = Point(1, 1)
        this.mStartBase.get(1).get(1)?.token = tokens[1]
    }

    override fun getAvailableStartBoardField(): BoardField? {
        var startBase: BoardField? = null
        if (this.mStartBase.get(0).get(2)?.token == null) {
            startBase = this.mStartBase.get(0).get(2)
        } else if (this.mStartBase.get(1).get(1)?.token == null) {
            startBase = this.mStartBase.get(1).get(1)
        }
        return startBase
    }
}