package com.ludo.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.ludo.game.Constants
import com.ludo.game.ResourceManager
import com.ludo.game.widgets.board.Board
import com.ludo.game.widgets.board.BoardField
import com.ludo.game.widgets.board.Token
import com.ludo.game.widgets.players.Player
import com.ludo.game.widgets.players.StartBase
import com.ludo.game.widgets.players.StartBaseFactory
import java.awt.Point
import kotlin.random.Random

class MainGameScreen: Screen {

    lateinit var mStage: Stage

    // BoardLevel instance.
    lateinit var mBoardLevel: Board

    // The players.
    lateinit var  mPlayers: Array<Player?>

    // A dice TextButton used to determine the next roll.
    lateinit var mDice: TextButton

    // The value of the last rolled dice.
    private var mRolledValue = 0

    // The number o used dices before passing the turn to another player.
    private var mDiceRoles = 0

    // Used with the mPlayers Array we get a reference to the current player.
    private var mCurrentPlayerIndex = 0

    // Elapsed time used for the Dice roll.
    private var mElapsedTime = 0f

    // Blocks or unlocks the startPhase.
    private var mStartPhase = false
    var resourceManager = ResourceManager ()

    override fun hide() {
    }

    override fun show() {
        resourceManager.load()
        mStage = Stage(FitViewport(Constants.BACKGROUND_IMAGE_WIDTH.toFloat(), Constants.BACKGROUND_IMAGE_HEIGHT.toFloat()))
        Gdx.input.inputProcessor = mStage
        mStage.addActor(this.resourceManager.background)
        val font = BitmapFont()
        val mDiceStyle = TextButtonStyle()
        mDiceStyle.font = font
        mDiceStyle.font.data.setScale(Constants.DICE_TEXT_SCALE.toFloat())
        mDice = TextButton("", mDiceStyle)
        val menuTable = Table()
        menuTable.add(mDice)
        menuTable.setFillParent(true)
        mStage.addActor(menuTable)
        mBoardLevel = Board(Constants.BOARD_GRID_ROWS, Constants.BOARD_GRID_COLUMNS,
                Constants.BOARD_GRID_SIZE, Constants.HOME_TOTAL_FIELDS)
        this.createPlayers()
        this.setupStartBases()
        this.nextPlayersTurn()
        mStartPhase = true
        /*val viewportWidth =  Gdx.graphics.width.toFloat()
        val viewportHeight = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera().apply {
            setToOrtho(false,viewportWidth,viewportHeight)
            update()
        }
        batch = SpriteBatch()
        board = Sprite(Texture("ludo.png")).apply {
            setOriginBasedPosition(viewportWidth/2f,viewportHeight/2f)
            setScale(.85f) }
        map = TmxMapLoader().load("ludo.tmx")
        mapRenderer = OrthogonalTiledMapRenderer(map)
        red = Texture("redgotty.png")*/
    }

    /**
     *
     * @param r amount of red from 0.0f to 1.0f
     * @param g amount of green from 0.0f to 1.0f
     * @param b amount of blue from 0.0f to 1.0f
     */
    fun setBackgroundColor(r: Float, g: Float, b: Float) {
        Gdx.gl.glClearColor(r, g, b, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun render(delta: Float) {
        this.startPhase()
        mStage.draw()
        /*batch.begin()
        board.draw(batch)
        batch.end()*/
    }
    private fun startPhase() {

        // get the number of tokens that are already home.
        val player: Player = this.getCurrentPlayer()
        val tokensHome = player.numberOfTokensHome()
        if (tokensHome == Constants.START_FIELD_LOCATION_NUMBER) {
            //Victory
            mDice.setText(Constants.VICTORY_MESSAGE)
            return
        }

        // Slow down how fast the dice is rolled.
        if (!mStartPhase || mElapsedTime < Constants.SECONDS_FOR_NEXT_ROLL) {
            mElapsedTime += Gdx.graphics.deltaTime
            return
        }

        // Roll the dice.
        this.rollDice()
        /**
         * If there is attlist one token on the field or the rolled value is 6
         * go to the 2 part of the game loop.
         */
        val allTokensInStartBase: Boolean = allTokensInStartBase()
        val playerTokensInStartBase: Boolean = playerTokensInStartBase()
        if (!playerTokensInStartBase || mRolledValue == Constants.ROLLED_VALUE_TO_SPAWN_TOKEN) {
            this.selectionPhase()
        } else if (allTokensInStartBase && mDiceRoles < Constants.MAX_NUM_OF_ROLLED_DICES) {
            startPhase()
        } else {
            this.nextPlayersTurn()
        }
    }

    private fun selectionPhase() {
        // disable the startPhase method.
        mStartPhase = false
        // get the current player.
         var player = getCurrentPlayer()
        // the the current player tokens.
         val tokens: Array<Token?> = player.tokens

        // loop through all the current player tokens.
        for (token in tokens) {
            // is the token is in is home let him be
            if (token!!.isHome) {
                continue;
            }
            // get the current player start location.
             var startLocation : Point = player.startBase?.startField?.location!!
            // get the possible future location of the token.
            var futureLocation : Point? = getFutureLocation(token, mRolledValue);
            // get the board field of the future location.
             var boardField : BoardField? = if(futureLocation != null)  this.mBoardLevel.board[futureLocation.getX().toInt()][futureLocation.getY().toInt()] else null;

            // if the board is not null and has a token with tokenType.
            if (boardField?.token != null) {
                // enable token for throwing opponent token.
                if (boardField.token!!.tokenType != token.tokenType) {
                    token.setCanMove(true, boardField.location);
                }
                // own token at target position.
                else if (boardField.token!!.tokenType == token.tokenType) {
                    token.setCanMove(false, null)
                }
            }
            else if (boardField != null) {
                token.setCanMove(true, boardField.location)
            }
            // enable token at start field
            else if (startLocation.equals(token.currLocation)) {
                token.setCanMove(true, futureLocation)
            }
            // dice score 6 and min 1 token in start base
            else if (this.mRolledValue == Constants.ROLLED_VALUE_TO_SPAWN_TOKEN
                    && player.startBase?.numOfFilledLocations!! > 0) {
                token.setCanMove(true, startLocation)
                token.isBase = false;
                token.isField = true;
                player.startBase?.numOfFilledLocations!!.dec();
                val pointToRemove :Point =  Point(token.currLocation.getX().toInt(), token.currLocation.getY().toInt());
                player.startBase?.removeToken(pointToRemove);
                break;
            } else {
                token.setCanMove(false, null);
            }
        }

        // simulate a decision from the user.
        this.fakeGestureDidTouch();
    }

    // Should be used to process the touch on a token
    private fun fakeGestureDidTouch() {
        // since touches are not working, just select any token as long as it able too move
        val player: Player = this.getCurrentPlayer()
        val tokens = player.tokens
        for (i in tokens.indices) {
            val token = tokens[i]!!
            if (token.isField && token.getCanMove()) {
                this.evaluationPhase(token)
                return
            }
        }
        // there is now possible play to make
        mStartPhase = true
        startPhase()
    }

    private fun evaluationPhase( token : Token) {
        // get the location the token might have in the future.
        var tokenPosition =  Point(token.possibleFutureLocation!!.getX().toInt(), token.possibleFutureLocation!!.getY().toInt())
        // get the board field corresponding to the given location.
         val boardField :BoardField? = if (token.isField)  this.mBoardLevel.board[tokenPosition.getX().toInt()][tokenPosition.getY().toInt()] else null
        // get the current player.
        var player:Player = getCurrentPlayer()
        // increment the number of moves the token gave.
        token.movedSpaces+= this.mRolledValue

        // check if we can go move to the home base.
        if (token.movedSpaces > Constants.SPACES_NEEDED_TO_GO_HOME) {
            token.isHome = true
            token.isField = false
            tokenPosition = this.mBoardLevel.getHomePoint(player.playerType, player.numberOfTokensHome())!!
        }
        // if there is an opponent in the location send it back to is base.
        else if (boardField?.token != null) {
            this.sendTokenToStartBase(boardField)
        }

        // move token token to its new place.
        this.mBoardLevel.board[tokenPosition.getX().toInt()][tokenPosition.getY().toInt()]?.token = token
        val tokenLocation : Vector2 = this.mBoardLevel.getCenterOfLocation(tokenPosition.getX().toInt(), tokenPosition.getY().toInt())!!
        token.setPosition(tokenLocation.x, tokenLocation.y);
        token.currLocation = tokenPosition;
        token.setCanMove(false, null);

        // if the dice roll is not 6 then its time to end the turn.
        if (this.mRolledValue != Constants.ROLLED_VALUE_TO_SPAWN_TOKEN) {
            this.nextPlayersTurn();
        }

        // unlock the startPhase method and call it.
        this.mStartPhase = true;
        this.startPhase();
    }

    private fun getFutureLocation(token: Token, rollValue: Int): Point? {
        if (!token.isField) {
            return null
        }
        var nextLocation = Point(token.currLocation.getX().toInt(), token.currLocation.getY().toInt())
        var boardField: BoardField
        for (i in 0 until rollValue) {
            boardField = mBoardLevel.board[nextLocation.getX().toInt()][nextLocation.getY().toInt()]!!
            nextLocation = Point(boardField.nextLocation?.getX()?.toInt()!!, boardField.nextLocation?.getY()?.toInt()!!)
        }
        return nextLocation
    }
    private fun rollDice() {
        mElapsedTime = 0f
        mDiceRoles++
        mRolledValue = Random.nextInt(6) + 1
        mDice.setText(Integer.toString(mRolledValue))
    }
    private fun allTokensInStartBase(): Boolean {
        for (player in mPlayers) {

            if (!player!!.startBase?.numOfFilledLocations?.equals(player.startBase?.numOfStartLocations)!!) {
                return false
            }
        }
        return true
    }
    private fun playerTokensInStartBase(): Boolean {
        val player: Player = getCurrentPlayer()
        return player.startBase?.numOfFilledLocations === player.startBase?.numOfStartLocations
    }

    private fun sendTokenToStartBase(boardField: BoardField) {
        val player: Player = getPlayerWithType(boardField.token!!.tokenType)!!
        val baseBoardField = player.startBase!!.getAvailableStartBoardField()
        if (baseBoardField != null) {
            boardField.token!!.isBase = true
            baseBoardField.token = boardField.token
            boardField.token = null
            val location: Point = boardField.location
            val tokenLocation = mBoardLevel.getCenterOfLocation(location.getX().toInt(), location.getY().toInt())
            baseBoardField.token!!.setPosition(tokenLocation!!.x, tokenLocation.y)
        }
    }
    private fun setupStartBases() {
        for (player in mPlayers) {
            val usedFields = player?.startBase!!.getUsedFields()
            val b = usedFields!!.iterator()
            while (b.hasNext()) {
                val boardField = b.next()
                val token = boardField.token
                val location: Point = boardField.location
                val tokenLocation = player.startBase.getCenterOfLocation(location.getX().toInt(), location.getY().toInt())
                token!!.setPosition(tokenLocation!!.x, tokenLocation.y)
                mStage.addActor(token)
            }
        }
    }

    private fun createPlayers() {
        mPlayers = arrayOfNulls(Constants.NUM_OF_PLAYERS)
        mCurrentPlayerIndex = 0
        val startBases = mBoardLevel.getStartingFields()
        val types: Array<Color> = arrayOf<Color>(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN)
        var tokens: Array<Token?>
        for (i in mPlayers.indices) {
            // create player tokens.
            tokens = arrayOfNulls(Constants.START_FIELD_LOCATION_NUMBER)
            for (j in 0 until Constants.START_FIELD_LOCATION_NUMBER) {
                tokens[j] = Token(types[i], resourceManager.getToken(types[i])!!)
            }
            // create player start base.
            val sb: StartBase = StartBaseFactory.createStartBase(types[i],
                    Constants.START_FIELD_GRID_ROWS,
                    Constants.START_FIELD_GRID_COLUMNS,
                    Constants.START_FIELD_GRID_SIZE,
                    Constants.START_FIELD_LOCATION_NUMBER,
                    startBases!![i],
                    tokens)!!
            // create player.
            mPlayers[i] = Player(sb, tokens, types[i])
        }
    }
    private fun nextPlayersTurn() {
        // reset the number of dice rolls, rolled value and elapsedTime
        mDiceRoles = 0
        mRolledValue = 0
        mElapsedTime = 0f
        //increment the mCurrentPlayerIndex when it reaches NUM_OF_PLAYERS go back to the start
        if (mCurrentPlayerIndex == Constants.NUM_OF_PLAYERS - 1) {
            mCurrentPlayerIndex = 0
        } else {
            mCurrentPlayerIndex++
        }
        when (mCurrentPlayerIndex) {
            Constants.RED_PLAYER_INDEX -> setBackgroundColor(Constants.BACKGROUND_RED_PLAYER_COLOR_R,
                    Constants.BACKGROUND_RED_PLAYER_COLOR_G,
                    Constants.BACKGROUND_RED_PLAYER_COLOR_B)
            Constants.BLUE_PLAYER_INDEX -> setBackgroundColor(Constants.BACKGROUND_BLUE_PLAYER_COLOR_R,
                    Constants.BACKGROUND_BLUE_PLAYER_COLOR_G,
                    Constants.BACKGROUND_BLUE_PLAYER_COLOR_B)
            Constants.GREEN_PLAYER_INDEX -> setBackgroundColor(Constants.BACKGROUND_GREEN_PLAYER_COLOR_R,
                    Constants.BACKGROUND_GREEN_PLAYER_COLOR_G,
                    Constants.BACKGROUND_GREEN_PLAYER_COLOR_B)
            Constants.YELLOW_PLAYER_INDEX -> setBackgroundColor(Constants.BACKGROUND_YELLOW_PLAYER_COLOR_R,
                    Constants.BACKGROUND_YELLOW_PLAYER_COLOR_G,
                    Constants.BACKGROUND_YELLOW_PLAYER_COLOR_B)
            else -> {
            }
        }
    }
    private fun getCurrentPlayer(): Player {
        return mPlayers[mCurrentPlayerIndex]!!
    }

    // returns the player type from the given player
    private fun getPlayerWithType(type: Color): Player? {
        for (player in mPlayers) {
            if (player?.playerType === type) {
                return player
            }
        }
        return null
    }
    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        /*batch.dispose()
        red.dispose()
        map.dispose()
        mapRenderer.dispose()*/
    }
}