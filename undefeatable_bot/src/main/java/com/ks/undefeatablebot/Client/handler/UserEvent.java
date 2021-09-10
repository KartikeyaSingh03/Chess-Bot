package com.ks.undefeatablebot.Client.handler;

import com.ks.undefeatablebot.Client.LichessClient;
import com.ks.undefeatablebot.Client.models.GameStart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserEvent {
    
    private LichessClient client;
    private LichessClient gameClient;
    private static final Logger log = LoggerFactory.getLogger(UserEvent.class);
    public GameThread gameThread;

    public UserEvent(LichessClient client){
        this.client = client;
    }
 
    public void incomingChallenge(ObjectNode challengeEvent){
        System.out.println("Incoming Challenge");
        String challengeId = challengeEvent.get("challenge").get("id").asText().toString();
        System.out.println(challengeId);
        client.acceptChallenge(challengeId);
    };

    public void gameStart(GameStart gameStartEvent){
        System.out.println("Game Start");
        String id = gameStartEvent.getId();
        System.out.println(id);

        gameThread = new GameThread(id);
        gameThread.start();
            
    };

    public void challengeCancelled(ObjectNode challengeEvent){
        System.out.println("Challenge Cancelled");
    };  

    public void gameFinish(ObjectNode challengeEvent){
        System.out.println("Game Finished");
        // try {
        //     wait();
        // } catch (InterruptedException e1) {
        //     e1.printStackTrace();
        // }

        // try {
        //     gameClient.close();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    };

    public class GameThread extends Thread {
 
        private String id;

        GameThread(String game_id) {
            this.id = game_id;
        }

        @Override
        public void run(){
            gameClient = new LichessClient("lip_ZKLrW84w38uiKLcdrrfC");
            client.streamGameState(id, new GameEvent(gameClient, id));    
        }


    };
}