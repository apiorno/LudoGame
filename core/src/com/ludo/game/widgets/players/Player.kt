package com.ludo.game.widgets.players

import com.badlogic.gdx.graphics.Color
import com.ludo.game.widgets.board.Token

class Player(val startBase: StartBase?, val tokens: Array<Token?>, val playerType: Color){


    // returns the amount of tokens that are on the home field
    fun numberOfTokensHome(): Int {
        var tokensHome = 0
        for (token in tokens) {
            if (token!!.isHome) {
                tokensHome++
            }
        }
        return tokensHome
    }
}