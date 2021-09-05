package com.ks.undefeatablebot.Client.handler;

import com.ks.undefeatablebot.Client.LichessClient;
import com.ks.undefeatablebot.Client.models.GameStart;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserEvent {
    
    private LichessClient client;

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

        client.streamGameState(id, new GameEvent(client, id));
    };

    public void challengeCanceled(ObjectNode challengeEvent){
        System.out.println("Challenge Cancelled");
    };  

    public void gameFinish(ObjectNode challengeEvent){
        System.out.println("Game Finished");
    };
}