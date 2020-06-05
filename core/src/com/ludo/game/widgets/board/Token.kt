package com.ludo.game.widgets.board

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ludo.game.Constants
import java.awt.Point

class Token(  val tokenType: Color,  val sprite: Sprite) : Image(sprite) {
    var movedSpaces = 0
    var currLocation: Point
    var possibleFutureLocation: Point
    private var canMove = false
    var isBase = false
    var isHome = false
    var isField = false

    init{
        movedSpaces = 0
        canMove = false
        isBase = true
        isHome = false
        isField = false
        currLocation = Point(0, 0)
        possibleFutureLocation = Point(0, 0)
    }

    fun setCanMove(canMove: Boolean, possibleFutureLocation: Point?) {
        this.possibleFutureLocation = (if (canMove) possibleFutureLocation else null) as Point
        val alpha: Float = if (canMove) Constants.TOKEN_SELECTABLE_ALPHA else Constants.TOKEN_NOT_SELECTABLE_ALPHA
        this.canMove = canMove
        val c = Color(sprite!!.color)
        c[c.r, c.g, c.b] = alpha
        this.setColor(c)
    }

    fun getCanMove(): Boolean {
        return canMove
    }
}