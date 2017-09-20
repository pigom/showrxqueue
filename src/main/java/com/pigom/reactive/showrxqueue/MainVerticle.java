package com.pigom.reactive.showrxqueue;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new RedisChannelVerticle());
        vertx.deployVerticle(new MessageVerticle());
    }
}
