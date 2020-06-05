package com.ludo.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.ludo.game.screens.MainGameScreen

class Ludo : Game() {

    override fun create() {
        setScreen(MainGameScreen())
    }

    override fun dispose() {
        super.dispose()
        screen.dispose()
    }
}