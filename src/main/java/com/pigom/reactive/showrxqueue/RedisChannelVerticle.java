package com.pigom.reactive.showrxqueue;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;
import io.vertx.core.AbstractVerticle;


public class RedisChannelVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        RedisClient client = RedisClient.create(RedisURI.create("lapqc05987",6379));
        StatefulRedisPubSubConnection<String, String> connection = client.connectPubSub();


        RedisPubSubReactiveCommands<String, String> reactive = connection.reactive();
        reactive.subscribe("pigom").subscribe();

        reactive.observeChannels().doOnNext(channelMessage -> {
            System.out.println("Message received!" + channelMessage.getMessage());
            vertx.eventBus().publish("com.pigom.ch1", channelMessage.getMessage());
        }).subscribe();


    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
