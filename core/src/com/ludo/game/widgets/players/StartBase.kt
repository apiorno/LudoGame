package com.ludo.game.widgets.players

import com.badlogic.gdx.math.Vector2
import com.ludo.game.widgets.board.BoardField
import com.ludo.game.widgets.board.Token
import java.awt.Point
import java.util.*

abstract class StartBase(val rows: Int, val columns: Int, val gridSize: Float, val gridOX: Float, val gridOY: Float, val numOfStartLocations: Int, val startField: BoardField?) {

    // BoardField matrix.
    protected var mStartBase: Array<Array<BoardField?>> = Array(rows) { arrayOfNulls<BoardField?>(columns) }
    var numOfFilledLocations : Int = numOfStartLocations

    init {
        setupStartBase(startField)
    }

    // returns the fields that are currently being used.
    fun getUsedFields(): List<BoardField>? {
        val userFields: MutableList<BoardField> = ArrayList<BoardField>()
        for (r in 0 until rows) {
            for (c in 0 until columns) {
                val boardField: BoardField? = mStartBase[r][c]
                if (boardField?.token != null) {
                    userFields.add(boardField)
                }
            }
        }
        return userFields
    }

    // removes a token from the start base based on the given point.
    fun removeToken(p: Point) {
        mStartBase[p.getX().toInt()][p.getY().toInt()]?.token = null
    }

    // Creates board fields for the start base, 9 for now even if we only use 2.
    private fun setupStartBase(startField: BoardField?) {
        for (r in 0 until rows) {
            for (c in 0 until columns) {
                val location = Point(r, c)
                val boardField = BoardField(startField?.location, location)
                mStartBase[r][c] = boardField
            }
        }
    }

    // Returns the Vector2 coordinates for the Token to be displayed
    fun getCenterOfLocation(row: Int, column: Int): Vector2? {
        return Vector2(row * gridSize + gridOX + 3 * gridSize / 4,
                column * gridSize + gridOY + 3 * gridSize / 4)
    }

    // Configures and sets the fields that will be used.
    abstract fun setUsableFields(tokens: Array<Token?>)

    // Returns an available BoardField
    abstract fun getAvailableStartBoardField(): BoardField?
}