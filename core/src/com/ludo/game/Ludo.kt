package com.ludo.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Ludo : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var board: Texture
    lateinit var red: Texture
    override fun create() {
        batch = SpriteBatch()
        board = Texture("board.png")
        red = Texture("redgotty.png")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(board, 0f, 0f,Gdx.graphics.width.toFloat(),Gdx.graphics.height.toFloat())
        batch.draw(red, 71f, 340f,red.width.toFloat()/2,red.height.toFloat()/2)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        board.dispose()
    }
}