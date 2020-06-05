package com.ludo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui.Image


class ResourceManager {

    lateinit var background : Image
    lateinit var blueToken : Sprite
    lateinit var greenToken : Sprite
    lateinit var redToken : Sprite
    lateinit var yellowToken : Sprite

    fun load(){
        background =  Image( Texture("gameBoard.png"))
        blueToken =  Sprite( Texture("tokenBlue.png"))
        greenToken =  Sprite( Texture("tokenGreen.png"))
        redToken =  Sprite( Texture("tokenRed.png"))
        yellowToken =  Sprite( Texture("tokenYellow.png"))
    }

    fun  getToken( color : Color) : Sprite?{
        return when (color) {
            Color.RED -> redToken
            Color.GREEN -> greenToken
            Color.YELLOW -> yellowToken
            Color.BLUE -> blueToken
            else -> null
        }
    }

}
