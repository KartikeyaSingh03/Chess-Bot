package com.ks.undefeatablebot.Client.http;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonStreamProcessor {

    void processJson(JsonNode json, ResponseContext context);
}
