package com.ks.undefeatablebot.Client.handler;

import java.util.Scanner;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ks.undefeatablebot.Client.LichessClient;
import com.ks.undefeatablebot.Client.models.Status;

public class GameEvent {

    private LichessClient client;
    private String id;
    private Boolean isWhite;

    GameEvent(LichessClient client, String id){
        this.client = client;
        this.id = id;
        this.isWhite = true;
    }

    private void checkColor(ObjectNode gameState){
        if(gameState.get("black").get("id").asText().toString().equals("undefeatable_bot")){
            isWhite = false;
        }
    }

    public void chatReceived(ObjectNode chat){
        System.out.println("Chat");
    };

    public void fullGameState(ObjectNode gameState){
        System.out.println("Full Game State");
        checkColor(gameState);
        System.out.println(isWhite);
        if(isWhite) {
            // Scanner sc = new Scanner(System.in);
            // String move = sc.nextLine();
            // Status status = client.makeMove(id, move);
            String move = "e2e4";
            Status status = client.makeMove(id, move);
            System.out.println(status.isOk());
        }
    };

    public void gameStateUpdate(ObjectNode gameState){
        // System.out.println("Game State Update");
        // Scanner sc = new Scanner(System.in);
        // String move = sc.nextLine();
        // String move = "e7e5";
        // Status status = client.makeMove(id, move);
        // System.out.println(status.isOk());
        
        
        Status status = client.abortGame(id);
        System.out.println(status.isOk());
    };
}
