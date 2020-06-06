package com.ludo.game.widgets.players

import com.ludo.game.widgets.board.BoardField
import com.ludo.game.widgets.board.Token
import java.awt.Point

class BlueStartBase(rows: Int, columns: Int, gridSize: Float, gridOX: Float,
                    gridOY: Float, numOfStartLocations: Int, startField: BoardField?, tokens: Array<Token?>) : StartBase(rows, columns, gridSize, gridOX, gridOY, numOfStartLocations, startField) {


    init {
        this.setUsableFields(tokens)
    }

    override fun setUsableFields(tokens: Array<Token?>) {
        /**
         * This is the our start field setup:
         * X X O
         * X O X
         * X X X
         */
        tokens[0]?.currLocation = Point(2, 2)
        mStartBase[2][2]!!.token = tokens[0]
        tokens[1]?.currLocation = Point(1, 1)
        mStartBase[1][1]!!.token = tokens[1]
    }

    override fun getAvailableStartBoardField(): BoardField? {
        var startBase: BoardField? = null
        if (mStartBase[2][2]!!.token == null) {
            startBase = mStartBase[2][2]
        } else if (mStartBase[1][1]!!.token == null) {
            startBase = mStartBase[1][1]
        }
        return startBase
    }
}
