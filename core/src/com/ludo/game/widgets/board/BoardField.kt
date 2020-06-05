package com.ludo.game.widgets.board

import java.awt.Point

class BoardField( val nextLocation: Point?, val location: Point) {

    // every board field may have a token.
    var token: Token? = null

}