package com.ludo.game.widgets.board

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.ludo.game.Constants
import java.awt.Point
import java.util.*

class Board {
    // BoardField matrix that represents the 24 fields and home fields.
    lateinit var board: Array<Array<BoardField?>>

    // Holds the location of the blue home fields.
    var blueHomeFields: MutableList<Point>?

    // Holds the location of the yellow home fields.
    var yellowHomeFields: MutableList<Point>?

    // Holds the location of the red home fields.
    var redHomeFields: MutableList<Point>?

    // Holds the location of the green home fields.
    var greenHomeFields: MutableList<Point>?

    // The size of each field.
    private var mGridSize = 0f

    constructor(rows: Int, columns: Int, gridSize: Float, homeFieldNum: Int) {
        mGridSize = gridSize
        blueHomeFields = ArrayList<Point>(homeFieldNum)
        yellowHomeFields = ArrayList<Point>(homeFieldNum)
        redHomeFields = ArrayList<Point>(homeFieldNum)
        greenHomeFields = ArrayList<Point>(homeFieldNum)
        setupBoard(rows, columns)
    }

    // Configures the the board based on a JSON file.
    private fun setupBoard(rows: Int, columns: Int) {
        board = Array(rows) { arrayOfNulls(columns) }
        var token: BoardField
        var nextLocation: Point? = null
        val board = loadJSONBoard()
        var c = columns - 1
        var i = 0
        while (c >= 0) {
            var r = 0
            while (r < rows) {
                val type = board[i]
                when (type) {
                    Constants.LUDO_BOARD_NEXT_UP_TOKEN -> {
                        nextLocation = getNextUpLocation(r, c)
                    }
                    Constants.LUDO_BOARD_NEXT_DOWN_TOKEN -> {
                        nextLocation = getNextDownLocation(r, c)
                    }
                    Constants.LUDO_BOARD_NEXT_LEFT_TOKEN -> {
                        nextLocation = getNextLeftLocation(r, c)
                    }
                    Constants.LUDO_BOARD_NEXT_RIGHT_TOKEN -> {
                        nextLocation = getNextRightLocation(r, c)
                    }
                    Constants.LUDO_BOARD_NEXT_NULL_TOKEN -> {
                        nextLocation = null
                    }
                    Constants.LUDO_BOARD_BLUE_HOME -> {
                        nextLocation = null
                        blueHomeFields!!.add(Point(r, c))
                    }
                    Constants.LUDO_BOARD_YELLOW_HOME -> {
                        nextLocation = null
                        yellowHomeFields!!.add(Point(r, c))
                    }
                    Constants.LUDO_BOARD_RED_HOME -> {
                        nextLocation = null
                        redHomeFields!!.add(Point(r, c))
                    }
                    Constants.LUDO_BOARD_GREEN_HOME -> {
                        nextLocation = null
                        greenHomeFields!!.add(Point(r, c))
                    }
                    else -> nextLocation = null
                }
                val location = Point(r, c)
                token = BoardField(nextLocation, location)
                this.board[r][c] = token
                r++
                i++
            }
            c--
        }
    }

    // Get a home field point
    fun getHomePoint(playerType: Color, numOfTokensOnHome: Int): Point? {
        var homePoint: Point? = null
        if (playerType.equals(Color.BLUE)) {
            homePoint = blueHomeFields!![numOfTokensOnHome - 1]
        } else if (playerType.equals(Color.RED)) {
            homePoint = redHomeFields!![numOfTokensOnHome - 1]
        } else if (playerType.equals(Color.YELLOW)) {
            homePoint = yellowHomeFields!![numOfTokensOnHome - 1]
        } else if (playerType.equals(Color.GREEN)) {
            homePoint = greenHomeFields!![numOfTokensOnHome - 1]
        }
        return homePoint
    }

    // Returns the 4 different starting fields
    fun getStartingFields(): Array<BoardField?>? {
        return arrayOf(board[2][0], board[0][4], board[4][6], board[6][2])
    }

    // Loads the configuration JSON file
    private fun loadJSONBoard(): IntArray {
        val file = Gdx.files.internal(Constants.LUDO_BOARD_JSON_LEVEL)
        val root = JsonReader().parse(file)
        val boardElement: JsonValue = root.get(Constants.LUDO_BOARD_JSON_LEVEL_ARRAY_KEY)
        return boardElement.asIntArray()
    }

    private fun getNextUpLocation(row: Int, column: Int): Point {
        return Point(row, column + 1)
    }

    private fun getNextDownLocation(row: Int, column: Int): Point {
        return Point(row, column - 1)
    }

    private fun getNextLeftLocation(row: Int, column: Int): Point {
        return Point(row - 1, column)
    }

    private fun getNextRightLocation(row: Int, column: Int): Point {
        return Point(row + 1, column)
    }

    // returns a Vector2 with the coordinates used to display the Tokens on the screen
    fun getCenterOfLocation(row: Int, column: Int): Vector2? {
        return Vector2(row * mGridSize + mGridSize / 2,
                column * mGridSize + mGridSize / 2)
    }
}