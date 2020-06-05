package com.ludo.game.widgets.players

import com.badlogic.gdx.graphics.Color
import com.ludo.game.widgets.board.Token

class Player(val startBase: StartBase?, val tokens: Array<Token?>, val playerType: Color){


    // returns the amount of tokens that are on the home field
    fun numberOfTokensHome(): Int {
        var tokensHome = 0
        for (i in tokens.indices) {
            val token: Token = tokens[i]
            if (token.isHome) {
                tokensHome++
            }
        }
        return tokensHome
    }
}