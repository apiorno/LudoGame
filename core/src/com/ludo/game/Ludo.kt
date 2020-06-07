package com.ludo.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.ludo.game.network.SocketClientController
import com.ludo.game.screens.MainGameScreen
import com.ludo.game.widgets.board.Board
import com.ludo.game.widgets.board.Token
import com.ludo.game.widgets.players.Player
import com.ludo.game.widgets.players.StartBase
import com.ludo.game.widgets.players.StartBaseFactory

class Ludo : Game() {

    private val rivals = hashMapOf<String,Player>()

    lateinit var id: String
    lateinit var player: Player
    var resourceManager = ResourceManager ()
    lateinit var board :Board
    lateinit var socketClientController : SocketClientController

    override fun create() {
        resourceManager.load()
        board = Board(Constants.BOARD_GRID_ROWS, Constants.BOARD_GRID_COLUMNS,
                Constants.BOARD_GRID_SIZE, Constants.HOME_TOTAL_FIELDS)
        setScreen(MainGameScreen())
        socketClientController = SocketClientController(this)
    }

    override fun dispose() {
        super.dispose()
        screen.dispose()
        socketClientController.dispose()
    }
    private fun newPlayerResolvingColorWith(colorIndex: Int) :Player{

        val startBases = board.getStartingFields()
        val types: Array<Color> = arrayOf(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN)
        val type = types.get(colorIndex)
        val tokens: Array<Token?> = arrayOfNulls(Constants.START_FIELD_LOCATION_NUMBER)
            for (j in 0 until Constants.START_FIELD_LOCATION_NUMBER) {
                tokens[j] = Token(type, resourceManager.getToken(type)!!)
            }
            // create player start base.
            val sb: StartBase = StartBaseFactory.createStartBase(type,
                    Constants.START_FIELD_GRID_ROWS,
                    Constants.START_FIELD_GRID_COLUMNS,
                    Constants.START_FIELD_GRID_SIZE,
                    Constants.START_FIELD_LOCATION_NUMBER,
                    startBases!![colorIndex],
                    tokens)!!
            // create player.
        return Player(sb, tokens, type)

    }
    fun setUpPlayer(id: String,colorIndex: Int) {
        val player = newPlayerResolvingColorWith(colorIndex)
        this.id = id
        this.player=player
    }
    fun addRival(id: String,colorIndex: Int) {
        val rival = newPlayerResolvingColorWith(colorIndex)
        rivals[id] = rival
    }
    fun removeRival(id: String) {
        rivals.remove(id)
    }
    fun addRivals(rivals: HashMap<String, Int>) {
        rivals.entries.forEach{
            val rival = newPlayerResolvingColorWith(it.value)
            this.rivals[it.key] = rival
        }

    }
}