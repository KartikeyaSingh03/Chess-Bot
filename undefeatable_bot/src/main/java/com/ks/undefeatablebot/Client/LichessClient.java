package com.ks.undefeatablebot.Client;


import com.ks.undefeatablebot.Client.handler.GameEvent;
import com.ks.undefeatablebot.Client.handler.UserEvent;
import com.ks.undefeatablebot.Client.http.Json;
import com.ks.undefeatablebot.Client.http.JsonClient;
import com.ks.undefeatablebot.Client.http.JsonResponse;
import com.ks.undefeatablebot.Client.models.Status;
import com.ks.undefeatablebot.Client.models.Email;
import com.ks.undefeatablebot.Client.models.GameStart;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.Header;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.util.Collections;
import java.util.List;


public class LichessClient implements AutoCloseable{

    public static final String BASE_URL = "https://lichess.org";
    private final JsonClient httpClient;

    public LichessClient(String apiToken) {
        List<Header> defaultHeaders = Collections.singletonList(new BasicHeader("Authorization", "Bearer " + apiToken));
        this.httpClient = new JsonClient(HttpClientBuilder.create().setDefaultHeaders(defaultHeaders).build());
    }

    public ObjectNode getMyProfile() {
        return (ObjectNode) get(URLS.ACCOUNT + "/me");
    }

    public String getMyEmailAddress() {
        return get(URLS.ACCOUNT + "/email", Email.class).getEmail();
    }

    public Status upgradeToBotAccount() {
        return post(URLS.BOT + "/account/upgrade", Status.class);
    }

    public void streamIncomingEvents(UserEvent handler) {
        httpClient.getAndStream(URLS.STREAM + "/event", (json, context) -> {
            System.out.println(json.toString());
            ObjectNode node = (ObjectNode) json;
            String type = node.get("type").asText();
            if (type.equals("challenge")) {
                handler.incomingChallenge(node);
            } else if (type.equals("gameStart")) {
                handler.gameStart(Json.parseJson(node, GameStart.class));
            } else if(type.equals("challengeCanceled")){
                handler.challengeCanceled(node);
            } else if(type.equals("gameFinish")){
                handler.gameFinish(node);
            }
            else {
                throw new RuntimeException("Unhandled event type: '" + type + "' \nFull json: " + json);
            }
        });
    }

    public void streamGameState(String gameId, GameEvent handler) {
        httpClient.getAndStream(URLS.BOT + "/game/stream/" + gameId, (json, context) -> {
            System.out.println(json.toString());
            ObjectNode node = (ObjectNode) json;
            String type = node.get("type").asText();
            if (type.equals("gameFull")) {
                handler.fullGameState(node);
            } else if (type.equals("gameState")) {
                handler.gameStateUpdate(node);
            } else if (type.equals("chatLine")) {
                handler.chatReceived(node);
            }

        });
    }

    public Status makeMove(String gameId, String move) {
        String url = URLS.BOT + "/game/" + gameId + "/move/" + move;

        return post(url, Status.class);
    }

    public Status writeInChat(String gameId, String room, String message) {
        String url = URLS.BOT + "/game/" + gameId + "/chat";
        ObjectNode json = Json.createJsonObject();
        json.put("room", room);
        json.put("text", message);
        return post(url, json, Status.class);
    }

    public Status abortGame(String gameId) {
        String url = URLS.BOT + "/game/" + gameId + "/abort";
        return post(url, Status.class);
    }

    public Status resignGame(String gameId) {
        String url = URLS.BOT + "/game/" + gameId + "/resign";
        return post(url, Status.class);
    }

    public Status acceptChallenge(String challengeId) {
        return post(URLS.CHALLENGE + "/" + challengeId + "/accept", Status.class);
    }

    public Status declineChallenge(String challengeId) {
        return post(URLS.CHALLENGE + "/" + challengeId + "/decline", Status.class);
    }

    private JsonNode get(String url) {
        try (JsonResponse response = httpClient.get(url)) {
            JsonNode json = response.toJson();
            response.close();
            return json;
        }
    }

    private <T> T get(String url, Class<T> toConvertTo) {
        try (JsonResponse response = httpClient.get(url)) {
            T object = response.toObject(toConvertTo);
            response.close();
            return object;
        }
    }

    private <T> T post(String url, Class<T> toConvertTo) {
        try (JsonResponse response = httpClient.post(url)) {
            System.out.println(response.toString());
            T object = response.toObject(toConvertTo);
            response.close();
            return object;
        }
    }

    private <T> T post(String url, ObjectNode postData, Class<T> toConvertTo) {
        try (JsonResponse response = httpClient.post(url, postData)) {
            T object = response.toObject(toConvertTo);
            response.close();
            return object;
        }
    }

    @Override
    public void close() throws Exception {
        httpClient.close();
    }

}
