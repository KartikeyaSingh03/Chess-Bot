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

    private int numMoves(String moves){
        return moves.split(" ").length;
    }

    private Boolean botTurn(int num_moves){
        if((num_moves%2 == 0 && isWhite) || (num_moves%2 == 1 && !isWhite))
            return true;
        return false;
    }

    public void chatReceived(ObjectNode chat){
        System.out.println("Chat");
    };

    public void fullGameState(ObjectNode gameState){
        System.out.println("Full Game State");
        checkColor(gameState);
        System.out.println(isWhite);
        if(isWhite) {
            Scanner sc = new Scanner(System.in);
            String move = sc.nextLine();
            Status status = client.makeMove(id, move);
            System.out.println(status.isOk());
        }
    };

    public void gameStateUpdate(ObjectNode gameState){
        System.out.println("Game State Update");

        String stat = gameState.get("status").asText().toString();                

        System.out.println(stat);

        if(stat.equals("mate") || stat.equals("resign") || stat.equals("aborted")){
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // notifyAll();
            return; 
        }

        String moves =  gameState.get("moves").asText().toString();

        int num_moves = numMoves(moves);

        if(botTurn(num_moves)){
            Scanner sc = new Scanner(System.in);
            String move = sc.nextLine();
            Status status = client.makeMove(id, move);
            System.out.println(status.isOk());
        }
 
    };
}
