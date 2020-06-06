package com.ludo.game.network

import com.badlogic.gdx.Gdx
import com.ludo.game.Ludo
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException as JSONException

class SocketClientController (ludo: Ludo) {

    val gameController = GameController(ludo)
    private val SERVERL_URL = "http://localhost:8080"
    private val socket = IO.socket(SERVERL_URL)

    init {
        configSocketEvents()
        socket.connect()
    }

    private fun configSocketEvents() {
        socket
                .on(Socket.EVENT_CONNECT, this::connectionEstablished)
                .on("socketID", this::retrieveSocketId)
                .on("playerConnected", this::playerConnected)
                .on("playerDisconnected", this::playerDisconnected)
                .on("playerMoved", this::retrievePlayers)
    }

    private fun connectionEstablished (args: Array<Any>) {
        Gdx.app.log("SocketIO", "Connected");
    }
    private fun retrieveSocketId(args: Array<Any>){
        val data = args[0] as JSONObject
        try {
            val id = data.getString("id");
            val color = data.getInt("color");
            Gdx.app.log("SocketIO", "My ID: " + id);
            gameController.setUpPlayer(id,color)
        } catch (e: JSONException) {
            Gdx.app.log("SocketIO", "Error getting ID");
        }

    }
    private fun playerConnected(args: Array<Any>){
        val data = args[0] as JSONObject
        try {
            val id = data.getString("id");
            val color = data.getInt("color");
            Gdx.app.log("SocketIO", "New Player Connect: " + id+ "color index:"+ color);
            gameController.addRivalPlayer(id,color)
        }catch( e: JSONException){
            Gdx.app.log("SocketIO", "Error getting New PlayerID");
        }
    }
    private fun playerDisconnected(args: Array<Any>){
        val data = args[0] as JSONObject
        try {
            val id = data.getString("id");
            gameController.removeRivalPlayer(id)
        }catch(e: JSONException){
            Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
        }
    }
    private fun retrievePlayers(args: Array<Any>){
             val objects =  args[0] as JSONArray
            val result = hashMapOf<String,Int>()
            try {

                for(i in 0 until objects.length()){
                    val obj = objects.getJSONObject(i)
                    result[obj.getString("id")] = obj.getInt("color")
                }
            gameController.setUpActualRivals(result)
            } catch( e: JSONException){
                Gdx.app.log("SocketIO", "Error getting Players");
            }

    }

}