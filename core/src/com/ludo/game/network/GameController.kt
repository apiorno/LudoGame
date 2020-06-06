package com.ludo.game.network

import com.ludo.game.Ludo
import com.ludo.game.widgets.players.Player

class GameController(private val ludo: Ludo) {


    fun setUpPlayer(id : String,colorIndex: Int){
        ludo.setUpPlayer(id,colorIndex)
    }
    fun addRivalPlayer(id: String, colorIndex: Int){
        ludo.addRival(id,colorIndex)
    }
    fun removeRivalPlayer(id : String){
        ludo.removeRival(id)
    }
    fun setUpActualRivals (rivals : HashMap<String, Int>){
        ludo.addRivals(rivals)
    }
}