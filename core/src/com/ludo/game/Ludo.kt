package com.ludo.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class Ludo : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var board: Sprite
    lateinit var red: Texture
    lateinit var mapRenderer: OrthogonalTiledMapRenderer
    lateinit var map: TiledMap
    lateinit var camera : OrthographicCamera
    override fun create() {
        val viewportWidth =  Gdx.graphics.width.toFloat()
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
        red = Texture("redgotty.png")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        board.draw(batch)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}