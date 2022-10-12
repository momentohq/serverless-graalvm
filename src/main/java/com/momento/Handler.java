package com.momento;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import momento.sdk.SimpleCacheClient;
import momento.sdk.messages.CacheGetResponse;

public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, String> {
    private static final String MOMENTO_AUTH_TOKEN = "REPLACE_ME";
    private static final String CACHE_NAME = "default";
    private static final String KEY_NAME = "test_key";
    SimpleCacheClient simpleCacheClient = SimpleCacheClient.builder(MOMENTO_AUTH_TOKEN, 3600).build();

    @Override
    public String handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        return simpleCacheClient
                .get(CACHE_NAME, KEY_NAME)
                .string()
                .orElse("NOT FOUND");
    }
}
