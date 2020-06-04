package com.ludo.game.network

import com.badlogic.gdx.Gdx
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException as JSONException

class SocketClientController {

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
        // player = new Starship(playerShip);
    }
    private fun retrieveSocketId(args: Array<Any>){
        val data = args[0] as JSONObject
        try {
            val id = data.getString("id");
            Gdx.app.log("SocketIO", "My ID: " + id);
        } catch (e: JSONException) {
            Gdx.app.log("SocketIO", "Error getting ID");
        }

    }
    private fun playerConnected(args: Array<Any>){
        val data = args[0] as JSONObject
        try {
            val id = data.getString("id");
            Gdx.app.log("SocketIO", "New Player Connect: " + id);
            //friendlyPlayers.put(id, new Starship(friendlyShip));
        }catch( e: JSONException){
            Gdx.app.log("SocketIO", "Error getting New PlayerID");
        }
    }
    private fun playerDisconnected(args: Array<Any>){
        val data = args[0] as JSONObject
        try {
            val id = data.getString("id");
            //friendlyPlayers.remove(id);
        }catch(e: JSONException){
            Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
        }
    }
    private fun retrievePlayers(args: Array<Any>){
             val objects =  args[0] as JSONArray
            try {

                for(i in 0 until objects.length()){
                    /*Starship coopPlayer = new Starship(friendlyShip);
                    Vector2 position = new Vector2();
                    position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                    position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                    coopPlayer.setPosition(position.x, position.y);
                    friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);*/
                }
            } catch( e: JSONException){
                Gdx.app.log("SocketIO", "Error getting Players");
            }

    }

}