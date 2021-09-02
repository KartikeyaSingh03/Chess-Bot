package com.ks.undefeatablebot.Client.handler;

import com.ks.undefeatablebot.Client.models.GameStart;
import com.fasterxml.jackson.databind.node.ObjectNode;


public interface UserEventHandler {

    void incomingChallenge(ObjectNode challengeEvent);

    void gameStart(GameStart gameStartEvent);
}
